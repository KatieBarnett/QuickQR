package dev.veryniche.quickqr

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.storage.QRCodesRepository
import javax.inject.Inject

@HiltViewModel
class ExpandedCodeViewModel @Inject constructor(
    private val qrCodesRepository: QRCodesRepository
) : ViewModel() {

    fun getQRCodeItem(id: Int?) = qrCodesRepository.getQRCode(id ?: -1)

}
