package com.hocok.passwordmanager.ui.screen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class Routes{

    @Serializable
    data object Login: Routes()

    @Serializable
    data class Registration(val information: String): Routes()

    @Serializable
    data object Home: Routes()

    @Serializable
    data object Search: Routes()

    @Serializable
    data class Create(val id: Int? = null): Routes()

//    Suffix for transition and search
    @Serializable
    data class Details(val id: Int, val suffix: String = ""): Routes()
}

data class BottomNavigationRoute(
    val icon: ImageVector,
    val name: String,
    val route: Routes
)

val navigationList = listOf(
    BottomNavigationRoute(Icons.Outlined.Home, "Home", Routes.Home),
    BottomNavigationRoute(Icons.Outlined.Search, "Search", Routes.Search)
)