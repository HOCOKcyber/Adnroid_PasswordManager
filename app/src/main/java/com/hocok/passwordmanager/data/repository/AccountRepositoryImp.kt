package com.hocok.passwordmanager.data.repository

import com.hocok.passwordmanager.data.db.AccountDao
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class AccountRepositoryImp(
    private val accountDao: AccountDao
): AccountRepository {
    override fun getAllAccount(): Flow<List<AccountData>> {
        return accountDao.getAllAccount()
    }

    override suspend fun getAccountById(id: Int): AccountData {
        return accountDao.getAccountById(id)
    }

    override suspend fun saveAccount(account: AccountData) {
        return accountDao.saveAccount(account)
    }

    override suspend fun deleteAccount(account: AccountData) {
        TODO("Not yet implemented")
    }

}