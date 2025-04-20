package com.hocok.passwordmanager.ui.screen.home

import android.content.ClipData
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.model.ExampleData
import com.hocok.passwordmanager.ui.component.AccountPreview
import com.hocok.passwordmanager.ui.component.ActionIcon
import com.hocok.passwordmanager.ui.component.SwipeActions
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
        onDelete = {viewModel.deleteAccount(it)},
        onFavourite = {viewModel.saveFavorite(it)},
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreenContent(
    accountList: List<AccountData>,
    toDetails: (id: Int) -> Unit,
    onFavourite: (account: AccountData) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDelete: (id: Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
    ) {
        items(accountList, key = {it.id!!}){account ->
            var isClose by remember { mutableStateOf(false) }

            SwipeActions(
                actions = {
                    ActionIcon(
                        onClick = {
                            isClose = true
                            onDelete(account.id!!)
                        },
                        icon = Icons.Outlined.Delete,
                        backgroundColor = Color.Red,
                        modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp) )
                    )
                    ActionIcon(
                        onClick = {
                            isClose = true
                            onFavourite(account.copy(isFavourite = !account.isFavourite))
                        },
                        icon = Icons.Outlined.Star,
                        backgroundColor = Color.Yellow,
                    )
                },
                onExpand = {
                    isClose = false
                },
                isClose = isClose,
                modifier = Modifier.padding(10.dp).animateItem()
            ) {
                HomeAccountCard(
                    account = account,
                    modifier = Modifier
                        .fillMaxSize(),
                    toDetails = { toDetails(account.id!!) },
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeAccountCard(
    account: AccountData,
    toDetails: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    suffix:String = "",
){
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val background =    if (account.isFavourite) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer


    Box(
        modifier = modifier.clickable { toDetails() }
            .background(background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .fillMaxWidth(),
        ) {
            with(sharedTransitionScope){
                AccountPreview(
                    account = account,
                    modifier = Modifier
                        .weight(1f)
                        .sharedElement(
                            rememberSharedContentState(key = "$suffix/${account.id.toString()}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }
            Spacer(Modifier.width(10.dp))
            IconButton(
                onClick = {
                    val clipData = ClipData.newPlainText("password", account.password)
                    val clipEntry = ClipEntry(clipData)
                    clipboardManager.setClip(clipEntry)
                    Toast.makeText(
                        context,
                        context.getString(R.string.password_copied),
                        Toast.LENGTH_LONG,
                    ).show()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.copy_image),
                    contentDescription = stringResource(R.string.copy),
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
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
fun HomeScreenContentPreview(){
    PasswordManagerTheme {
        SharedTransitionLayout {
        AnimatedVisibility(true) {
        HomeScreenContent(
            accountList = ExampleData.accountList,
            toDetails = {},
            sharedTransitionScope = this@SharedTransitionLayout,
            animatedVisibilityScope = this,
            onDelete = {},
            onFavourite = {}
        )}}
    }
}