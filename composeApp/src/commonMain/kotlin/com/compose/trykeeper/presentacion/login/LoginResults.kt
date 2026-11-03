package com.compose.trykeeper.presentacion.login

sealed class LoginResults {
    data class Success(val isSuccess: Boolean) : LoginResults()
    data class Error(val isError: Boolean) : LoginResults()
    data class Loading(val isLoading: Boolean) : LoginResults()
}