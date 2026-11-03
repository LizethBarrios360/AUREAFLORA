package com.compose.trykeeper.domain

import com.compose.trykeeper.presentacion.login.LoginResults


interface LoginRepository {
    fun login(username: String, password: String): LoginResults
}