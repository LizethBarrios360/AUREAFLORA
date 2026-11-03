package com.compose.trykeeper.presentacion.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.compose.trykeeper.domain.LoginRepository
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private var _uiState by mutableStateOf(LoginResults.Loading(false))
    val uiState = _uiState

    private var _isLogin by mutableStateOf(false)
    val isLogin: Boolean
        get() = _isLogin

    private var _isError by mutableStateOf(false)
    val isError: Boolean
        get() = _isError

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState = LoginResults.Loading(true)
            val isLogin = loginRepository.login(username, password)
            when (isLogin) {
                is LoginResults.Success -> {
                    _isLogin = true
                    _uiState = LoginResults.Loading(false)
                }
                is LoginResults.Error -> {
                    _isError = true
                    _uiState = LoginResults.Loading(false)
                }
                else -> {}
            }
        }
    }
}