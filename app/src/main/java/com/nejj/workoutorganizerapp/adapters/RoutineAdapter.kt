package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemRoutinePreviewBinding
import com.nejj.workoutorganizerapp.models.WorkoutRoutine

class RoutineAdapter : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(val viewBinding: ItemRoutinePreviewBinding): RecyclerView.ViewHolder(viewBinding.root)

    private val differCallback = object: DiffUtil.ItemCallback<WorkoutRoutine>() {
        override fun areItemsTheSame(oldItem: WorkoutRoutine, newItem: WorkoutRoutine): Boolean {
            return oldItem.name == newItem.name
            TODO("don't compare by name but by id when you implement the database")
        }

        override fun areContentsTheSame(oldItem: WorkoutRoutine, newItem: WorkoutRoutine): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        return RoutineViewHolder(
            ItemRoutinePreviewBinding.inflate(

                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val workoutRoutine = differ.currentList[position]
        holder.viewBinding.apply {
            tvRoutineName.text = workoutRoutine.name
            root.setOnClickListener {
                onItemClickListener?.let { it(workoutRoutine) }
            }
        }
    }

    private var onItemClickListener: ((WorkoutRoutine) -> Unit)? = null

    fun setOnItemClickListener(listener: (WorkoutRoutine) -> Unit) {
        onItemClickListener = listener
    }
}