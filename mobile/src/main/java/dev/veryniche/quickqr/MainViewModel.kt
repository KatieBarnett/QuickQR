package dev.veryniche.quickqr

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val barcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

}