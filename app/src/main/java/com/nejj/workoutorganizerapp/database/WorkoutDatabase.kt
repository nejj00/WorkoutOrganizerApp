package com.nejj.workoutorganizerapp.database

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.models.converters.LocalDateConverter
import com.nejj.workoutorganizerapp.models.converters.LocalTimeConverter
import com.nejj.workoutorganizerapp.util.DatabaseQueryLoggerCallback
import java.util.concurrent.Executors

@Database(
    entities = [
        ExerciseCategory::class,
        Exercise::class,
        WorkoutRoutine::class,
        RoutineSet::class,
        LoggedExerciseSet::class,
        LoggedRoutineSet::class,
        LoggedWorkoutRoutine::class,
        LastLoggedInUser::class
    ],
    version = 25
)
@TypeConverters(
    LocalDateConverter::class,
    LocalTimeConverter::class
)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getExerciseCategoryDao(): ExerciseCategoryDao
    abstract fun getExerciseDao(): ExerciseDao
    abstract fun getWorkoutRoutineDao(): WorkoutRoutineDao
    abstract fun getRoutineSetDao(): RoutineSetDao
    abstract fun getLoggedWorkoutRoutineDao(): LoggedWorkoutRoutineDao
    abstract fun getLoggedRoutineSetDao(): LoggedRoutineSetDao
    abstract fun getLoggedExerciseSetDao(): LoggedExerciseSetDao
    abstract fun getLastLoggedInUserDao(): LastLoggedInUserDao
    companion object {
        @Volatile
        private var instance: WorkoutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context): WorkoutDatabase {
            val dbBuilder = Room.databaseBuilder(
                context.applicationContext,
                WorkoutDatabase::class.java,
                "workout_db.db"
            )
            dbBuilder.setQueryCallback(DatabaseQueryLoggerCallback(), Executors.newSingleThreadExecutor())

            return dbBuilder.fallbackToDestructiveMigration().build()
        }


    }
}