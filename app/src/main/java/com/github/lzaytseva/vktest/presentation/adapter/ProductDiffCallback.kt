package com.github.lzaytseva.vktest.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.lzaytseva.vktest.domain.model.Product

object ProductDiffCallback: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}