package com.hocok.passwordmanager.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hocok.passwordmanager.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepositoryImp(
    private val dataStore: DataStore<Preferences>
): DataStoreRepository {

    override val userPassword: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG_PASSWORD, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preference ->
            preference[PASSWORD] ?: ""
        }

    override suspend fun savePassword(newPassword: String) {
        dataStore.edit { preference ->
            preference[PASSWORD] = newPassword
        }
    }

    companion object{
        val PASSWORD = stringPreferencesKey("password")
        const val TAG_PASSWORD = "Data Store password"
    }
}