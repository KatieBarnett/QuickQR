package dev.veryniche.quickqr.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dev.veryniche.quickqr.R
import timber.log.Timber

class BarcodeClientHelper(val context: Context, val errorInstallingBarcodeScanner: (Int) -> Unit) {

    val moduleInstallClient = ModuleInstall.getClient(context)
    private val scanner = GmsBarcodeScanning.getClient(context)
    private val listener = ModuleInstallProgressListener(context)

    fun checkInstallBarcodeModule() {
        moduleInstallClient
            .areModulesAvailable(scanner)
            .addOnSuccessListener {
                if (it.areModulesAvailable()) {
                    Timber.d("Barcode module is available")
                } else {
                    Timber.d("Barcode module is not installed")
                    installBarcodeModule()
                }
            }
            .addOnFailureListener {
                Timber.e("Error installing modules", it)
                errorInstallingBarcodeScanner.invoke(R.string.module_download_unavailable)
            }
    }

    fun installBarcodeModule() {
        val moduleInstallRequest =
            ModuleInstallRequest.newBuilder()
                .setListener(listener)
                .addApi(scanner) // Add the scanner client to the module install request
                .build()

        // Send an urgent module install request see https://developers.google.com/android/guides/module-install-apis#send_an_urgent_module_install_request
        // If you'd like the OS to determine the best time, use a deferred install request, but I'd recommend the urgent one
        // See https://developers.google.com/android/guides/module-install-apis#send_a_deferred_install_request
        moduleInstallClient
            .installModules(moduleInstallRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()) {
                    Timber.d("Modules are already installed")
                } else {
                    Timber.d("Start module install")
                    Toast.makeText(
                        context,
                        context.getString(R.string.module_download_started),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Timber.e("Error installing modules", it)
                errorInstallingBarcodeScanner.invoke(R.string.module_download_failed)
            }
    }

    inner class ModuleInstallProgressListener(val context: Context) : InstallStatusListener {

        private var progressLastReported: Int? = null

        @SuppressLint("StringFormatInvalid")
        override fun onInstallStatusUpdated(update: ModuleInstallStatusUpdate) {
            // Progress info is only set when modules are in the progress of downloading.
            update.progressInfo?.let {
                val progress = (it.bytesDownloaded * 100 / it.totalBytesToDownload).toInt()
                Timber.d("Module install progress: $progress")
                if (progressLastReported != progress) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.module_download_in_progress, progress),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressLastReported = progress
                }
            }

            if (isTerminateState(update.installState)) {
                progressLastReported = null
                moduleInstallClient.unregisterListener(this)
            }
        }

        fun isTerminateState(@ModuleInstallStatusUpdate.InstallState state: Int): Boolean {
            when (state) {
                STATE_CANCELED -> Timber.d("Module install progress is cancelled")
                STATE_COMPLETED -> {
                    Timber.d("Module install progress is completed")
                    Toast.makeText(
                        context,
                        context.getString(R.string.module_download_complete),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                STATE_FAILED -> {
                    Timber.d("Module install progress is failed")
                    Toast.makeText(
                        context,
                        context.getString(R.string.module_download_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED
        }
    }
}
