package com.santelocale.utils

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.santelocale.worker.ReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleReminder(context: Context, hour: Int, minute: Int, tag: String, title: String, message: String) {
        val workManager = WorkManager.getInstance(context)

        val now = Calendar.getInstance()
        val due = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (due.before(now)) {
            due.add(Calendar.HOUR_OF_DAY, 24)
        }

        val initialDelay = due.timeInMillis - now.timeInMillis

        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .build()

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(tag)
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelReminder(context: Context, tag: String) {
        WorkManager.getInstance(context).cancelUniqueWork(tag)
    }
}
