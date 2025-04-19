package com.hocok.passwordmanager.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hocok.passwordmanager.PasswordManagerApp
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            val newLogin = async {  accountRepository.getAccountsByLoginParams(params) }
            val newService = async { accountRepository.getAccountsByServiceParams(params) }
            _uiState.value = _uiState.value.copy(
                loginList = newLogin.await() ,
                serviceList = newService.await()
            )
        }
    }

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ChangeService -> {
                _uiState.value = _uiState.value.copy(
                    isLogin = false,
                    isService = true,
                )}
            is SearchEvent.ChangeLogin -> {
                _uiState.value = _uiState.value.copy(
                    isLogin = true,
                    isService = false,
                )}
            is SearchEvent.ChangeParams -> {
                _uiState.value = _uiState.value.copy(
                    params = event.newParams
                )}
            is SearchEvent.DeleteAccount -> {
                viewModelScope.launch {
                    accountRepository.deleteAccount(event.id)
                    getAccounts()
                }
            }
            is SearchEvent.Favourite -> {
                viewModelScope.launch {
                    accountRepository.saveAccount(event.account)
                    getAccounts()
                }
            }
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
    val isLogin: Boolean = true,
    val isService: Boolean = false,
    val loginList: List<AccountData> = listOf(),
    val serviceList: List<AccountData> = listOf(),
)

sealed class SearchEvent{
    data class ChangeParams(val newParams: String): SearchEvent()
    data object ChangeLogin: SearchEvent()
    data object ChangeService: SearchEvent()
    data class DeleteAccount(val id: Int): SearchEvent()
    data class Favourite(val account: AccountData): SearchEvent()
}