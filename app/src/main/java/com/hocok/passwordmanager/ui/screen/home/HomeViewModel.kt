package com.hocok.passwordmanager.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.data.db.AccountDao
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
  accountRepository: AccountRepository
) :ViewModel() {
    val accountList: StateFlow<List<AccountData>> = accountRepository.getAllAccount().stateIn(
        scope =  viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf<AccountData>()
    )

    companion object{
        val factory = viewModelFactory {
            initializer {
                val accountRepository = (this[APPLICATION_KEY] as PasswordManagerApp).accountRepository
                HomeViewModel(accountRepository = accountRepository)
            }
        }
    }
}