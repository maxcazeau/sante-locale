package com.santelocale

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.santelocale.data.database.DatabaseKeyManager
import com.santelocale.data.database.HealthDatabase

class SanteLocaleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Load SQLCipher native library
        System.loadLibrary("sqlcipher")
        handleDatabaseEncryption()
        createNotificationChannel()
    }

    /**
     * Ensures encrypted database is ready.
     * If this is first launch with encryption, deletes any old unencrypted database.
     */
    private fun handleDatabaseEncryption() {
        val keyManager = DatabaseKeyManager(this)

        // If no encryption key exists yet, this is either first install
        // or first launch after adding encryption - delete old unencrypted database
        if (!keyManager.hasExistingKey()) {
            HealthDatabase.deleteExistingDatabase(this)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rappels Quotidiens"
            val descriptionText = "Notifications pour les rappels de glycémie"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("reminders_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
