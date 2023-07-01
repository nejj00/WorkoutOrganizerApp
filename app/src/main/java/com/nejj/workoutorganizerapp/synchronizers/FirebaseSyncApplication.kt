package com.nejj.workoutorganizerapp.synchronizers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class FirebaseSyncApplication: Application() {

        override fun onCreate() {
            super.onCreate()

            val channel = NotificationChannel(
                "FirebaseSyncChannel",
                "Firebase Sync Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
}