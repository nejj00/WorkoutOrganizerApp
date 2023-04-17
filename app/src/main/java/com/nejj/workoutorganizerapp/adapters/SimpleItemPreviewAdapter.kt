package com.nejj.workoutorganizerapp.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nejj.workoutorganizerapp.databinding.ItemRoutinePreviewBinding

abstract class SimpleItemPreviewAdapter<T> : RecyclerView.Adapter<SimpleItemPreviewAdapter<T>.SimpleItemPreviewViewHolder>() {

    inner class SimpleItemPreviewViewHolder(val viewBinding: ItemRoutinePreviewBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    abstract val differCallback: DiffUtil.ItemCallback<T>
    abstract val differ: AsyncListDiffer<T>

    abstract fun getItemText(position: Int): String?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemPreviewViewHolder {
        return SimpleItemPreviewViewHolder(
            ItemRoutinePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimpleItemPreviewViewHolder, position: Int) {
        val itemText = getItemText(position)
        val item = differ.currentList[position]
        holder.viewBinding.apply {
            tvRoutineName.text = itemText
            tvRoutineName.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
            textViewOptions.setOnClickListener { view ->
                onOptionsClickListener?.let { it(item, view) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((T) -> Unit)? = null

    fun setOnItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

    private var onOptionsClickListener: ((T, View) -> Unit)? = null

    fun setOnOptionsClickListener(listener: (T, View) -> Unit) {
        onOptionsClickListener = listener
    }
}