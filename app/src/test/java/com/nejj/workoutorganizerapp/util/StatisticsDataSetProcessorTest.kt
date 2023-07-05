import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.util.StatisticsDataSetProcessor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.time.LocalDate

class StatisticsDataSetProcessorTest {
    private lateinit var processor: StatisticsDataSetProcessor
    private val mockLoggedExerciseSet: LoggedExerciseSet = mock(LoggedExerciseSet::class.java)

    @Before
    fun setup() {
        processor = StatisticsDataSetProcessor()

        // Setup mockLoggedExerciseSet
        `when`(mockLoggedExerciseSet.reps).thenReturn(5)
        `when`(mockLoggedExerciseSet.weight).thenReturn(10.0)
    }

    @Test
    fun getOverallDataSetMapByStatisticType_returnsExpectedResult() {
        // Setup test data
        val statisticsType = OverallStatisticsType.NUMBER_OF_WORKOUTS
        val dateToExerciseSetsMap = mapOf(
            Pair(LocalDate.of(2023, 1, 1), listOf(mockLoggedExerciseSet))
        )

        // Run the function under test
        val result = processor.getOverallDataSetMapByStatisticType(statisticsType, dateToExerciseSetsMap)

        // Assert the result is as expected
        assertEquals(1, result.size)
        assertEquals(1.0f, result[dateToExerciseSetsMap.keys.first().toString()])
    }

    @Test
    fun getOverallDataSetMapByStatisticType_returnsEmptyForEmptyMap() {
        // Setup test data
        val statisticsType = OverallStatisticsType.NUMBER_OF_WORKOUTS
        val dateToExerciseSetsMap = mapOf<LocalDate, List<LoggedExerciseSet>>()

        // Run the function under test
        val result = processor.getOverallDataSetMapByStatisticType(statisticsType, dateToExerciseSetsMap)

        // Assert the result is as expected
        assertEquals(0, result.size)
    }

    @Test
    fun getOverallDataSetMapByStatisticType_returnsExpectedResultForVolume() {
        // Setup test data
        val statisticsType = OverallStatisticsType.VOLUME
        val dateToExerciseSetsMap = mapOf(
            Pair(LocalDate.of(2023, 1, 1), listOf(mockLoggedExerciseSet))
        )

        // Run the function under test
        val result = processor.getOverallDataSetMapByStatisticType(statisticsType, dateToExerciseSetsMap)

        // Assert the result is as expected
        assertEquals(1, result.size)
        assertEquals(50.0f, result[dateToExerciseSetsMap.keys.first().toString()])
    }

    @Test
    fun getOverallDataSetMapByStatisticType_returnsExpectedResultForMaxWeight() {
        // Setup test data
        val statisticsType = PersonalRecordStatisticsType.MAX_WEIGHT
        val dateToExerciseSetsMap = mapOf(
            Pair(LocalDate.of(2023, 1, 1), listOf(mockLoggedExerciseSet))
        )

        // Run the function under test
        val result = processor.getOverallDataSetMapByStatisticType(statisticsType, dateToExerciseSetsMap)

        // Assert the result is as expected
        assertEquals(1, result.size)
        assertEquals(10.0f, result[dateToExerciseSetsMap.keys.first().toString()])
    }
}
