package com.compose.trykeeper.presentacion.home

import com.compose.trykeeper.model.Products

sealed class HomeResults {
    data class ProductsSearch(val products: List<Products>) : HomeResults()
    data class Error(val message: String) : HomeResults()
}