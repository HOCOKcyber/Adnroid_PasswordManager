package com.hocok.passwordmanager.ui.screen.search

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.ui.component.ActionIcon
import com.hocok.passwordmanager.ui.component.SwipeActions
import com.hocok.passwordmanager.ui.screen.home.HomeAccountCard
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    toDetails: (id: Int, suffix: String) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier
){
    val viewModel = viewModel<SearchViewModel>(factory = SearchViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()

    SearchContent(
        uiState = uiState,
        onSearchChange = {viewModel.onEvent(SearchEvent.ChangeParams(it))},
        onAllChange = {viewModel.onEvent(SearchEvent.ChangeAll)},
        onLoginChange = {viewModel.onEvent(SearchEvent.ChangeLogin)},
        onServiceChange = {viewModel.onEvent(SearchEvent.ChangeService)},
        toDetails = toDetails,
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope,
        onSearch = {viewModel.getAccounts()},
        onDelete = {viewModel.onEvent(SearchEvent.DeleteAccount(it))},
        onFavourite = {viewModel.onEvent(SearchEvent.Favourite(it))},
        modifier = modifier
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchContent(
    onSearchChange: (String) -> Unit,
    onAllChange: () -> Unit,
    onLoginChange: () -> Unit,
    onServiceChange: () -> Unit,
    toDetails: (id: Int, suffix: String) -> Unit,
    onSearch: () -> Unit,
    onDelete: (Int) -> Unit,
    onFavourite: (AccountData) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    uiState: SearchState = SearchState(),
){
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = uiState.params,
            onValueChange = { onSearchChange(it) },
            singleLine = true,
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodySmall,
            shape = RoundedCornerShape(10.dp),
            placeholder = { Text(text = stringResource(R.string.search), style = MaterialTheme.typography.bodySmall) },
            leadingIcon = { Icon(Icons.Outlined.Search, stringResource(R.string.search)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(bottom = 10.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch()
                    KeyboardActions.Default.onDone
                }
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            GroupSection(
                name = stringResource(R.string.all),
                isActive = uiState.isAll,
                onChange = onAllChange,
                modifier = Modifier.weight(1f)
            )
            GroupSection(
                name = stringResource(R.string.login),
                isActive = uiState.isLogin,
                onChange = onLoginChange,
                modifier = Modifier.weight(1f)
            )
            GroupSection(
                name = stringResource(R.string.service),
                isActive = uiState.isService,
                onChange = onServiceChange,
                modifier = Modifier.weight(1f)
            )
        }
        LazyColumn(
            modifier = Modifier.padding(top = 10.dp).padding(horizontal = 10.dp)
        ) {
            if ((uiState.isLogin || uiState.isAll)){

                Log.d("Login List Update", "Ok")
                item {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    if (uiState.loginList.isEmpty()) Text(
                        text = stringResource(R.string.empty),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                items(uiState.loginList, key = {"login/${it.id}"}){
                    FoundedItem(
                        account = it,
                        suffix = "login",
                        toDetails = {id, suffix  -> toDetails(id, suffix)},
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onFavourite = {
                            onFavourite(it.copy(isFavourite = !it.isFavourite))
                            onSearch()
                        },
                        onDelete = {
                            onDelete(it.id!!)
                            onSearch()
                        },
                        modifier = Modifier.animateItem()
                    )
                }
            }

            if ((uiState.isService || uiState.isAll)){

                Log.d("Service List Update", "Ok")
                item {
                    Text(
                        text = stringResource(R.string.service),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom =  10.dp)
                    )
                    if (uiState.serviceList.isEmpty()) Text(
                        text = stringResource(R.string.empty),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                items(uiState.serviceList, key = {"service/${it.id}"}){
                    FoundedItem(
                        account = it,
                        suffix = "service",
                        toDetails = {id, suffix  -> toDetails(id, suffix)},
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onFavourite = {
                            onFavourite(it.copy(isFavourite = !it.isFavourite))
                            onSearch()
                                      },
                        onDelete = {
                            onDelete(it.id!!)
                            onSearch()
                        },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoundedItem(
    onDelete: () -> Unit,
    suffix: String,
    toDetails: (Int, String) -> Unit,
    account: AccountData,
    onFavourite: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier
){
    var isClose by remember { mutableStateOf(false) }

    SwipeActions(
        actions = {
            ActionIcon(
                onClick = {
                    isClose = true
                    onDelete()
                },
                icon = Icons.Outlined.Delete,
                backgroundColor = Color.Red,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp) )
            )
            ActionIcon(
                onClick = {
                    isClose = true
                    onFavourite()
                },
                icon = Icons.Outlined.Star,
                backgroundColor = Color.Yellow,
            )
        },
        onExpand = {
            isClose = false
        },
        isClose = isClose,
        modifier = modifier.padding(10.dp)
    ) {
        HomeAccountCard(
            account = account,
            suffix = suffix,
            modifier = Modifier.fillMaxSize(),
            toDetails = { toDetails(account.id!!, suffix)},
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope,
        )
    }
    }


@Composable
fun GroupSection(
    name: String,
    isActive: Boolean,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Light,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.align(Alignment.Center).clickable {
                onChange()
            }
        )
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    showBackground = true,
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true,
    widthDp = 355
)
@Composable
fun SearchContentPreview(){
    PasswordManagerTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                SearchContent(
                    onSearchChange = {},
                    onAllChange = {},
                    onLoginChange = {},
                    onServiceChange = {},
                    toDetails = {_, _ -> },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    modifier = Modifier.padding(PaddingValues(20.dp)),
                    onSearch = {},
                    onDelete = {},
                    onFavourite = {}
                )
            }
        }
    }
}