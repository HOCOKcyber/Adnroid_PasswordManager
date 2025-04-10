package com.hocok.passwordmanager.ui.screen.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.login.components.PasswordTextField
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.screen.auth.login.components.ShowLogo
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(){

    val viewModel = viewModel<LoginViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        Box(
            Modifier.weight(2f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ){
            Column (
                modifier = Modifier.fillMaxSize().padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.weight(1f))
                PasswordTextField(
                    value = uiState.value.password,
                    onValueChange = {viewModel.loginEvent(LoginEvent.ChangePassword(it))},
                    isVisible = uiState.value.isVisible,
                    onVisibleChange = { viewModel.loginEvent(LoginEvent.ChangeVisible) },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = stringResource(R.string.forgot_password),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null) {
                            /* TO REG */
                        }
                )
                Spacer(Modifier.weight(1.5f))
                StyleButton(
                    onClick = {
                        /* TO HOME */
                    },
                    textRes = R.string.continue_text
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    PasswordManagerTheme {
        LoginScreen()
    }
}
