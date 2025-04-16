package com.hocok.passwordmanager.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.repository.AccountRepository
import com.hocok.passwordmanager.ui.screen.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    val accountRepository: AccountRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState = _uiState.asStateFlow()

    init {
        getAccounts()
    }

    fun getAccounts(){
        val params: String = _uiState.value.params
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loginList = accountRepository.getAccountsByLoginParams(params),
                serviceList = accountRepository.getAccountsByServiceParams(params)
            )
        }
    }

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ChangeAll -> {
                _uiState.value = _uiState.value.copy(
                    isAll = true,
                    isLogin = false,
                    isService = false,
                )
            }
            is SearchEvent.ChangeService -> {
                _uiState.value = _uiState.value.copy(
                    isAll = false,
                    isLogin = false,
                    isService = true,
                )}
            is SearchEvent.ChangeLogin -> {
                _uiState.value = _uiState.value.copy(
                    isAll = false,
                    isLogin = true,
                    isService = false,
                )}
            is SearchEvent.ChangeParams -> {
                _uiState.value = _uiState.value.copy(
                    params = event.newParams
                )}
        }
    }

    companion object{
        val factory = viewModelFactory {
            initializer {
                val accountRepository = (this[APPLICATION_KEY] as PasswordManagerApp).accountRepository
                SearchViewModel(accountRepository = accountRepository)
            }
        }
    }
}

data class SearchState(
    val params: String = "",
    val isAll: Boolean = true,
    val isLogin: Boolean = false,
    val isService: Boolean = false,
    val loginList: List<AccountData> = listOf(),
    val serviceList: List<AccountData> = listOf(),
)

sealed class SearchEvent{
    data class ChangeParams(val newParams: String): SearchEvent()
    data object ChangeAll: SearchEvent()
    data object ChangeLogin: SearchEvent()
    data object ChangeService: SearchEvent()
}