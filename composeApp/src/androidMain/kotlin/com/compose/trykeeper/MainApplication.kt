package com.compose.trykeeper

import android.app.Application
import com.compose.trykeeper.data.DatabaseDriverFactory
import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule(AureaFloraDatabase.invoke(DatabaseDriverFactory(this@MainApplication).createDriver())))
        }
    }
}