package com.compose.trykeeper.presentacion.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.compose.trykeeper.domain.HomeRepository
import com.compose.trykeeper.model.Products
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var _products by mutableStateOf<List<Products>>(listOf())

    val products: List<Products>
        get() = _products

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            when (val products  = homeRepository.getProducts()) {
                is HomeResults.ProductsSearch -> {
                    _products = products.products
                }
                is HomeResults.Error -> {
                    // Handle error
                }
            }
        }
    }
}