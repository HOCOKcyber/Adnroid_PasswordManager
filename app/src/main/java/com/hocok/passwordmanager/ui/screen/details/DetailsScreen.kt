package com.hocok.passwordmanager.ui.screen.details

import android.content.ClipData
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.ui.component.AccountPreview
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsScreen(
    toChange: () -> Unit,
    accountId: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    suffix: String = "",
){
    val viewModel: DetailsViewModel =
        viewModel<DetailsViewModel>(factory = DetailsViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        key1 = true,
    ) {
        viewModel.fetchAccount(accountId)
    }

    DetailsContent(
        suffix = suffix,
        account = uiState,
        toChange = toChange,
        modifier = modifier.fillMaxSize(),
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsContent(
    account: AccountData,
    toChange: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    suffix: String = ""
){
    val clipManager = LocalClipboardManager.current
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 10.dp),
    ) {
        with(sharedTransitionScope){
            AccountPreview(
                account = account,
                imageSize = 80,
                modifier = Modifier.padding(horizontal = 10.dp)
                    .padding(bottom = 20.dp, top = 10.dp)
                    .sharedElement(
                        rememberSharedContentState(key = "$suffix/${account.id.toString()}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        DetailsSection(
            name = stringResource(R.string.domain),
            value = account.domain,
            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
        )
        DetailsSection(
            name = stringResource(R.string.login),
            value = account.login,
            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
        )
        DetailsSection(
            name = stringResource(R.string.password),
            value = account.password,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.weight(1f))
        StyleButton(
            onClick = {
                val clipData = ClipData.newPlainText("password", account.password)
                val clipEntry = ClipEntry(clipData)
                clipManager.setClip(clipEntry)
                Toast.makeText(
                    context,
                    context.getString(R.string.password_copied),
                    Toast.LENGTH_SHORT,
                ).show()
            },
            textRes = R.string.copy,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        StyleButton(
            onClick = toChange,
            textRes = R.string.change,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        StyleButton(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Login: ${account.login}\nPassword: ${account.password}\nDomain: ${account.domain}")
                    type = "text/plan"
                }
                context.startActivity(shareIntent, null)
            },
            textRes = R.string.share,
        )
    }

}


@Composable
fun DetailsSection(
    name: String,
    value: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier.weight(2f)
        ) {
            SelectionContainer{
                Text(
                    text = value,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
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
fun DetailsContentPreview(){
    PasswordManagerTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                DetailsContent(
                    account = AccountData(),
                    toChange = {},
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}