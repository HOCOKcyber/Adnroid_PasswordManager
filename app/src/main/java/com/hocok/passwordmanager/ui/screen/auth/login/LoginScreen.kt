package com.hocok.passwordmanager.ui.screen.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.components.AuthContent
import com.hocok.passwordmanager.ui.screen.auth.components.PasswordTextField
import com.hocok.passwordmanager.ui.screen.auth.components.ShowLogo
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(
    toRegistration: () -> Unit,
    toHome: () -> Unit,
){
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.isFirstEnter {
            toRegistration()
        }
    }

    LoginScreenContent(
        toRegistration = toRegistration,
        onSubmit = {viewModel.onEvent(LoginEvent.Submit(toHome))},
        onValueChange = { viewModel.onEvent(LoginEvent.ChangePassword(it)) },
        onVisibleChange = { viewModel.onEvent(LoginEvent.ChangeVisible) },
        uiState = uiState
    )
}

@Composable
fun LoginScreenContent(
    toRegistration: () -> Unit,
    onSubmit: () -> Unit,
    onValueChange: (String) -> Unit,
    onVisibleChange: () -> Unit,
    uiState: LoginState
){
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        AuthContent(
            onContinueButton = onSubmit,
            modifier = Modifier.weight(2f)
        ){
            PasswordTextField(
                value = uiState.password,
                onValueChange = { onValueChange(it) },
                isVisible = uiState.isVisible,
                onVisibleChange = onVisibleChange,
                errorMessage = uiState.passwordError
            )
            Text(
                text = stringResource(R.string.forgot_password),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 10.dp, end = 6.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null) {
                        toRegistration()
                    }
            )
        }
    }
}

@Preview(
    apiLevel = 34,
    showBackground = true
)
@Composable
fun LoginScreenPreview(){
    PasswordManagerTheme {
        LoginScreenContent(
            toRegistration = {},
            onSubmit = {},
            onVisibleChange = {},
            onValueChange = {},
            uiState = LoginState()
        )
    }
}
