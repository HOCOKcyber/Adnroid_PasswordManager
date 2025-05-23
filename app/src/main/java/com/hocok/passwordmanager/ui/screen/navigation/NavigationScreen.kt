package com.hocok.passwordmanager.ui.screen.navigation

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.login.LoginScreen
import com.hocok.passwordmanager.ui.screen.auth.registration.RegistrationScreen
import com.hocok.passwordmanager.ui.screen.create.CreateScreen
import com.hocok.passwordmanager.ui.screen.details.DetailsScreen
import com.hocok.passwordmanager.ui.screen.home.HomeScreen
import com.hocok.passwordmanager.ui.screen.search.SearchScreen
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationScreen(){

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    SharedTransitionLayout {
        NavHost(
            startDestination = Routes.Login,
            navController = navController
        ) {
            composable<Routes.Login> {
                LoginScreen(
                    toRegistration = { navController.navigate(Routes.Registration(it)){
                        popUpTo<Routes.Login>()
                    } },
                    toHome = { navController.navigate(Routes.Home){
                        popUpTo(Routes.Login){
                            inclusive = true
                        }
                    } }
                )

            }

            composable<Routes.Registration> {
                val information: Int = it.toRoute<Routes.Registration>().information
                RegistrationScreen(
                    information = information,
                    toHome = { navController.navigate(Routes.Home){
                        popUpTo(Routes.Registration(information)){
                            inclusive = true
                        }
                    } }
                )
            }

            composable<Routes.Home> {
                MainContentWrapper(
                    currentDestination = currentDestination,
                    onBottomNavigate = { navController.navigate(it){
                        popUpTo(it){
                        inclusive = true
                    }} },
                    toCreate = { navController.navigate(Routes.Create())}
                ) { modifier ->
                    HomeScreen(
                        toDetails = { id -> navController.navigate(Routes.Details(id)) },
                        modifier = modifier,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
            }

            composable<Routes.Search> {
                MainContentWrapper(
                    currentDestination = currentDestination,
                    onBottomNavigate = { navController.navigate(it){
                        popUpTo(it){
                            inclusive = true
                        }}  },
                    toCreate = { navController.navigate(Routes.Create()) }
                ) { modifier ->
                    SearchScreen(
                        toDetails = {id, suffix -> navController.navigate(Routes.Details(id, suffix))},
                        animatedVisibilityScope = this,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        modifier = modifier
                    )
                }
            }

            composable<Routes.Create> {
                val accountDetailsId = it.toRoute<Routes.Create>().id
                TopBarWrapper(
                    title = if (accountDetailsId != null) stringResource(R.string.add_password)
                    else stringResource(R.string.change_password),
                    onBack = { navController.popBackStack() },
                ) { modifier ->
                    CreateScreen(
                        id = accountDetailsId,
                        modifier = modifier,
                        toHome = { navController.popBackStack() }
                    )
                }
            }

            composable<Routes.Details> {
                val accountDetailsId = it.toRoute<Routes.Details>().id
                val suffix = it.toRoute<Routes.Details>().suffix

                TopBarWrapper(
                    title = stringResource(R.string.details),
                    onBack = { navController.popBackStack() },
                ) { modifier ->
                    DetailsScreen(
                        accountId = accountDetailsId,
                        toChange = { navController.navigate(Routes.Create(accountDetailsId)) },
                        modifier = modifier,
                        suffix = suffix,
                        animatedVisibilityScope = this,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }
            }
        }
    }
}

@Composable
fun MainContentWrapper(
    currentDestination: NavDestination?,
    onBottomNavigate: (Routes) -> Unit,
    toCreate: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit = {},
){
    Scaffold(
        bottomBar = {
            NavigationBottomBar(
                currentDestination = currentDestination,
                onClick = onBottomNavigate,
            )},
        floatingActionButton = {
            PasswordManagerFloatingActionButton(
                onClick = toCreate
            )
        },

    ) {
        content(
            Modifier.padding(it)
        )
    }
}

@Composable
fun NavigationBottomBar(
    currentDestination: NavDestination?,
    onClick: (Routes) -> Unit,
){
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
    ){
        navigationList.forEach { navigationRoute ->
            val isSelect = currentDestination?.hasRoute(navigationRoute.route::class) == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = navigationRoute.icon,
                        contentDescription = navigationRoute.name,
                        tint = if (isSelect) MaterialTheme.colorScheme.surface
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                selected = isSelect,
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
        modifier = Modifier
            .padding(bottom = 20.dp, end = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable { onClick() }
    ){
        Text(
            text = "+",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        )
    }
}


@Composable
fun TopBarWrapper(
    title: String,
    onBack: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
){
    Scaffold(
        topBar = {PasswordManagerTopBar(
            title = title,
            onBack = onBack,
        )}
    ) {
        content(Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerTopBar(
    title: String,
    onBack: () -> Unit,
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 28.sp,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(
    showBackground = true
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PasswordManagerFloatingActionButtonPreview(){
    PasswordManagerTheme {
        PasswordManagerFloatingActionButton(
            onClick = {}
        )
    }
}

@Preview
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun NavigationBottomBarPreview(){
    PasswordManagerTheme {
        NavigationBottomBar(
            onClick = {},
            currentDestination = null
        )
    }
}

@Preview(
    showBackground = true,
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true,
    widthDp = 355,
)
@Composable
fun PasswordManagerTopBarPreview(){
    PasswordManagerTopBar(
        title = "Изменение пароля",
        onBack = {}
    )
}