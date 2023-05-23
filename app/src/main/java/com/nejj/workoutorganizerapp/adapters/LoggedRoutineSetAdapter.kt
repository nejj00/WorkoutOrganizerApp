package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet

class LoggedRoutineSetAdapter(hideOptions: Boolean = true) : SimpleItemPreviewAdapter<LoggedRoutineSet>(hideOptions) {
    override val differCallback = object: DiffUtil.ItemCallback<LoggedRoutineSet> () {
        override fun areItemsTheSame(oldItem: LoggedRoutineSet, newItem: LoggedRoutineSet): Boolean {
            return oldItem.loggedRoutineSetId == newItem.loggedRoutineSetId
        }

        override fun areContentsTheSame(oldItem: LoggedRoutineSet, newItem: LoggedRoutineSet): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String? {
        return "${differ.currentList[position].setsCount}x ${differ.currentList[position].exerciseName}"
    }

    override fun getItemTextSize(): Float {
        return 13f
    }

    override fun getPadding(): Int {
        return 3
    }
}