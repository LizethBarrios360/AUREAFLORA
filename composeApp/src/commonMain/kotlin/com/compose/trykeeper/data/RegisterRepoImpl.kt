package com.compose.trykeeper.data

import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.domain.RegisterRepository
import com.compose.trykeeper.model.Users
import com.compose.trykeeper.presentacion.register.RegisterResults

class RegisterRepoImpl(
    private val aureaFloraDatabase: AureaFloraDatabase
) : RegisterRepository {

    private val queries = aureaFloraDatabase.aureaDBQueries

    override fun register(user: Users): RegisterResults {
        val existingUser = queries.validateUsername(user.userName).executeAsOneOrNull()
        return if (existingUser == null) {
            queries.createUser(
                nombre = user.name,
                apellido = user.lastName,
                cedula = user.cedula,
                celular = user.celular,
                codigo_vendedor = user.codeSale,
                usuario = user.userName,
                contrasena = user.password
            )
            RegisterResults.Success(true)
        } else {
            RegisterResults.Error(true)
        }
    }
}