package com.nabha.telemedicine

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NabhaApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channels = listOf(
                NotificationChannel(
                    CHANNEL_APPOINTMENTS,
                    "Appointments",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Appointment reminders and updates"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_CONSULTATIONS,
                    "Consultations",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Video consultation notifications"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_PHARMACY,
                    "Pharmacy",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Medicine availability updates"
                },
                NotificationChannel(
                    CHANNEL_GENERAL,
                    "General",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General app notifications"
                }
            )

            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }

    companion object {
        const val CHANNEL_APPOINTMENTS = "channel_appointments"
        const val CHANNEL_CONSULTATIONS = "channel_consultations"
        const val CHANNEL_PHARMACY = "channel_pharmacy"
        const val CHANNEL_GENERAL = "channel_general"
    }
}
