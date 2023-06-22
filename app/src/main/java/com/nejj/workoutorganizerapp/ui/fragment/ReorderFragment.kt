package com.nejj.workoutorganizerapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.adapters.ReorderItemsAdapter
import com.nejj.workoutorganizerapp.adapters.SimpleItemPreviewAdapter
import com.nejj.workoutorganizerapp.databinding.ItemsListViewFragmentBinding
import com.nejj.workoutorganizerapp.enums.FragmentContext
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.ReorderableItem
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.ui.dialogs.CountdownTimerBottomSheet
import com.nejj.workoutorganizerapp.ui.viewmodels.LoggedRoutineSetViewModel
import com.nejj.workoutorganizerapp.ui.viewmodels.RoutineSetMainViewModel
import kotlinx.coroutines.launch
import java.util.*

class ReorderFragment : Fragment(R.layout.items_list_view_fragment) {

    private lateinit var viewBinding: ItemsListViewFragmentBinding
    private lateinit var reorderItemsAdapter: ReorderItemsAdapter
    private val args: ReorderFragmentArgs by navArgs()
    private val routineSetViewModel: RoutineSetMainViewModel by activityViewModels()
    private val loggedRoutineSetViewModel: LoggedRoutineSetViewModel by activityViewModels()

    private var reorderItems = mutableListOf<ReorderableItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ItemsListViewFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAppBarMenuItems()
        viewBinding.fabAddItem.visibility = View.GONE

        val routineId = args.routineId
        val fragmentContext = args.fragmentContext as FragmentContext

        if(fragmentContext == FragmentContext.ROUTINE_CONTEXT) {
            lifecycleScope.launch {
                val routineSets = routineSetViewModel.getRoutineSetsByRoutineId(routineId)
                reorderItems.addAll(routineSets.toList().sortedBy { it.setsOrder })
                setUpRecyclerView()
            }
        }
        else if(fragmentContext == FragmentContext.WORKOUT_CONTEXT) {
            lifecycleScope.launch {
                val loggedRoutineSets = loggedRoutineSetViewModel.getLoggedRoutineSetsByLoggedRoutineId(routineId)
                reorderItems.addAll(loggedRoutineSets.toList().sortedBy { it.setsOrder })
                setUpRecyclerView()
            }
        }

    }

    private fun setUpAppBarMenuItems() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        saveRoutineSetsOrder()
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveRoutineSetsOrder() {
        if(args.fragmentContext == FragmentContext.ROUTINE_CONTEXT) {
            for(reorderItem in reorderItems) {
                val routineSet = reorderItem as RoutineSet
                routineSet.setsOrder = reorderItems.indexOf(reorderItem)
                routineSetViewModel.insertEntity(routineSet)
            }
        }
        else if(args.fragmentContext == FragmentContext.WORKOUT_CONTEXT) {
            for(reorderItem in reorderItems) {
                val loggedRoutineSet = reorderItem as LoggedRoutineSet
                loggedRoutineSet.setsOrder = reorderItems.indexOf(reorderItem)
                loggedRoutineSetViewModel.insertEntity(loggedRoutineSet)
            }
        }

    }

    private fun setUpRecyclerView() {
        reorderItemsAdapter = ReorderItemsAdapter(reorderItems)
        viewBinding.rvItems.apply {
            adapter = reorderItemsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(viewBinding.rvItems)
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPos = viewHolder.adapterPosition
            val endPos = target.adapterPosition

            Collections.swap(reorderItems, startPos, endPos)
            reorderItemsAdapter.notifyItemMoved(startPos, endPos)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    }
}