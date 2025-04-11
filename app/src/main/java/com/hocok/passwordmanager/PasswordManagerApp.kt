package com.hocok.passwordmanager

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hocok.passwordmanager.data.repository.DataStoreRepositoryImp
import com.hocok.passwordmanager.domain.repository.DataStoreRepository

private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES
)

class PasswordManagerApp: Application() {
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate() {
        super.onCreate()
        dataStoreRepository = DataStoreRepositoryImp(dataStore)
    }
}