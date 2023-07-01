package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.FragmentRoutineSetEditBinding
import com.nejj.workoutorganizerapp.enums.FragmentContext
import com.nejj.workoutorganizerapp.ui.viewmodels.RoutineSetMainViewModel

class EditRoutineSetFragment : Fragment(R.layout.fragment_routine_set_edit){

    private lateinit var viewBinding: FragmentRoutineSetEditBinding
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()

    private val args: EditRoutineSetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRoutineSetEditBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBarMenuItems()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveRoutineSet()
            findNavController().navigateUp()
        }

        val routineSet = args.routineSet

        viewBinding.tiSets.editText?.setText(routineSet.setsCount.toString())
        viewBinding.tiWarmupSets.editText?.setText(routineSet.warmupSetsCount.toString())

        viewBinding.btnReplace.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("entityId", routineSet.routineSetId)
                putSerializable("fragmentContext", FragmentContext.EDIT_ROUTINE_SET_CONTEXT)
            }
            findNavController().navigate(
                R.id.action_editRoutineSetFragment_to_addRoutineExerciseCategoriesFragment,
                bundle
            )

//            val addExerciseDialogFragment = AddExerciseDialogFragment()
//            val fragmentManager = childFragmentManager
//
//            addExerciseDialogFragment.arguments = Bundle().apply {
//                putSerializable("addDialogContext", AddExerciseDialogContext.EDIT_ROUTINE_SET)
//                putSerializable("routineSet", routineSet)
//            }
//
//            addExerciseDialogFragment.show(fragmentManager, "addExerciseDialogFragment")
        }

        viewBinding.btnDelete.setOnClickListener(
            View.OnClickListener {
                routineSetViewModel.deleteEntity(routineSet)
                findNavController().navigateUp()
            }
        )
    }

    private fun setupAppBarMenuItems() {
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        saveRoutineSet()
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                activity!!.addMenuProvider(menuProvider)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                activity!!.removeMenuProvider(menuProvider)
            }
        })
    }

    private fun saveRoutineSet() {
        val setsCount = viewBinding.tiSets.editText?.text.toString().toInt()
        val warmupSetsCount = viewBinding.tiWarmupSets.editText?.text.toString().toInt()

        val routineSet = args.routineSet
        routineSet.setsCount = setsCount
        routineSet.warmupSetsCount = warmupSetsCount

        routineSetViewModel.insertEntity(routineSet)
    }
}