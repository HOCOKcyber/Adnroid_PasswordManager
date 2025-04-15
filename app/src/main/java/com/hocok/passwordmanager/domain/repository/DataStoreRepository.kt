package com.hocok.passwordmanager.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val userPassword: Flow<String>

    suspend fun savePassword(newPassword: String)

    suspend fun read(): String
}