import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.ui.viewmodels.ExercisesMainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ExercisesMainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var viewModel: ExercisesMainViewModel

    @Before
    fun setup() {
        workoutRepository = Mockito.mock(WorkoutRepository::class.java)
        viewModel = ExercisesMainViewModel(Application(), workoutRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun testInsertEntity() = runTest {
        val exercise = Exercise(1, 2, "Test", "Type", false, false, "user123")
        viewModel.insertEntity(exercise)
        Mockito.verify(workoutRepository).upsertExercise(exercise)
    }

    // Create similar tests for other ViewModel methods, like deleteEntity, getEntities, etc.
}
