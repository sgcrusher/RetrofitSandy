package com.sg.retrofitexample.presentation.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sg.retrofitexample.data.model.ProductsResponse
import com.sg.retrofitexample.data.repository.ProductRepository
import com.sg.retrofitexample.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel (private val repo : ProductRepository) : ViewModel(){

    val listProduct = MutableLiveData<ResultWrapper<ProductsResponse>>()

fun getProductList(){
    viewModelScope.launch(Dispatchers.IO) {
        repo.getProducts().collect{
            listProduct.postValue(it)
        }
    }
}
}