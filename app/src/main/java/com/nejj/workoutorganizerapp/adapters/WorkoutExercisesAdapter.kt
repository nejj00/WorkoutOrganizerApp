package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.WorkoutExercise

class WorkoutExercisesAdapter : SimpleItemPreviewAdapter<WorkoutExercise>() {
    override val differCallback = object: DiffUtil.ItemCallback<WorkoutExercise>() {
        override fun areItemsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
            return oldItem.exercise == newItem.exercise
        }

        override fun areContentsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
            return oldItem == newItem
        }
    }

    override val differ = AsyncListDiffer<WorkoutExercise>(this, differCallback)

    override fun getItemText(position: Int): String? {
        return differ.currentList[position].exercise.name
    }
}