package com.hocok.passwordmanager.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.domain.model.PasswordManagerUtils
import com.hocok.passwordmanager.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val dataStoreRep: DataStoreRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())

    val uiState = _uiState.asStateFlow()

    suspend fun isFirstEnter(
        toRegistration: () -> Unit,
    ){
        val correctPassword = dataStoreRep.read()
        if (correctPassword == "") toRegistration()
    }


    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.ChangePassword -> {
                _uiState.value = _uiState.value.copy(password = event.newPassword, passwordError = "")
            }
            is LoginEvent.ChangeVisible -> {
                _uiState.value = _uiState.value.copy( isVisible = !uiState.value.isVisible )
            }
            is LoginEvent.Submit -> {
                viewModelScope.launch {
                    val correctPassword = dataStoreRep.read()
                    val userCryptoPassword = PasswordManagerUtils.crypto(_uiState.value.password, PasswordManagerUtils.SHIFT)

                    if (userCryptoPassword == correctPassword) event.onSubmit()
                    else _uiState.value = _uiState.value.copy(password = "", passwordError = "Неверный пароль")
                }
            }
        }
    }

    companion object{
        val factory = viewModelFactory {
            initializer {
                val dataStoreRep = (this[APPLICATION_KEY] as PasswordManagerApp).dataStoreRepository
                LoginViewModel(dataStoreRep = dataStoreRep)
            }
        }
    }
}

data class LoginState(
    val password: String = "",
    val passwordError: String = "",
    val isVisible: Boolean = false,
)

sealed class LoginEvent{
    data class ChangePassword( val newPassword: String ): LoginEvent()
    data object ChangeVisible: LoginEvent()
    data class Submit( val onSubmit: () -> Unit ): LoginEvent()
}