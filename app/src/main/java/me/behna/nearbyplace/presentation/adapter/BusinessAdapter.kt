package me.behna.nearbyplace.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.behna.nearbyplace.data.model.BusinessModel
import me.behna.nearbyplace.databinding.ItemBusinessBinding


class BusinessAdapter :
    PagingDataAdapter<BusinessModel, BusinessAdapter.BusinessViewHolder>(BusinessDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(
            ItemBusinessBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = getItem(position)
        if (business != null) {
            holder.bind(business)
        }
    }

    class BusinessViewHolder(private val binding: ItemBusinessBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BusinessModel) {
            binding.apply {
                business = item
                Glide.with(img).load(item.imageUrl).into(img)
                executePendingBindings()
            }
        }
    }
}

private class BusinessDiffCallback : DiffUtil.ItemCallback<BusinessModel>() {
    override fun areItemsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BusinessModel, newItem: BusinessModel): Boolean {
        return oldItem == newItem
    }
}
