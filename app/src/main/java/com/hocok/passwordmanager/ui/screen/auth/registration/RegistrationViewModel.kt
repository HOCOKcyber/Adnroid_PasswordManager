package com.hocok.passwordmanager.ui.screen.auth.registration

import androidx.lifecycle.ViewModel
import com.hocok.passwordmanager.ui.screen.auth.login.LoginEvent
import com.hocok.passwordmanager.ui.screen.auth.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationState())

    val uiState = _uiState.asStateFlow()

    fun registrationEvent(event: RegistrationEvent){
        when(event){
            is RegistrationEvent.PasswordChange -> {
                _uiState.value = _uiState.value.copy( password = event.newPassword )
            }
            is RegistrationEvent.RepeatPasswordChange -> {
                _uiState.value = _uiState.value.copy( repeatPassword = event.newRepeatPassword )
            }
            is RegistrationEvent.VisibleChange -> {
                _uiState.value = _uiState.value.copy( isVisible = !uiState.value.isVisible )
            }
            is RegistrationEvent.RepeatVisibleChange -> {
                _uiState.value = _uiState.value.copy( isRepeatVisible = !uiState.value.isRepeatVisible )
            }
        }
    }
}

data class RegistrationState(
    val password: String = "",
    val repeatPassword: String = "",
    val isVisible: Boolean = false,
    val isRepeatVisible: Boolean = false,
)

sealed class RegistrationEvent{
    data class PasswordChange(val newPassword: String): RegistrationEvent()
    data class RepeatPasswordChange(val newRepeatPassword: String): RegistrationEvent()
    data object VisibleChange: RegistrationEvent()
    data object RepeatVisibleChange: RegistrationEvent()
}