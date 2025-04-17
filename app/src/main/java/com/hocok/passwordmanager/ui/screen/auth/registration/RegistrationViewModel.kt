package com.hocok.passwordmanager.ui.screen.auth.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    val dataStoreRep: DataStoreRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationState())

    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegistrationEvent){
        when(event){
            is RegistrationEvent.PasswordChange -> {
                _uiState.value = _uiState.value.copy( password = event.newPassword, errorMessage = "" )
            }
            is RegistrationEvent.RepeatPasswordChange -> {
                _uiState.value = _uiState.value.copy( repeatPassword = event.newRepeatPassword, repeatErrorMessage = "" )
            }
            is RegistrationEvent.VisibleChange -> {
                _uiState.value = _uiState.value.copy( isVisible = !uiState.value.isVisible )
            }
            is RegistrationEvent.RepeatVisibleChange -> {
                _uiState.value = _uiState.value.copy( isRepeatVisible = !uiState.value.isRepeatVisible )
            }
            is RegistrationEvent.Submit -> {
                var err = ""
                var repeatErr = ""
                if (_uiState.value.password.length < 8) err = "Менее 8 символов"
                if (_uiState.value.repeatPassword != _uiState.value.password) repeatErr = "Не совпадает"
                else {
                    Log.d("Regist",_uiState.value.password)
                    viewModelScope.launch {
                        dataStoreRep.savePassword(_uiState.value.password)
                        event.oSubmit()
                    }
                    return Unit
                }

                _uiState.value = _uiState.value.copy(
                    password = "",
                    repeatPassword = "",
                    errorMessage = err,
                    repeatErrorMessage = repeatErr
                )
            }
        }
    }

    companion object{
        val factory = viewModelFactory {
            initializer {
                val dataStoreRep = (this[APPLICATION_KEY] as PasswordManagerApp).dataStoreRepository
                RegistrationViewModel(dataStoreRep = dataStoreRep)
            }
        }
    }
}

data class RegistrationState(
    val password: String = "",
    val repeatPassword: String = "",
    val isVisible: Boolean = false,
    val isRepeatVisible: Boolean = false,
    val errorMessage: String = "",
    val repeatErrorMessage: String = "",
)

sealed class RegistrationEvent{
    data class PasswordChange(val newPassword: String): RegistrationEvent()
    data class RepeatPasswordChange(val newRepeatPassword: String): RegistrationEvent()
    data object VisibleChange: RegistrationEvent()
    data object RepeatVisibleChange: RegistrationEvent()
    data class Submit(val oSubmit: () -> Unit) : RegistrationEvent()
}