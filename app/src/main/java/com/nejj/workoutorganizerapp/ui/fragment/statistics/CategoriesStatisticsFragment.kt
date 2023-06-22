package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.ExercisesAdapter
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.adapters.StatisticsOptionsAdapter
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.ui.fragment.SimpleItemsDoubleListViewFragment
import com.nejj.workoutorganizerapp.ui.viewmodels.ExercisesMainViewModel

class CategoriesStatisticsFragment : SimpleItemsDoubleListViewFragment<StatisticsType, Exercise>() {

    private val exercisesViewModel: ExercisesMainViewModel by activityViewModels()

    private val args: CategoriesStatisticsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseCategory = args.exerciseCategory

        exercisesViewModel.getExercisesByCategoryIdLive(exerciseCategory.name).observe(viewLifecycleOwner) { exercises ->
            //allCategories.addAll(categories)
            simpleItemPreviewBottomRVAdapter.differ.submitList(exercises)
        }

        val overallStatistics = StatisticsType.values()

        simpleItemPreviewTopRVAdapter.differ.submitList(overallStatistics.toList())
    }

    override val topTVItemClickedListener = fun(statistic: StatisticsType) {

    }
    override val bottomTVItemClickedListener = fun(exercise: Exercise) {
        val bundle = Bundle().apply {
            putSerializable("exercise", exercise)
        }
        findNavController().navigate(
            R.id.action_categoriesStatisticsFragment_to_exercisesStatisticsFragment,
            bundle
        )
    }

    override fun getTopRVAdapter(): SimpleItemPreviewAdapter<StatisticsType> {
        return StatisticsOptionsAdapter(true)
    }

    override fun getBottomRVAdapter(): SimpleItemPreviewAdapter<Exercise> {
        return ExercisesAdapter(true)
    }


}