package com.hocok.passwordmanager.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hocok.passwordmanager.domain.model.AccountData
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM AccountData ORDER BY isFavourite DESC")
    fun getAllAccount(): Flow<List<AccountData>>

    @Query("SELECT * FROM AccountData where id = :id")
    suspend fun getAccountById(id: Int): AccountData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccount(account: AccountData)

    @Query("SELECT * FROM AccountData WHERE login like :param ORDER BY isFavourite DESC, login")
    suspend fun getAccountsByLoginParams(param: String): List<AccountData>

    @Query("SELECT * FROM AccountData WHERE service like :param ORDER BY isFavourite DESC, service")
    suspend fun getAccountsByServiceParams(param: String): List<AccountData>

    @Query("DELETE FROM AccountData where id = :id")
    suspend fun deleteAccount(id: Int)

    @Query("DELETE FROM AccountData ")
    suspend fun deleteAll()
}