package com.hocok.passwordmanager.ui.screen.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hocok.passwordmanager.ui.screen.auth.login.LoginScreen
import com.hocok.passwordmanager.ui.screen.auth.registration.RegistrationScreen

@Composable
fun NavigationScreen(){

    Scaffold {
        innerPadding ->
        val pad = innerPadding

        val navController = rememberNavController()

        NavHost(
            startDestination = Routes.Login,
            navController = navController
        ) {
            composable<Routes.Login> {
                LoginScreen(
                    toRegistration = { navController.navigate(Routes.Registration) }
                )
            }

            composable<Routes.Registration> {
                RegistrationScreen()
            }
        }


    }
}

