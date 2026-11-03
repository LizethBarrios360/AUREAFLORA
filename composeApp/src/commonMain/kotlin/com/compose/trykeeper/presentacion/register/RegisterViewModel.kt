package com.compose.trykeeper.presentacion.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.compose.trykeeper.domain.RegisterRepository
import com.compose.trykeeper.model.Users
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class RegisterViewModel(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private var _isRegister by mutableStateOf(false)
    val isRegister: Boolean
        get() = _isRegister

    fun register(user: Users) {
        viewModelScope.launch {
            val register = registerRepository.register(user)
            when (register) {
                is RegisterResults.Success -> {
                    _isRegister = true
                }
                is RegisterResults.Error -> {
                    _isRegister = false
                }
            }
        }
    }
}