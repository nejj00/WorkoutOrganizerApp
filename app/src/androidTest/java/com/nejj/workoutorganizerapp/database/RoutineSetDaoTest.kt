package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.RoutineSet
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class RoutineSetDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var routineSetDao: RoutineSetDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        routineSetDao = database.getRoutineSetDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRoutineSet() = runTest {
        val routineSet = RoutineSet(1, 1, 1, 2, 3, 1, false, "")
        routineSetDao.upsert(routineSet)

        val allRoutineSets = routineSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allRoutineSets).contains(routineSet)
    }

    @Test
    fun deleteRoutineSet() = runTest {
        val routineSet = RoutineSet(1, 1, 1, 2, 3, 1, false, "")
        routineSetDao.upsert(routineSet)
        routineSetDao.deleteEntity(routineSet)

        val allRoutineSets = routineSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allRoutineSets).doesNotContain(routineSet)
    }

    @Test
    fun updateRoutineSetUID() = runTest {
        val newUID = "newUID"
        val routineSet1 = RoutineSet(null, 1, 1, 2, 3, 1, false, "firstUID")
        val routineSet2 = RoutineSet(null, 1, 2, 1, 2, 2, false, "secondUID")
        routineSetDao.upsert(routineSet1)
        routineSetDao.upsert(routineSet2)

        routineSetDao.updateRoutineSetsUserUID(newUID)
        val allRoutineSets = routineSetDao.getAllEntities().getOrAwaitValue()
        // Check if all routine sets have the userUID equal to newUID
        val allRoutineSetsHaveNewUID = allRoutineSets.all { it.userUID == newUID }
        assertTrue(allRoutineSetsHaveNewUID)
    }
}
