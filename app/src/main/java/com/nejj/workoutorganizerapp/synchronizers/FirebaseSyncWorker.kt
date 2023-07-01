package com.nejj.workoutorganizerapp.synchronizers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlin.random.Random

class FirebaseSyncWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        //startForegroundService()
        val workoutRepository = WorkoutRepository(WorkoutDatabase(context))
        val firestoreSynchronizationManager = FirestoreSynchronizationManager(workoutRepository)
        firestoreSynchronizationManager.synchronize()

        return Result.success()
    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "FirebaseSyncChannel")
                    .setContentTitle("Firebase Sync")
                    .setContentText("Syncing data with Firebase")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build()
            )
        )
    }
}