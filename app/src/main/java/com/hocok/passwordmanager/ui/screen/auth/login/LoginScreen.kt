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
import com.hocok.passwordmanager.ui.screen.auth.components.PasswordTextField
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.screen.auth.components.AuthContent
import com.hocok.passwordmanager.ui.screen.auth.components.ShowLogo
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(
    toRegistration: () -> Unit,
){

    val viewModel = viewModel<LoginViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        AuthContent(
            firstTextFieldIsVisible = uiState.value.isVisible,
            firstTextFieldOnVisibleChange = {viewModel.loginEvent(LoginEvent.ChangeVisible)},
            firstTextFieldValue = uiState.value.password,
            firstTextFieldOnValueChange = { viewModel.loginEvent(LoginEvent.ChangePassword(it))},
            onContinueButton = {},
            modifier = Modifier.weight(2f)
        ){
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    PasswordManagerTheme {
        LoginScreen(
            toRegistration = {}
        )
    }
}
