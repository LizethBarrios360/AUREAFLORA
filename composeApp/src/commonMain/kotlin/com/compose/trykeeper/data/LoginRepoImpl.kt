package com.compose.trykeeper.data

import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.domain.LoginRepository
import com.compose.trykeeper.presentacion.login.LoginResults

class LoginRepoImpl(
    private val aureaFloraDatabase: AureaFloraDatabase
) : LoginRepository {

    private val queries = aureaFloraDatabase.aureaDBQueries

    override fun login(username: String, password: String): LoginResults {
        val user = queries.validateUsername(username).executeAsOneOrNull()
        return if (user != null && user.contrasena == password) LoginResults.Success(true)
        else LoginResults.Error(true)
    }
}