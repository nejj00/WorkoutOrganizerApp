package com.nejj.workoutorganizerapp.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.ItemExerciseSetBinding
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet

class LoggedExerciseSetAdapter : RecyclerView.Adapter<LoggedExerciseSetAdapter.LoggedExerciseSetViewHolder>() {

    inner class LoggedExerciseSetViewHolder(val viewBinding: ItemExerciseSetBinding) :
            RecyclerView.ViewHolder(viewBinding.root)


    private val differCallback = object: DiffUtil.ItemCallback<LoggedExerciseSet>() {
        override fun areItemsTheSame(oldItem: LoggedExerciseSet, newItem: LoggedExerciseSet): Boolean {
            return oldItem.loggedExerciseSetId == newItem.loggedExerciseSetId
        }

        override fun areContentsTheSame(oldItem: LoggedExerciseSet, newItem: LoggedExerciseSet): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private val updatedExerciseSets = mutableListOf<LoggedExerciseSet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoggedExerciseSetViewHolder {
        return LoggedExerciseSetViewHolder(
                ItemExerciseSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: LoggedExerciseSetViewHolder, position: Int) {
        val loggedExerciseSet = differ.currentList[position]
        holder.viewBinding.apply {

            val textWatcher = object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    loggedExerciseSet.apply {
                        weight = tiWeight.editText?.text.toString().toDoubleOrNull() ?: weight
                        reps = tiReps.editText?.text.toString().toIntOrNull() ?: reps
                        notes = tiNotes.editText?.text.toString()
                    }
                }
            }

            tiWeight.editText?.doOnTextChanged {
                text, start, count, after ->
                onWeightTextChangedListener?.let { it(tiWeight, tvError,loggedExerciseSet, text, start, count, after) }
            }

            tiWeight.editText?.addTextChangedListener(textWatcher)
            tiReps.editText?.addTextChangedListener(textWatcher)
            tiNotes.editText?.addTextChangedListener(textWatcher)

            if (loggedExerciseSet.weight > 0.0) {
                tiWeight.editText?.setText(loggedExerciseSet.weight.toString())
            } else {
                tiWeight.editText?.hint = loggedExerciseSet.weight.toString()
            }
            if (loggedExerciseSet.reps > 0) {
                tiReps.editText?.setText(loggedExerciseSet.reps.toString())
            } else {
                tiReps.editText?.hint = loggedExerciseSet.reps.toString()
            }
            tiNotes.editText?.setText(loggedExerciseSet.notes)

            tvExerciseSetOptions.setOnClickListener { view ->
                onOptionsClickListener?.let { it(loggedExerciseSet, view) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getExerciseSets(): List<LoggedExerciseSet> {
        return updatedExerciseSets
    }

    private var onWeightTextChangedListener: ((TextInputLayout, TextView, LoggedExerciseSet, CharSequence?, Int, Int, Int) -> Unit)? = null

    fun setOnWeightTextChangedListener(listener: (TextInputLayout, TextView, LoggedExerciseSet, CharSequence?, Int, Int, Int) -> Unit) {
        onWeightTextChangedListener = listener
    }

    private var onOptionsClickListener: ((LoggedExerciseSet, View) -> Unit)? = null

    fun setOnOptionsClickListener(listener: (LoggedExerciseSet, View) -> Unit) {
        onOptionsClickListener = listener
    }
}