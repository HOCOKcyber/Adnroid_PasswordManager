package com.hocok.passwordmanager.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())

    val uiState = _uiState.asStateFlow()

    fun loginEvent(event: LoginEvent){

        when(event){
            is LoginEvent.ChangePassword -> {
                _uiState.value = _uiState.value.copy( password = event.newPassword )
            }
            is LoginEvent.ChangeVisible -> {
                _uiState.value = _uiState.value.copy( isVisible = !uiState.value.isVisible )
            }
        }
    }
}

data class LoginState(
    val password: String = "",
    val isVisible: Boolean = false
)

sealed class LoginEvent{
    data class ChangePassword( val newPassword: String ): LoginEvent()
    data object ChangeVisible: LoginEvent()
}