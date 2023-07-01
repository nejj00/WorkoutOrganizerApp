package com.nejj.workoutorganizerapp.ui.fragment.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.PersonalRecordAdapter
import com.nejj.workoutorganizerapp.databinding.FragmentDoubleListViewBinding
import com.nejj.workoutorganizerapp.models.PersonalRecord
import com.nejj.workoutorganizerapp.ui.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch

class PersonalRecordsStatisticsFragment : Fragment(R.layout.fragment_double_list_view) {

    private lateinit var viewBinding: FragmentDoubleListViewBinding
    private lateinit var topItemsAdapter: PersonalRecordAdapter
    private lateinit var bottomItemsAdapter: PersonalRecordAdapter
    private val statisticsViewModel: StatisticsViewModel by activityViewModels()

    private val args: PersonalRecordsStatisticsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDoubleListViewBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val personalRecords = statisticsViewModel.getPersonalRecordsForExercise(args.exercise.exerciseId!!)

            setupBottomRecyclerView(personalRecords)
            bottomItemsAdapter.setOnItemClickListener(itemClickedListener)
        }

    }

    val itemClickedListener = fun(personalRecord: PersonalRecord) {
        val bundle = Bundle().apply {
            putSerializable("personalRecord", personalRecord)
        }
        findNavController().navigate(
            R.id.action_personalRecordsStatisticsFragment_to_personalRecordChartFragment,
            bundle
        )
    }

    private fun setupTopRecyclerView(personalRecords: MutableList<PersonalRecord>) {
        topItemsAdapter = PersonalRecordAdapter(personalRecords)
        viewBinding.rvTopItems.apply {
            adapter = topItemsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupBottomRecyclerView(personalRecords: MutableList<PersonalRecord>) {
        bottomItemsAdapter = PersonalRecordAdapter(personalRecords)
        viewBinding.rvBottomItems.apply {
            adapter = bottomItemsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}