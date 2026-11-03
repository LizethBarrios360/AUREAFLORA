package com.compose.trykeeper.domain

import com.compose.trykeeper.presentacion.home.HomeResults

interface HomeRepository {
    fun getProducts(): HomeResults
}