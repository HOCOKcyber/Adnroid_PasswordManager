package com.hocok.passwordmanager.data.repository

import com.hocok.passwordmanager.data.db.AccountDao
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.model.PasswordManagerUtils
import com.hocok.passwordmanager.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class AccountRepositoryImp(
    private val accountDao: AccountDao
): AccountRepository {
    override fun getAllAccount(): Flow<List<AccountData>> {
        return accountDao.getAllAccount()
    }

    override suspend fun getAccountById(id: Int): AccountData {
        val cryptoAccount = accountDao.getAccountById(id)
        return cryptoAccount.copy(
            password = PasswordManagerUtils.crypto(cryptoAccount.password, -PasswordManagerUtils.SHIFT)
        )
    }

    override suspend fun saveAccount(account: AccountData) {
        val cryptoAccount = account.copy(
            password = PasswordManagerUtils.crypto(account.password, PasswordManagerUtils.SHIFT)
        )
        return accountDao.saveAccount(cryptoAccount)
    }

    override suspend fun getAccountsByLoginParams(param: String): List<AccountData> {
        return accountDao.getAccountsByLoginParams("%$param%")
    }

    override suspend fun getAccountsByServiceParams(param: String): List<AccountData> {
        return accountDao.getAccountsByServiceParams("%$param%")
    }

    override suspend fun deleteAccount(id: Int) {
        accountDao.deleteAccount(id)
    }

    override suspend fun deleteAll() {
        accountDao.deleteAll()
    }

}