package com.compose.trykeeper.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.compose.trykeeper.getColorsTheme
import com.compose.trykeeper.presentacion.home.HomeViewModel
import com.compose.trykeeper.presentacion.login.LoginViewModel
import com.compose.trykeeper.presentacion.register.RegisterViewModel
import com.compose.trykeeper.ui.home.HomeScreen
import com.compose.trykeeper.ui.login.LoginScreen
import com.compose.trykeeper.ui.register.RegisterScreen
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import org.koin.core.parameter.parametersOf

@Composable
fun Navigation(navigator: Navigator) {
    val colors = getColorsTheme()

    val viewModel = koinViewModel(LoginViewModel::class) { parametersOf() }
    val viewModelRegister = koinViewModel(RegisterViewModel::class) { parametersOf() }
    val viewModelHome = koinViewModel(HomeViewModel::class) { parametersOf() }

    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/login"
    ) {
        scene(route = "/login") {
            LoginScreen(viewModel) {
                navigator.navigate("/register")
            }

            if (viewModel.isLogin) {
                navigator.navigate("/home")
            }
        }

        scene("/register") {
            RegisterScreen {
                viewModelRegister.register(it)
                if (viewModelRegister.isRegister) {
                    navigator.navigate("/login")
                }
            }
        }

        scene("/home") {
            HomeScreen(viewModelHome.products)
        }
    }
}
