package com.sg.retrofitexample.presentation.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.sg.retrofitexample.R
import com.sg.retrofitexample.data.network.datasource.ProductDataSourceImpl
import com.sg.retrofitexample.data.network.service.ProductService
import com.sg.retrofitexample.data.repository.ProductRepository
import com.sg.retrofitexample.data.repository.ProductRepositoryImpl
import com.sg.retrofitexample.databinding.ActivityProductBinding
import com.sg.retrofitexample.utils.GenericViewModelFactory
import com.sg.retrofitexample.utils.proceedWhen

class ProductActivity : AppCompatActivity() {

    private val binding : ActivityProductBinding by lazy {
        ActivityProductBinding.inflate(layoutInflater)
    }

    private val productAdapter = ProductListAdapter()

    private val viewModel : ProductViewModel by viewModels {
        val service = ProductService.invoke()
        val dataSource = ProductDataSourceImpl(service)
        val repo : ProductRepository = ProductRepositoryImpl(dataSource)
        GenericViewModelFactory.create(ProductViewModel(repo))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRv()
        observeData()
        getData()
    }

    fun setupRv(){
        binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = productAdapter
            productAdapter.refreshList()
        }
    }
    private fun observeData() {
        viewModel.listProduct.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvProduct.isVisible = true
                    binding.commonLayoutState.root.isVisible = false
                    binding.commonLayoutState.pbLoading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.apply {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        adapter = productAdapter
                    }
                    it.payload?.let {
                        productAdapter.setData(it.products)
                    }
                },
                doOnLoading = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.pbLoading.isVisible = true
                    binding.commonLayoutState.tvError.isVisible = false
                    binding.rvProduct.isVisible = false
                },
                doOnError = { err ->
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.pbLoading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvProduct.isVisible = false
                },
                doOnEmpty = {
                    binding.commonLayoutState.root.isVisible = true
                    binding.commonLayoutState.pbLoading.isVisible = false
                    binding.commonLayoutState.tvError.isVisible = true
                    binding.commonLayoutState.tvError.text = getString(R.string.no_value)
                    binding.rvProduct.isVisible = false
                }
            )
        }
    }

    private fun getData() {
        viewModel.getProductList()
    }
}