package com.github.lzaytseva.vktest.data.mapper

import com.github.lzaytseva.vktest.data.network.dto.ProductDto
import com.github.lzaytseva.vktest.domain.model.Product
import javax.inject.Inject

class ProductMapper @Inject constructor() {
    fun mapDtoToDomain(dto: ProductDto): Product {
        return Product(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            thumbnailUrl = dto.thumbnailUrl,
            brand = dto.brand,
            price = dto.price,
            category = dto.category,
            discountPercentage = dto.discountPercentage,
            images = dto.images,
            stock = dto.stock,
            rating = dto.rating
        )
    }
}