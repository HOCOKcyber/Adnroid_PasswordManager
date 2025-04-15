package com.hocok.passwordmanager.ui.screen.details

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    val accountRepository: AccountRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AccountData())
    val uiState = _uiState.asStateFlow()

    fun fetchAccount(id: Int){
        viewModelScope.launch {
            _uiState.update { accountRepository.getAccountById(id) }
        }
    }

    fun deleteAccount(id: Int){
        viewModelScope.launch {
            accountRepository.deleteAccount(id)
        }
    }

    companion object{
        val factory = viewModelFactory {
            initializer {
                val accountRepository = (this[APPLICATION_KEY] as PasswordManagerApp).accountRepository
                DetailsViewModel(accountRepository = accountRepository)
            }
        }
    }

}