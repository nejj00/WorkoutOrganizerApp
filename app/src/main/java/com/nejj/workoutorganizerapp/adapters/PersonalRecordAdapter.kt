package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemPersonalRecordPreviwBinding
import com.nejj.workoutorganizerapp.models.PersonalRecord
import com.nejj.workoutorganizerapp.util.StringFormatter

class PersonalRecordAdapter(private var personalRecords: MutableList<PersonalRecord>) : RecyclerView.Adapter<PersonalRecordAdapter.PersonalRecordViewHolder>() {

    inner class PersonalRecordViewHolder(val viewBinding: ItemPersonalRecordPreviwBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordViewHolder {
        return PersonalRecordViewHolder(
            ItemPersonalRecordPreviwBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return personalRecords.size
    }

    override fun onBindViewHolder(holder: PersonalRecordViewHolder, position: Int) {
        val personalRecord = personalRecords[position]
        holder.viewBinding.apply {
            tvRecordType.text = personalRecord.personalRecordStatisticsType.toString()
            tvRecord.text = StringFormatter.formatDouble(personalRecord.record)
            tvRecordDate.text = StringFormatter.formatLocalDate(personalRecord.date)

            clPersonalRecordItem.setOnClickListener {
                onItemClickListener?.let { it(personalRecord) }
            }
        }
    }

    private var onItemClickListener: ((PersonalRecord) -> Unit)? = null

    fun setOnItemClickListener(listener: (PersonalRecord) -> Unit) {
        onItemClickListener = listener
    }
}