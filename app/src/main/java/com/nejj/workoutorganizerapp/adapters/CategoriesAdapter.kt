package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemRoutinePreviewBinding
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class CategoriesAdapter : SimpleItemPreviewAdapter<ExerciseCategory>() {

    override val differCallback = object: DiffUtil.ItemCallback<ExerciseCategory>() {
        override fun areItemsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem.categoryName == newItem.categoryName
        }

        override fun areContentsTheSame(oldItem: ExerciseCategory, newItem: ExerciseCategory): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String {
        return differ.currentList[position].categoryName
    }
}