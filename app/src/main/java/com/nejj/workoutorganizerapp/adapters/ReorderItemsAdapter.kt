package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemRoutinePreviewBinding
import com.nejj.workoutorganizerapp.models.ReorderableItem
import com.nejj.workoutorganizerapp.models.RoutineSet
import java.util.Objects

class ReorderItemsAdapter(private var reorderItems: MutableList<ReorderableItem>) : RecyclerView.Adapter<ReorderItemsAdapter.ReorderItemViewHolder>() {

    inner class ReorderItemViewHolder(val viewBinding: ItemRoutinePreviewBinding) :
        RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReorderItemViewHolder {
        return ReorderItemViewHolder(
            ItemRoutinePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return reorderItems.size
    }

    override fun onBindViewHolder(holder: ReorderItemViewHolder, position: Int) {
        holder.viewBinding.apply {
            tvRoutineName.text = reorderItems[position].getItemText()
        }
    }

}