package com.hocok.passwordmanager.ui.screen.home

import android.content.ClipData
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.model.ExampleData
import com.hocok.passwordmanager.ui.component.AccountPreview
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    toDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
){
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory)
    val accountList by viewModel.accountList.collectAsState()
    HomeScreenContent(
        accountList = accountList,
        toDetails = toDetails,
        modifier = modifier,

        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreenContent(
    accountList: List<AccountData>,
    toDetails: (id: Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier
){
    val clipboardManager = LocalClipboardManager.current
    LazyColumn(
        modifier = modifier
    ) {
        items(accountList){account ->
            HomeAccountCard(
                account = account,
                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                toDetails = { toDetails(account.id!!) },
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope,
                onCopy = {
                    val clipData = ClipData.newPlainText("password", account.password)
                    val clipEntry = ClipEntry(clipData)
                    clipboardManager.setClip(clipEntry)
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeAccountCard(
    account: AccountData,
    toDetails: () -> Unit,
    onCopy: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.clickable { toDetails() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp).fillMaxWidth(),
        ) {
            with(sharedTransitionScope){
                AccountPreview(
                    account = account,
                    modifier = Modifier.weight(1f).sharedElement(
                        rememberSharedContentState(key = account.id.toString()),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }
            Spacer(Modifier.width(10.dp))
            IconButton(
                onClick = onCopy,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.copy_image),
                    contentDescription = stringResource(R.string.copy)
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenContentPreview(){
    PasswordManagerTheme {
        SharedTransitionLayout {
        AnimatedVisibility(true) {
        HomeScreenContent(
            accountList = ExampleData.accountList,
            toDetails = {},
            sharedTransitionScope = this@SharedTransitionLayout,
            animatedVisibilityScope = this,
        )}}
    }
}