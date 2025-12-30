package com.santelocale.data.database

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Manages the SQLCipher database encryption key using Android Keystore.
 *
 * Key Management Strategy:
 * 1. Generate a random 256-bit key on first launch
 * 2. Encrypt this key using Android Keystore (hardware-backed when available)
 * 3. Store the encrypted key in SharedPreferences
 * 4. Retrieve and decrypt when opening the database
 */
class DatabaseKeyManager(private val context: Context) {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "sante_locale_db_key"
        private const val PREFS_NAME = "sante_locale_encrypted_prefs"
        private const val PREF_ENCRYPTED_KEY = "encrypted_db_key"
        private const val PREF_KEY_IV = "db_key_iv"
        private const val GCM_TAG_LENGTH = 128
        private const val DATABASE_KEY_SIZE = 32 // 256 bits for SQLCipher
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    /**
     * Get the database passphrase as a ByteArray (required by SupportFactory).
     * Creates the key if it doesn't exist.
     */
    fun getOrCreateDatabaseKey(): ByteArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        return if (prefs.contains(PREF_ENCRYPTED_KEY)) {
            decryptStoredKey(prefs)
        } else {
            generateAndStoreKey(prefs)
        }
    }

    private fun generateAndStoreKey(prefs: android.content.SharedPreferences): ByteArray {
        // Generate random database key
        val databaseKey = ByteArray(DATABASE_KEY_SIZE)
        SecureRandom().nextBytes(databaseKey)

        // Ensure Keystore key exists
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            createKeystoreKey()
        }

        // Encrypt the database key
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedKey = cipher.doFinal(databaseKey)
        val iv = cipher.iv

        // Store encrypted key and IV
        prefs.edit()
            .putString(PREF_ENCRYPTED_KEY, Base64.encodeToString(encryptedKey, Base64.NO_WRAP))
            .putString(PREF_KEY_IV, Base64.encodeToString(iv, Base64.NO_WRAP))
            .apply()

        return databaseKey
    }

    private fun decryptStoredKey(prefs: android.content.SharedPreferences): ByteArray {
        val encryptedKeyBase64 = prefs.getString(PREF_ENCRYPTED_KEY, null)
            ?: throw IllegalStateException("Encrypted key not found")
        val ivBase64 = prefs.getString(PREF_KEY_IV, null)
            ?: throw IllegalStateException("Key IV not found")

        val encryptedKey = Base64.decode(encryptedKeyBase64, Base64.NO_WRAP)
        val iv = Base64.decode(ivBase64, Base64.NO_WRAP)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        return cipher.doFinal(encryptedKey)
    }

    private fun createKeystoreKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val keySpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(false)
            .build()

        keyGenerator.init(keySpec)
        keyGenerator.generateKey()
    }

    /**
     * Check if a database key exists (database has been initialized before).
     */
    fun hasExistingKey(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(PREF_ENCRYPTED_KEY)
    }
}
