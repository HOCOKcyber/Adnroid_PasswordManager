package com.hocok.passwordmanager.ui.screen.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.domain.model.ExampleData
import com.hocok.passwordmanager.ui.component.AccountPreview
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun HomeScreen(){

}

@Composable
fun HomeScreenContent(
    accountList: List<AccountData>,
    toDetails: () -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
    ) {
        items(accountList){
            HomeAccountCard(
                account = it,
                modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                toDetails = toDetails
            )
        }

    }
}

@Composable
fun HomeAccountCard(
    account: AccountData,
    toDetails: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.clickable { toDetails() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp).fillMaxWidth(),
        ) {
            AccountPreview(account = account)
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {
//                    COPY
                },
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenContentPreview(){
    PasswordManagerTheme {
        HomeScreenContent(
            accountList = ExampleData.accountList,
            toDetails = {}
        )
    }
}