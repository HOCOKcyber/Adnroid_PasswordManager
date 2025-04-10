package com.hocok.passwordmanager.ui.screen.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hocok.passwordmanager.R

@Composable
fun ShowLogo(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "password manager logo",
            modifier = Modifier.size(160.dp).align(Alignment.Center)
        )
    }
}