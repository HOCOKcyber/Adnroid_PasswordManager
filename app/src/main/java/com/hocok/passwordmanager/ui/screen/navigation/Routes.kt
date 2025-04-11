package com.hocok.passwordmanager.ui.screen.navigation

import kotlinx.serialization.Serializable


sealed class Routes{

    @Serializable
    data object Login: Routes()

    @Serializable
    data object Registration: Routes()

    @Serializable
    data object Home: Routes()
}