package com.example.citiesdistanceusinghilt.feature.distance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.databinding.ItemDistanceBinding

class DistanceAdapter() :
    ListAdapter<Distance, DistanceAdapter.MyViewHolder>(MyDiffUtil) {
    var onLongClick: ((Distance) -> Unit)? = null

    companion object MyDiffUtil : DiffUtil.ItemCallback<Distance>() {
        override fun areItemsTheSame(oldItem: Distance, newItem: Distance): Boolean =
            oldItem == newItem


        override fun areContentsTheSame(oldItem: Distance, newItem: Distance): Boolean =
            oldItem.id == newItem.id

    }

    inner class MyViewHolder(private val binding: ItemDistanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(distance: Distance?) {
            binding.textViewItemDistanceBeginning.text = distance?.beginning
            binding.textViewItemDistanceDestination.text = distance?.destination
            binding.textViewItemDistanceDistance.text = "${distance?.distance} کیلومتر"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemDistanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val distance = getItem(position)
        holder.bind(distance)

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(distance)
            return@setOnLongClickListener false
        }
    }
}