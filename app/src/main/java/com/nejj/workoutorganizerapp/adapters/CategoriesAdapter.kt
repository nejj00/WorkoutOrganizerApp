package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class CategoriesAdapter(hideOptions: Boolean = false) : SimpleItemPreviewAdapter<ExerciseCategory>(hideOptions) {

    override val differCallback = object: DiffUtil.ItemCallback<ExerciseCategory>() {
        override fun areItemsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String {
        return differ.currentList[position].name
    }

    override fun getPadding() = 20
    override fun getPaddingLeft() = 20
}