package dev.veryniche.quickqr.review

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import dev.veryniche.quickqr.analytics.trackReviewRequested
import dev.veryniche.quickqr.storage.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class ReviewManager(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    companion object {
        const val DAYS_SINCE_LAST_REVIEW = 30
        const val REVIEW_AFTER_NUMBER_OF_OPENS = 3
    }

    private val lastReviewFlow = userPreferencesRepository.userPreferencesFlow.map {
        Pair(
            it.lastReviewDate,
            it.numberOfOpens
        )
    }

    suspend fun requestReviewIfAble(activity: Activity, coroutineScope: CoroutineScope) {
        lastReviewFlow.collectLatest { lastReview ->
            val lastReviewDate = lastReview.first
            val numberOfOpens = lastReview.second
            val currentTimestamp = System.currentTimeMillis()
            val daysSinceLastReview = if (lastReviewDate > 0) {
                (currentTimestamp - lastReviewDate) / (1000 * 60 * 60 * 24)
            } else {
                -1
            }
            Timber.i(
                "Days since last review: $daysSinceLastReview, last review date: $lastReviewDate, number of opens: $numberOfOpens"
            )
            if (((numberOfOpens >= REVIEW_AFTER_NUMBER_OF_OPENS) && (lastReviewDate == -1L)) ||
                (daysSinceLastReview >= DAYS_SINCE_LAST_REVIEW)
            ) {
                val manager = ReviewManagerFactory.create(activity)
                val request = manager.requestReviewFlow()
                trackReviewRequested()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // We got the ReviewInfo object
                        val reviewInfo = task.result
                        val flow = manager.launchReviewFlow(activity, reviewInfo)
                        flow.addOnCompleteListener { _ ->
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.
                            coroutineScope.launch {
                                Timber.i("Updating last review date")
                                userPreferencesRepository.updateLastReviewDate()
                            }
                        }
                    } else {
                        Timber.e(task.exception, "Error loading review flow.")
                    }
                }
            }
        }
    }
}
