package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemLoggedWorkoutBinding
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets

class LoggedWorkoutRoutinesAdapter : RecyclerView.Adapter<LoggedWorkoutRoutinesAdapter.LoggedWorkoutRoutinesViewHolder>() {

    private lateinit var loggedRoutineSetAdapter: LoggedRoutineSetAdapter

    inner class LoggedWorkoutRoutinesViewHolder(val viewBinding: ItemLoggedWorkoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private val differCallback = object: DiffUtil.ItemCallback<LoggedWorkoutRoutineWithLoggedRoutineSets>() {
        override fun areItemsTheSame(oldItem: LoggedWorkoutRoutineWithLoggedRoutineSets, newItem: LoggedWorkoutRoutineWithLoggedRoutineSets): Boolean {
            return oldItem.loggedWorkoutRoutine.loggedRoutineId == newItem.loggedWorkoutRoutine.loggedRoutineId
        }

        override fun areContentsTheSame(oldItem: LoggedWorkoutRoutineWithLoggedRoutineSets, newItem: LoggedWorkoutRoutineWithLoggedRoutineSets): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoggedWorkoutRoutinesViewHolder {
        return LoggedWorkoutRoutinesViewHolder(
            ItemLoggedWorkoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoggedWorkoutRoutinesViewHolder, position: Int) {
        val loggedWorkoutRoutine = differ.currentList[position]
        holder.viewBinding.apply {
            tvWorkoutTitle.text = loggedWorkoutRoutine.loggedWorkoutRoutine.name
            tvDayOfWeek.text = loggedWorkoutRoutine.loggedWorkoutRoutine.date.dayOfWeek.toString().take(3)
            tvDayOfMonth.text = loggedWorkoutRoutine.loggedWorkoutRoutine.date.dayOfMonth.toString()
            tvMonth.text = loggedWorkoutRoutine.loggedWorkoutRoutine.date.month.toString().take(3)

            cardLoggedWorkout.setOnClickListener {
                onItemClickListener?.let { it(loggedWorkoutRoutine) }
            }

            if(onItemLongClickListener != null) {
                cardLoggedWorkout.setOnLongClickListener {
                    (onItemLongClickListener!!)(loggedWorkoutRoutine, it)
                }
            }

            rvLogCardExercises.setOnClickListener {
                onItemClickListener?.let { it(loggedWorkoutRoutine) }
            }

            loggedRoutineSetAdapter = LoggedRoutineSetAdapter()
            rvLogCardExercises.apply {
                adapter = loggedRoutineSetAdapter
                layoutManager = LinearLayoutManager(context)
            }

            loggedWorkoutRoutine.loggedRoutineSets.let {loggedRoutineSetList ->
                loggedRoutineSetAdapter.differ.submitList(loggedRoutineSetList.sortedBy { it.setsOrder })
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((LoggedWorkoutRoutineWithLoggedRoutineSets) -> Unit)? = null

    fun setOnItemClickListener(listener: (LoggedWorkoutRoutineWithLoggedRoutineSets) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((LoggedWorkoutRoutineWithLoggedRoutineSets, View) -> Boolean)? = null

    fun setOnItemLongClickListener(listener: (LoggedWorkoutRoutineWithLoggedRoutineSets, view: View) -> Boolean) {
        onItemLongClickListener = listener
    }

}