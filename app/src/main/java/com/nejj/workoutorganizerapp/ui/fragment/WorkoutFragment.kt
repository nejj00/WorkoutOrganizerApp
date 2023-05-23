package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.LoggedWorkoutRoutinesAdapter
import com.nejj.workoutorganizerapp.adapters.RoutineAdapter
import com.nejj.workoutorganizerapp.databinding.ActivityWorkoutBinding
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedWorkoutRoutineViewModel

class WorkoutFragment : Fragment(R.layout.activity_workout) {

    private lateinit var viewBinding: ActivityWorkoutBinding
    private val args: WorkoutFragmentArgs by navArgs()
    private val loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ActivityWorkoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loggedWorkoutRoutine = args.loggedWorkoutRoutine
        //viewBinding.tfWorkoutName.editText?.setText(loggedWorkoutRoutine.name)
        //viewBinding.tfWorkoutNotes.editText?.setText(loggedWorkoutRoutine.notes)


    }


}