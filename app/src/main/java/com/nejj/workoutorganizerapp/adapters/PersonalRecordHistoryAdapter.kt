package com.nejj.workoutorganizerapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.nejj.workoutorganizerapp.models.PersonalRecord
import com.nejj.workoutorganizerapp.util.StringFormatter
import java.time.LocalDate

class PersonalRecordHistoryAdapter : SimpleItemPreviewAdapter<RecordHistoryItem>() {

    override fun onBindViewHolder(holder: SimpleItemPreviewViewHolder, position: Int) {
        val recordHistoryItem = differ.currentList[position]
        holder.viewBinding.apply {
            tvRoutineName.text = recordHistoryItem.date
            textViewOptions.text = StringFormatter.formatDouble(recordHistoryItem.record)
        }
    }

    override val differCallback = object: DiffUtil.ItemCallback<RecordHistoryItem>() {
        override fun areItemsTheSame(oldItem: RecordHistoryItem, newItem: RecordHistoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RecordHistoryItem, newItem: RecordHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
    override val differ = AsyncListDiffer(this, differCallback)

    override fun getItemText(position: Int): String {
        return ""
    }
}

data class RecordHistoryItem(
    val date: String,
    val record: Double
)