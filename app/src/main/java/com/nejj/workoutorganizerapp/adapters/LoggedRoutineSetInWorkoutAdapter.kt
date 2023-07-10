package com.nejj.workoutorganizerapp.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.nejj.workoutorganizerapp.databinding.ItemWorkoutRoutineSetBinding
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet

class LoggedRoutineSetInWorkoutAdapter : RecyclerView.Adapter<LoggedRoutineSetInWorkoutAdapter.LoggedRoutineSetInWorkoutViewHolder>() {

    private lateinit var loggedExerciseSetAdapter: LoggedExerciseSetAdapter

    inner class LoggedRoutineSetInWorkoutViewHolder(val viewBinding: ItemWorkoutRoutineSetBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private val differCallback = object: DiffUtil.ItemCallback<LoggedRoutineSetWithLoggedExerciseSet>() {
        override fun areItemsTheSame(oldItem: LoggedRoutineSetWithLoggedExerciseSet, newItem: LoggedRoutineSetWithLoggedExerciseSet): Boolean {
            return oldItem.loggedRoutineSet.loggedRoutineSetId == newItem.loggedRoutineSet.loggedRoutineSetId
        }

        override fun areContentsTheSame(oldItem: LoggedRoutineSetWithLoggedExerciseSet, newItem: LoggedRoutineSetWithLoggedExerciseSet): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoggedRoutineSetInWorkoutViewHolder {
        return LoggedRoutineSetInWorkoutViewHolder(
            ItemWorkoutRoutineSetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoggedRoutineSetInWorkoutViewHolder, position: Int) {
        val routineSetWithExerciseSet = differ.currentList[position]
        holder.viewBinding.apply {
            tvExerciseName.text = routineSetWithExerciseSet.loggedRoutineSet.exerciseName
            tvLoggedRoutineSetOptions.setOnClickListener { view ->
                onOptionsClickListener?.let { it(routineSetWithExerciseSet, view) }
            }

            loggedExerciseSetAdapter = LoggedExerciseSetAdapter()
            rvExerciseSets.apply {
                adapter = loggedExerciseSetAdapter
                layoutManager = LinearLayoutManager(context)
            }

            loggedExerciseSetAdapter.differ.submitList(routineSetWithExerciseSet.loggedExerciseSets)

            btnAddSet.setOnClickListener {
                onAddSetClickListener?.let { it(routineSetWithExerciseSet) }
            }

            onExerciseSetOptionsClickListener?.let {
                loggedExerciseSetAdapter.setOnOptionsClickListener(it)
            }

            onWeightTextChangedListener?.let {
                loggedExerciseSetAdapter.setOnWeightTextChangedListener(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getExerciseSets(): List<LoggedExerciseSet> {
        val exerciseSets = mutableListOf<LoggedExerciseSet>()
        differ.currentList.forEach { routineSetWithExerciseSet ->
            exerciseSets.addAll(routineSetWithExerciseSet.loggedExerciseSets)
        }

        return exerciseSets
    }

    private var onOptionsClickListener: ((LoggedRoutineSetWithLoggedExerciseSet, View) -> Unit)? = null

    fun setOnOptionsClickListener(listener: (LoggedRoutineSetWithLoggedExerciseSet, View) -> Unit) {
        onOptionsClickListener = listener
    }

    private var onAddSetClickListener: ((LoggedRoutineSetWithLoggedExerciseSet) -> Unit)? = null

    fun setOnAddSetClickListener(listener: (LoggedRoutineSetWithLoggedExerciseSet) -> Unit) {
        onAddSetClickListener = listener
    }

    private var onExerciseSetOptionsClickListener: ((LoggedExerciseSet, View) -> Unit)? = null

    fun setOnExerciseSetOptionsClickListener(listener: (LoggedExerciseSet, View) -> Unit) {
        onExerciseSetOptionsClickListener = listener
    }

    private var onWeightTextChangedListener: ((TextInputLayout, TextView, LoggedExerciseSet, CharSequence?, Int, Int, Int) -> Unit)? = null

    fun setOnWeightTextChangedListener(listener: (TextInputLayout, TextView, LoggedExerciseSet, CharSequence?, Int, Int, Int) -> Unit) {
        onWeightTextChangedListener = listener
    }
}