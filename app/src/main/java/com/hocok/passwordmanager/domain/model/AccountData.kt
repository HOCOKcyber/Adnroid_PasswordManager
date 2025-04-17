package com.hocok.passwordmanager.domain.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Stable
@Entity
data class AccountData(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val login: String = "example@gmail.com",
    val service: String = "example service",
    val domain: String = "errorDomain",
    val password: String = "examplePassword",
    val isFavourite: Boolean = false,
    )


object ExampleData{
    val accountList: List<AccountData> = listOf(
        AccountData(1, "login1", "vk", "vk.com", "vkPassword"),
        AccountData(2, "login2", "steam", "steampowered.com", "steamPassword"),
        AccountData(3, "login3", "github", "github.com", "githubPassword"),
        AccountData(4, "veeeeeeeeeeeryLonoooooooogLogin", "gmail", "google.com", "gmailPassword")
    )
}