package com.hocok.passwordmanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hocok.passwordmanager.domain.model.AccountData

@Database(
    entities = [AccountData::class],
    version = 1,
    exportSchema = false
)
abstract class AccountDataBase(): RoomDatabase() {

    abstract fun getAccountDao(): AccountDao

    companion object{
        var dbInstance: AccountDataBase? = null

        fun getInstance(context: Context): AccountDataBase = dbInstance ?:
        Room.databaseBuilder(
            context = context,
            klass = AccountDataBase::class.java,
            name = "accounts.db"
        ).build().also { dbInstance =  it }
    }
}