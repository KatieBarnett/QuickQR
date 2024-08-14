package dev.veryniche.quickqr.widgets

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.storage.QRCodesRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WidgetViewModel @Inject constructor(
    private val qrCodesRepository: QRCodesRepository,
) : ViewModel() {

    val availableQRCodes = qrCodesRepository.getQRCodes()

    var selectedQRCodeId = mutableStateOf<Int?>(null)

    val selectedQRCode = availableQRCodes.map { it.firstOrNull { it.id == selectedQRCodeId.value } }
}
