package com.compose.trykeeper.domain

import com.compose.trykeeper.model.Users
import com.compose.trykeeper.presentacion.register.RegisterResults

interface RegisterRepository {
    fun register(user: Users): RegisterResults
}