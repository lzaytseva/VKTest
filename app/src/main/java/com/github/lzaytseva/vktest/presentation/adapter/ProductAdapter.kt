package com.github.lzaytseva.vktest.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.lzaytseva.vktest.R
import com.github.lzaytseva.vktest.databinding.ProductItemBinding
import com.github.lzaytseva.vktest.domain.model.Product

class ProductAdapter(private val onProductClickListener: (Int) -> Unit) :
    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.itemView.setOnClickListener { onProductClickListener(currentList[position].id) }

    }

    class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Product) {
            with(binding) {
                with(model) {
                    tvDescription.text = description
                    tvTitle.text = name
                    Glide.with(itemView)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.product_image_placeholder)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(
                                itemView.resources.getDimensionPixelSize(R.dimen.thumbnail_image_corner_radius)
                            ),
                        )
                        .into(ivProductThumbnail)
                }
            }
        }
    }
}

