package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemRoutineSetPreviewBinding
import com.nejj.workoutorganizerapp.models.WorkoutExercise
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise

class RoutineSetsAdapter : RecyclerView.Adapter<RoutineSetsAdapter.RoutineSetsViewHolder>() {

    inner class RoutineSetsViewHolder(val viewBinding: ItemRoutineSetPreviewBinding) :
            RecyclerView.ViewHolder(viewBinding.root)

    private val differCallback = object: DiffUtil.ItemCallback<RoutineSetsWithExercise>() {
        override fun areItemsTheSame(oldItem: RoutineSetsWithExercise, newItem: RoutineSetsWithExercise): Boolean {
            return oldItem.exercise == newItem.exercise
        }

        override fun areContentsTheSame(oldItem: RoutineSetsWithExercise, newItem: RoutineSetsWithExercise): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<RoutineSetsWithExercise>(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineSetsViewHolder {
        return RoutineSetsViewHolder(
                ItemRoutineSetPreviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }
    override fun onBindViewHolder(holder: RoutineSetsViewHolder, position: Int) {
        val routineSetWithExercise = differ.currentList[position]
        holder.viewBinding.apply {
            tvExerciseName.text = routineSetWithExercise.exercise.name
            tvSets.text = "${routineSetWithExercise.routineSet.setsCount.toString()} Sets"
            tvExerciseName.setOnClickListener {
                onItemClickListener?.let { it(routineSetWithExercise) }
            }
            textViewOptions.setOnClickListener { view ->
                onOptionsClickListener?.let { it(routineSetWithExercise, view) }
            }
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((RoutineSetsWithExercise) -> Unit)? = null

    fun setOnItemClickListener(listener: (RoutineSetsWithExercise) -> Unit) {
        onItemClickListener = listener
    }

    private var onOptionsClickListener: ((RoutineSetsWithExercise, View) -> Unit)? = null

    fun setOnOptionsClickListener(listener: (RoutineSetsWithExercise, View) -> Unit) {
        onOptionsClickListener = listener
    }

}