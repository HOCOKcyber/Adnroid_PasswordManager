package com.hocok.passwordmanager.ui.screen.auth.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.components.AuthContent
import com.hocok.passwordmanager.ui.screen.auth.components.PasswordTextField
import com.hocok.passwordmanager.ui.screen.auth.components.ShowLogo
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(
    toRegistration: (information: String) -> Unit,
    toHome: () -> Unit,
){
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.checkFirstTime{
            toRegistration(context.getString(R.string.create_password))
        }
    }

    LoginScreenContent(
        toRegistration = {toRegistration(context.getString(R.string.create_new_password))},
        onSubmit = {viewModel.onEvent(LoginEvent.Submit(toHome))},
        onValueChange = { viewModel.onEvent(LoginEvent.ChangePassword(it)) },
        onVisibleChange = { viewModel.onEvent(LoginEvent.ChangeVisible) },
        uiState = uiState,
        isVisible = uiState.isFirstTime == LoginFirstTime.NotFirstTime
    )

}


@Composable
fun LoginScreenContent(
    toRegistration: () -> Unit,
    onSubmit: () -> Unit,
    onValueChange: (String) -> Unit,
    onVisibleChange: () -> Unit,
    uiState: LoginState,
    isVisible: Boolean,
){
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        AnimatedVisibility(
            isVisible,
            enter = slideIn(initialOffset = { IntOffset(0, it.height) }),
            modifier = Modifier.weight(2f)
        ) {
            AuthContent(
                onContinueButton = onSubmit,
                modifier = Modifier
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
                    color = MaterialTheme.colorScheme.secondary,
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
}


@Preview(
    showBackground = true,
    apiLevel = 34,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    apiLevel = 34,
    showBackground = true
)
@Preview(
    showBackground = true,
    widthDp = 355
)
@Composable
fun LoginScreenPreview(){
    PasswordManagerTheme  {
        LoginScreenContent(
            toRegistration = {},
            onSubmit = {},
            onVisibleChange = {},
            onValueChange = {},
            uiState = LoginState(),
            isVisible = true
        )
    }
}
