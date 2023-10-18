package com.sg.retrofitexample.data.repository


import com.sg.retrofitexample.data.model.ProductsResponse
import com.sg.retrofitexample.data.network.datasource.ProductDataSource
import com.sg.retrofitexample.utils.ResultWrapper
import com.sg.retrofitexample.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface ProductRepository {
    suspend fun getProducts(): Flow<ResultWrapper<ProductsResponse>>
}

class ProductRepositoryImpl(private val productDataSource: ProductDataSource) : ProductRepository {
    override suspend fun getProducts(): Flow<ResultWrapper<ProductsResponse>> {
        return proceedFlow {
            productDataSource.getProducts()
        }.map {
            if (it.payload?.products?.isEmpty() == true)
                ResultWrapper.Empty(it.payload)
            else
                it
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

}