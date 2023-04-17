package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class CategoriesAdapter : SimpleItemPreviewAdapter<ExerciseCategory>() {

    override val differCallback = object: DiffUtil.ItemCallback<ExerciseCategory>() {
        override fun areItemsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String {
        return differ.currentList[position].name
    }
}