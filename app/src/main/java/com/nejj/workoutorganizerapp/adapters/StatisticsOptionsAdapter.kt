package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType

class StatisticsOptionsAdapter(hideOptions: Boolean = false) : SimpleItemPreviewAdapter<StatisticsType>(hideOptions) {

    override val differCallback = object: DiffUtil.ItemCallback<StatisticsType>() {
        override fun areItemsTheSame(oldItem: StatisticsType, newItem: StatisticsType): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StatisticsType, newItem: StatisticsType): Boolean {
            return oldItem.equals(newItem)
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String {
        return differ.currentList[position].toString()
    }
    override fun getPadding() = 20

    override fun getPaddingLeft() = 20
}