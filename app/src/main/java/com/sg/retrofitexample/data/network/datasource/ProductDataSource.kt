package com.sg.retrofitexample.data.network.datasource

import com.sg.retrofitexample.data.model.ProductsResponse
import com.sg.retrofitexample.data.network.service.ProductService


interface ProductDataSource {
    suspend fun getProducts(): ProductsResponse
}

class ProductDataSourceImpl(private val service: ProductService) : ProductDataSource {
    override suspend fun getProducts(): ProductsResponse {
        return service.getProducts()
    }
}