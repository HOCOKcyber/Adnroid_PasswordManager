package com.hocok.passwordmanager.ui.screen.auth.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.component.StyleButton

@Composable
fun AuthContent(
    onContinueButton: () -> Unit,
    modifier: Modifier = Modifier,
    information: String = "",
    @StringRes title: Int = R.string.welcome,
    content: @Composable () -> Unit = {},
){

    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        Column (
            modifier = Modifier.fillMaxSize().padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            if (information.isNotEmpty()) Text(text = information, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.weight(1f))

//            HERE LOGIN CONTENT
//            OR REG CONTETN
            content()


            Spacer(Modifier.weight(1.5f))
            StyleButton(
                onClick = {
                    onContinueButton()
                },
                textRes = R.string.continue_text
            )
        }
    }
}