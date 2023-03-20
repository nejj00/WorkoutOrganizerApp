package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.WorkoutRoutine

class RoutineAdapter : SimpleItemPreviewAdapter<WorkoutRoutine>() {

    override val differCallback = object: DiffUtil.ItemCallback<WorkoutRoutine>() {
        override fun areItemsTheSame(oldItem: WorkoutRoutine, newItem: WorkoutRoutine): Boolean {
            return oldItem.name == newItem.name
            TODO("don't compare by name but by id when you implement the database")
        }

        override fun areContentsTheSame(oldItem: WorkoutRoutine, newItem: WorkoutRoutine): Boolean {
            return oldItem == newItem
        }
    }

    override val differ = AsyncListDiffer(this, differCallback)
    override fun getItemText(position: Int): String {
        return differ.currentList[position].name
    }

}