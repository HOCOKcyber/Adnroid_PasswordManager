package com.hocok.passwordmanager.ui.screen.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val lowerLetter = "abcdefghijklmnopqrstuvwxyz"
const val numbers = "0123456789"
const val symbols = "!@#$%^&*?_"
const val upperLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

class CreateViewModel(
    val accountRepository: AccountRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<CreateState> = MutableStateFlow(CreateState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CreateEvent){
        when(event){
            is CreateEvent.ChangeLogin -> {_uiState.value = _uiState.value.copy(login = event.newLogin)}
            is CreateEvent.ChangeService -> {_uiState.value = _uiState.value.copy(service = event.newService)}
            is CreateEvent.ChangePassword -> {_uiState.value = _uiState.value.copy(password = event.newPassword)}
            is CreateEvent.ChangeDomain -> {_uiState.value = _uiState.value.copy(domain = event.newDomain)}
            is CreateEvent.ChangeSlider -> {_uiState.value = _uiState.value.copy(length = event.newLength.toInt().toString())}
            is CreateEvent.ChangeLength -> {_uiState.value = _uiState.value.copy(length = event.newLength)}
            is CreateEvent.CheckedNumbers -> {_uiState.value = _uiState.value.copy(isNumbers = !_uiState.value.isNumbers)}
            is CreateEvent.CheckedSymbols -> {_uiState.value = _uiState.value.copy(isSymbols = !_uiState.value.isSymbols)}
            is CreateEvent.CheckedUpperLetter -> {_uiState.value = _uiState.value.copy(isUpperLetter = !_uiState.value.isUpperLetter)}

            is CreateEvent.CheckLength -> {
                var newLength: String

                try {
                    val correctLength = _uiState.value.length.toInt()
                    newLength = if (correctLength > 20) "20"
                                else if (correctLength < 8) "8"
                                else correctLength.toString()
                } catch (e: NumberFormatException){
                    newLength = "8"
                }

                _uiState.value = _uiState.value.copy( length = newLength)

            }


            is CreateEvent.GeneratePassword -> {
                var newPassword = ""
                var alphabet = lowerLetter
                if (_uiState.value.isNumbers) alphabet += numbers
                if (_uiState.value.isSymbols) alphabet += symbols
                if (_uiState.value.isUpperLetter) alphabet += upperLetter

                for (i in 0.._uiState.value.length.toInt()){
                    val letter_index = (alphabet.indices).random()
                    newPassword += alphabet[letter_index]
                }

                val isNumberInPassword = numbers.indices.any { numbers[it] in newPassword }
                val isSymbolInPassword = symbols.indices.any { symbols[it] in newPassword }
                val isUpperLetterInPassword = upperLetter.indices.any { upperLetter[it] in newPassword }

                if (_uiState.value.isNumbers && !isNumberInPassword){
                    newPassword += numbers[numbers.indices.random()]
                }
                if (_uiState.value.isSymbols && !isSymbolInPassword){
                    newPassword += symbols[symbols.indices.random()]
                }
                if (_uiState.value.isUpperLetter && !isUpperLetterInPassword){
                    newPassword += upperLetter[upperLetter.indices.random()]
                }

                _uiState.value = _uiState.value.copy(password = newPassword)
            }

            is CreateEvent.OnSave -> {
                val login = _uiState.value.login
                val service = _uiState.value.service
                val domain = _uiState.value.domain
                val password = _uiState.value.password

                if (domain.isNotEmpty() &&
                    login.isNotEmpty() &&
                    service.isNotEmpty() &&
                    password.isNotEmpty())
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
    val isNumbers: Boolean = false,
    val isSymbols: Boolean = false,
    val isUpperLetter: Boolean = false,
)

sealed class CreateEvent{
    data class ChangeLogin(val newLogin: String): CreateEvent()
    data class ChangeService(val newService: String): CreateEvent()
    data class ChangeDomain(val newDomain: String): CreateEvent()
    data class ChangePassword(val newPassword: String): CreateEvent()
    data class ChangeLength(val newLength: String): CreateEvent()
    data class ChangeSlider(val newLength: Float): CreateEvent()
    data object CheckLength: CreateEvent()
    data object CheckedNumbers: CreateEvent()
    data object CheckedSymbols: CreateEvent()
    data object CheckedUpperLetter: CreateEvent()
    data object GeneratePassword: CreateEvent()
    data object OnSave: CreateEvent()
}