package com.compose.trykeeper

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import moe.tlaster.precompose.ProvidePreComposeLocals

fun main() {
    application {
        Window(
            title = "TryKeeper",
            onCloseRequest = {
                exitApplication()
            },
        ) {
            ProvidePreComposeLocals {
                App()
            }
        }
    }
}