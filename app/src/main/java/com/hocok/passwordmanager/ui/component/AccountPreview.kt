package com.hocok.passwordmanager.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.domain.model.AccountData
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun AccountPreview(
    account: AccountData,
    modifier: Modifier = Modifier,
    imageSize: Int = 55,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.google.com/s2/favicons?domain=${account.domain}&sz=${256}")
                .crossfade(true)
                .build(),
            contentDescription = null,
            error = painterResource(R.drawable.error_image),
            onError = { Log.d("Image Error", it.toString())},
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(end = 20.dp)
                .size(imageSize.dp)
                .clip(CircleShape),
        )
        Column {
            Text(
                text = account.service,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = account.login,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountPreviewPreview(){
    PasswordManagerTheme {
        AccountPreview(
            account = AccountData(
                login = "mqwkpednqeodqedjeqjdjkqjedkjewqidjqed;k"
            )
        )
    }
}
