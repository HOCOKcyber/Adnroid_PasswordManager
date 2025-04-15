package com.hocok.passwordmanager.ui.screen.details

import android.telecom.Call.Details
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.ui.component.AccountPreview
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun DetailsScreen(
    toChange: () -> Unit,
    accountId: Int,
    modifier: Modifier = Modifier,
){
    val viewModel = viewModel<DetailsViewModel>(factory = DetailsViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        key1 = true,
    ) {
        viewModel.fetchAccount(accountId)
    }

    DetailsContent(
        account = uiState,
        toChange = toChange,
        modifier = modifier
    )
}

@Composable
fun DetailsContent(
    account: AccountData,
    toChange: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.padding(horizontal = 10.dp),
    ) {
        AccountPreview(
            account = account,
            imageSize = 80,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
        )
        Text(
            text = stringResource(R.string.details),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 25.dp).fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
        )
        Spacer(Modifier.weight(1f))
        StyleButton(
            onClick = {},
            textRes = R.string.copy,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        StyleButton(
            onClick = toChange,
            textRes = R.string.change,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        StyleButton(
            onClick = {},
            textRes = R.string.share,
            modifier = Modifier.padding(bottom = 10.dp)
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
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun DetailsContentPreview(){
    PasswordManagerTheme {
        DetailsContent(
            account = AccountData(),
            toChange = {}
        )
    }
}