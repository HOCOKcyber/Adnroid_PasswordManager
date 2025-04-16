package com.hocok.passwordmanager.ui.screen.search

import android.content.ClipData
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
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
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    uiState: SearchState = SearchState(),
){
    Column(
        modifier = modifier
    ) {
        val clipboardManager = LocalClipboardManager.current
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
            if ((uiState.isLogin || uiState.isAll) && uiState.loginList.isNotEmpty()){
                item {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                items(uiState.loginList){
                    HomeAccountCard(
                        account = it,
                        modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                        toDetails = { toDetails(it.id!!, "login") },
                        suffix = "login",
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onCopy = {
                            val clipData = ClipData.newPlainText("password", it.password)
                            val clipEntry = ClipEntry(clipData)
                            clipboardManager.setClip(clipEntry)
                        }
                    )
                }
            }

            if ((uiState.isService || uiState.isAll) && uiState.serviceList.isNotEmpty()){
                item {
                    Text(
                        text = stringResource(R.string.service),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom =  10.dp)
                    )
                }
                items(uiState.serviceList){
                    HomeAccountCard(
                        account = it,
                        suffix = "service",
                        modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                        toDetails = { toDetails(it.id!!, "service")},
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onCopy = {
                            val clipData = ClipData.newPlainText("password", it.password)
                            val clipEntry = ClipEntry(clipData)
                            clipboardManager.setClip(clipEntry)
                        }
                    )
                }
            }
        }
        
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
    showSystemUi = true,
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
                    toDetails = {id, suffix -> },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    modifier = Modifier.padding(PaddingValues(20.dp)),
                    onSearch = {}
                )
            }
        }
    }
}