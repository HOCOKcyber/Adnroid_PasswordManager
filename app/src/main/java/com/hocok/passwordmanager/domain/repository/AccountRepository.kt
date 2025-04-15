package com.hocok.passwordmanager.domain.repository

import com.hocok.passwordmanager.domain.model.AccountData
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun getAllAccount(): Flow<List<AccountData>>

    suspend fun getAccountById(id: Int): AccountData

    suspend fun saveAccount(account: AccountData)

    suspend fun deleteAccount(id: Int)
}