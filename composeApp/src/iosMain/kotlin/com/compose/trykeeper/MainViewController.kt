package com.compose.trykeeper

import androidx.compose.ui.window.ComposeUIViewController
import com.compose.trykeeper.data.DatabaseDriverFactory
import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.di.appModule
import org.koin.core.context.startKoin


fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule(AureaFloraDatabase.invoke(DatabaseDriverFactory().createDriver())))
    }.koin
}