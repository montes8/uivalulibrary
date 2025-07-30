package com.gb.vale.uivalulibrary.manager.finger

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.validateNewFinger
import java.util.concurrent.Executor

class UiTayAuthenticationManager(
    private var activity: AppCompatActivity?,
    var listener: AuthenticationHandler? = null
) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo

    interface AuthenticationHandler {
        fun onSucceeded(result: BiometricPrompt.AuthenticationResult)
        fun onFailed(): (() -> Unit)? = null

        fun onDetectedChangeConfig(): (() -> Unit)? = null

        fun onError(errorCode: Int, errorString: String): (() -> Unit)? = null
    }

    companion object {
        fun validateIfCanAuthenticateWithBiometrics(context: Context?): UiTayFingerStatus {
            return context?.let {
                val biometricManager = BiometricManager.from(it)
                return when (biometricManager.canAuthenticate()) {
                    BiometricManager.BIOMETRIC_SUCCESS -> UiTayFingerStatus.CAN_AUTHENTICATE
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> UiTayFingerStatus.NO_HARDWARE
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> UiTayFingerStatus.UNAVAILABLE
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> UiTayFingerStatus.NO_FINGERPRINTS
                    else -> UiTayFingerStatus.UNKNOWN_STATUS
                }
            } ?: UiTayFingerStatus.UNKNOWN_STATUS
        }
    }

    init {
        this.initializeAuthenticationMethods()
    }

    private fun initializeAuthenticationMethods() {
        try {
            activity?.let {
                this.executor = ContextCompat.getMainExecutor(it)
                this.biometricPrompt = BiometricPrompt(
                    it,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            listener?.onError(errorCode, it.getString(R.string.tay_ui_authentication_error
                                , errString.trim().toString()))
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)

                            listener?.onSucceeded(result)
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            listener?.onFailed()
                        }
                    })
                this.biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(it.resources.getString((R.string.app_name)))
                    .setSubtitle(it.getString(R.string.tay_ui_put_finger_to_continue))
                    .setNegativeButtonText(it.getString(R.string.tay_ui_btn_cancel))
                    .setConfirmationRequired(true)
                    .build()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    fun showAuthenticationDialog(statusType: UiTayFingerStatus?,keyName:String = UI_TAY_EMPTY) {
        if (keyName.isNotEmpty()){
            val statusFinger = validateNewFinger(keyName)
            if (statusFinger.first && statusType == UiTayFingerStatus.CAN_AUTHENTICATE) {
                statusFinger.second?.let {
                    biometricPrompt.authenticate(
                        this.biometricPromptInfo,
                        BiometricPrompt.CryptoObject(it)
                    )
                }
            } else {
                listener?.onDetectedChangeConfig()
            }
        }else{
            if (statusType == UiTayFingerStatus.CAN_AUTHENTICATE) {
                    biometricPrompt.authenticate(this.biometricPromptInfo) }
        }

    }

}