package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.Exercise

class ExercisesAdapter(hideOptions: Boolean = false) : SimpleItemPreviewAdapter<Exercise>(hideOptions) {

    override val differCallback = object: DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.name == newItem.name && oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }

    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String? {
        return differ.currentList[position].name
    }

    override fun getPadding() = 20
    override fun getPaddingLeft() = 20
}