package com.nejj.workoutorganizerapp.database

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory

@Database(
    entities = [ExerciseCategory::class, Exercise::class],
    version = 4
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getExerciseCategoryDao(): ExerciseCategoryDao
    abstract fun getExerciseDao(): ExerciseDao
    companion object {
        @Volatile
        private var instance: WorkoutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WorkoutDatabase::class.java,
                "workout_db.db"
            ).fallbackToDestructiveMigration().build()
    }
}