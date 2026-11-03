package com.compose.trykeeper.presentacion.register

sealed class RegisterResults {
    data class Success(val isSuccess: Boolean) : RegisterResults()
    data class Error(val isError: Boolean) : RegisterResults()
}