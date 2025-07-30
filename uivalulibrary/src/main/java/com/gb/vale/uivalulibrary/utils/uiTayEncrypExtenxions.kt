package com.gb.vale.uivalulibrary.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

fun validateNewFinger(keyName:String):Pair<Boolean, Cipher?>{
    var cipher : Cipher? = null
    var isValid = true
    try{
        cipher = getCipher()
        val secretKey = getSecretKey(keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    }catch (e: Exception){
        e.printStackTrace()
        isValid = false
    }
    return Pair(isValid,cipher)
}

fun generateSecretKey(keyName:String) {
    val keyGenerator = KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    keyGenerator.init(
        KeyGenParameterSpec.Builder(keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build())
    keyGenerator.generateKey()
}

fun getSecretKey(keyName:String): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    return keyStore.getKey(keyName, null) as SecretKey
}

fun getCipher(): Cipher {
    return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
            + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
}