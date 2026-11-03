package com.compose.trykeeper.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.trykeeper.AppTheme
import com.compose.trykeeper.getColorsTheme
import com.compose.trykeeper.model.Products
import moe.tlaster.precompose.PreComposeApp
import org.jetbrains.compose.resources.painterResource
import trykeeper.composeapp.generated.resources.Res
import trykeeper.composeapp.generated.resources.compose_multiplatform
import trykeeper.composeapp.generated.resources.ic_product1
import trykeeper.composeapp.generated.resources.ic_product2
import trykeeper.composeapp.generated.resources.ic_product3
import trykeeper.composeapp.generated.resources.ic_product4
import trykeeper.composeapp.generated.resources.ic_product5
import trykeeper.composeapp.generated.resources.ic_product6

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(listProduct: List<Products>, onClickProduct: (Products) -> Unit = {}) {
    val colors = getColorsTheme()
    val chipOptions = listOf("Todos", "Azul", "Ramos", "Mariposa", "Brazalete", "Rojo")
    var selectedChip by remember { mutableStateOf("Todos") }

    PreComposeApp {
        AppTheme {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                    Text("Productos", fontSize = 32.sp, color = colors.textColor)
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(chipOptions) { chip ->
                            FilterChip(
                                selected = selectedChip == chip,
                                onClick = { selectedChip = chip },
                                modifier = Modifier.padding(end = 8.dp)
                            ){
                                Text(
                                    text = chip,
                                    color = if (selectedChip == chip) colors.textColor else colors.textColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    val filteredProducts = if (selectedChip == "Todos") {
                        listProduct
                    } else {
                        listProduct.filter { it.idCategory.toString() == chipOptions.indexOf(selectedChip).toString() }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredProducts) {product ->
                            ProductCard(product, onClickProduct)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ProductCard(product: Products, onClickProduct: (Products) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = { onClickProduct(product) })
    ) {
        Image(
            painter = when (product.idProduct){
                1 -> painterResource(Res.drawable.ic_product1)
                2 -> painterResource(Res.drawable.ic_product2)
                3 -> painterResource(Res.drawable.ic_product3)
                4 -> painterResource(Res.drawable.ic_product4)
                5 -> painterResource(Res.drawable.ic_product5)
                6 -> painterResource(Res.drawable.ic_product6)
                else -> painterResource(Res.drawable.compose_multiplatform)
            },
            contentDescription = product.name,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = product.name,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$${product.value}",
            style = MaterialTheme.typography.body1,
            color = Color.Gray
        )
    }
}
