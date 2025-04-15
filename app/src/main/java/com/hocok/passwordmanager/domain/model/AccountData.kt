package com.hocok.passwordmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountData(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val login: String = "example@gmail.com",
    val service: String = "example service",
    val domain: String = "errorDomain",
    val password: String = "examplePassword",
    )


object ExampleData{
    val accountList: List<AccountData> = listOf(
        AccountData(null, "login1", "vk", "vk.com", "vkPassword"),
        AccountData(null, "login2", "steam", "steampowered.com", "steamPassword"),
        AccountData(null, "login3", "github", "github.com", "githubPassword"),
        AccountData(null, "login4", "gmail", "google.com", "gmailPassword")
    )
}