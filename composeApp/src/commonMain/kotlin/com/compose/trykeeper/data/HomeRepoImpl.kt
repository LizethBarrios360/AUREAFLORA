package com.compose.trykeeper.data

import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.domain.HomeRepository
import com.compose.trykeeper.model.Products
import com.compose.trykeeper.presentacion.home.HomeResults

class HomeRepoImpl(private val aureaFloraDatabase: AureaFloraDatabase) : HomeRepository {

    private val queries = aureaFloraDatabase.aureaDBQueries

    override fun getProducts(): HomeResults {
        return if (queries.products().executeAsList().isEmpty()) {
            HomeResults.Error("No products found")
        } else {
            val products = queries.products().executeAsList().map { product ->
                Products(
                    idProduct = product.id_producto.toInt(),
                    name = product.nombre,
                    value = product.valor,
                    idCategory = product.id_categoria!!.toInt()
                )
            }
            HomeResults.ProductsSearch(products)
        }
    }
}