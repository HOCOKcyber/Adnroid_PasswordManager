package com.hocok.passwordmanager.ui.screen.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hocok.passwordmanager.domain.model.ExampleData
import com.hocok.passwordmanager.ui.screen.auth.login.LoginScreen
import com.hocok.passwordmanager.ui.screen.auth.registration.RegistrationScreen
import com.hocok.passwordmanager.ui.screen.create.CreateScreen
import com.hocok.passwordmanager.ui.screen.home.HomeScreenContent
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun NavigationScreen(){

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (navigationList.any{ currentDestination?.hasRoute(it.route::class) == true })
            NavigationBottomBar(
                currentDestination = currentDestination,
                onClick = { navController.navigate(it) }
            )
        },
        floatingActionButton = {
            PasswordManagerFloatingActionButton(
                onClick = { navController.navigate(Routes.Create) }
            )
        }
    ) {
        innerPadding ->

        NavHost(
            startDestination = Routes.Login,
            navController = navController
        ) {
            composable<Routes.Login> {
                LoginScreen(
                    toRegistration = { navController.navigate(Routes.Registration) },
                    toHome = { navController.navigate(Routes.Home) }
                )
            }

            composable<Routes.Registration> {
                RegistrationScreen(
                    toHome = { navController.navigate(Routes.Home) }
                )
            }

            composable<Routes.Home> {
                HomeScreenContent(
                    accountList = ExampleData.accountList,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable<Routes.Search> {
                Text(
                    text = "SEARCH PAGE"
                )
            }

            composable<Routes.Create> {
                CreateScreen(
                    paddingValue = innerPadding
                )
            }
        }

    }
}

@Composable
fun NavigationBottomBar(
    currentDestination: NavDestination?,
    onClick: (Routes) -> Unit,
){
    BottomNavigation{
        navigationList.forEach { navigationRoute ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = navigationRoute.icon,
                        contentDescription = navigationRoute.name,
                        tint = Color.White
                    )
                },
                selected = currentDestination?.hasRoute(navigationRoute.route::class) == true,
                onClick = { onClick(navigationRoute.route)}
            )
        }
    }
}

@Composable
fun PasswordManagerFloatingActionButton(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier.clickable {
            onClick()
        }
    ){
        Text(
            text = "+"
        )
    }
}

@Preview
@Composable
fun NavigationBottomBarPreview(){
    PasswordManagerTheme {
        NavigationBottomBar(
            onClick = {},
            currentDestination = null
        )
    }
}