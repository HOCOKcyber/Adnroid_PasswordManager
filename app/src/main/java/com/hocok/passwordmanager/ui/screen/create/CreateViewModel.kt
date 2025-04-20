package com.hocok.passwordmanager.ui.screen.create

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.model.PasswordManagerUtils
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateViewModel(
    val accountRepository: AccountRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<CreateState> = MutableStateFlow(CreateState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CreateEvent){
        when(event){
            is CreateEvent.ChangeLogin ->           _uiState.update{it.copy(login = event.newLogin)}
            is CreateEvent.ChangeService ->         _uiState.update{it.copy(service = event.newService)}
            is CreateEvent.ChangePassword ->        _uiState.update{it.copy(password = event.newPassword)}
            is CreateEvent.ChangeDomain ->          _uiState.update{it.copy(domain = event.newDomain)}
            is CreateEvent.ChangeSlider ->          _uiState.update{it.copy(length = event.newLength.toInt().toString())}
            is CreateEvent.ChangeLength ->          _uiState.update{it.copy(length = event.newLength)}
            is CreateEvent.CheckedNumbers ->        _uiState.update{it.copy(isNumbers = !it.isNumbers)}
            is CreateEvent.CheckedSymbols ->        _uiState.update{it.copy(isSymbols = !it.isSymbols)}
            is CreateEvent.CheckedUpperLetter ->    _uiState.update{it.copy(isUpperLetter = !it.isUpperLetter)}
            is CreateEvent.ChangeMask ->            _uiState.update { it.copy(mask = event.newMask) }
            is CreateEvent.ChangePasswordMethod ->  _uiState.update { it.copy(isRandomPassword = !it.isRandomPassword) }

            is CreateEvent.CheckLength -> {
                var newLength: String
                try {
                    val correctLength = _uiState.value.length.toInt()
                    newLength = correctLength.coerceIn(8, 20).toString()
                } catch (e: NumberFormatException){
                    newLength = "8"
                }
                _uiState.value = _uiState.value.copy( length = newLength )
            }

            is CreateEvent.GenerateMaskPassword -> {
                val newPassword = PasswordManagerUtils.createMaskPassword(_uiState.value.mask.trim())
                val errorMessage = if (newPassword.isEmpty()) event.context.getString(R.string.mask_error) else ""
                _uiState.update { it.copy(password = newPassword, maskError = errorMessage) }
            }

            is CreateEvent.GenerateRandomPassword -> {
                val newPassword = PasswordManagerUtils.createRandomPassword(
                    length = uiState.value.length.toInt(),
                    isNumber = uiState.value.isNumbers,
                    isSymbols = uiState.value.isSymbols,
                    isUpperLetter = uiState.value.isUpperLetter
                )

                _uiState.value = _uiState.value.copy(password = newPassword)
            }

            is CreateEvent.OnSave -> {
                val login = _uiState.value.login
                val service = _uiState.value.service
                val domain = _uiState.value.domain
                val password = _uiState.value.password

                viewModelScope.launch {
                    accountRepository.saveAccount(AccountData(
                        id = _uiState.value.id,
                        login = login,
                        password = password,
                        service = service,
                        domain = domain
                    ))
                }

            }

        }
    }

    suspend fun fetchAccount(id: Int){
        val account = accountRepository.getAccountById(id)
        _uiState.value = _uiState.value.copy(
            id = id,
            login = account.login,
            password = account.password,
            service = account.service,
            domain = account.domain
        )
    }

    companion object{
        val factory = viewModelFactory {
            initializer {
                val accountRepository = (this[APPLICATION_KEY] as PasswordManagerApp).accountRepository
                CreateViewModel(accountRepository = accountRepository)
            }
        }
    }
}

data class CreateState(
    val id: Int? = null,
    val login: String = "",
    val service: String = "",
    val domain: String = "",
    val password: String = "",
    val length: String = "8",
    val mask: String = "",
    val maskError: String = "",
    val isRandomPassword: Boolean = true,
    val isNumbers: Boolean = false,
    val isSymbols: Boolean = false,
    val isUpperLetter: Boolean = false,
)

sealed class CreateEvent{
    data class ChangeLogin(val newLogin: String): CreateEvent()
    data class ChangeService(val newService: String): CreateEvent()
    data class ChangeDomain(val newDomain: String): CreateEvent()
    data class ChangePassword(val newPassword: String): CreateEvent()
    data class ChangeMask(val newMask: String): CreateEvent()
    data class ChangeLength(val newLength: String): CreateEvent()
    data class ChangeSlider(val newLength: Float): CreateEvent()
    data object ChangePasswordMethod: CreateEvent()
    data object CheckLength: CreateEvent()
    data object CheckedNumbers: CreateEvent()
    data object CheckedSymbols: CreateEvent()
    data object CheckedUpperLetter: CreateEvent()
    data object GenerateRandomPassword: CreateEvent()
    data class GenerateMaskPassword(val context: Context): CreateEvent()
    data object OnSave: CreateEvent()
}