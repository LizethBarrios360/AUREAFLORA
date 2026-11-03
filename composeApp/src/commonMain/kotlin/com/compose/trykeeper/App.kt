package com.compose.trykeeper

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.di.appModule
import com.compose.trykeeper.navigation.Navigation
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.getKoin


@Composable
@Preview
fun App() {
    PreComposeApp {
        KoinContext {
            AppTheme {
                val navigator = rememberNavigator()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Navigation(navigator)
                }
            }
        }
    }
}
