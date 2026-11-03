package com.compose.trykeeper.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.compose.trykeeper.db.AureaFloraDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AureaFloraDatabase.Schema,
            context = context,
            name = "AureaFloraDatabase.db"
        )
    }
}