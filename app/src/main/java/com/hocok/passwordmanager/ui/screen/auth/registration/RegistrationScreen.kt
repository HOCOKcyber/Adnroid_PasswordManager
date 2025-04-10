package com.hocok.passwordmanager.ui.screen.auth.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.components.AuthContent
import com.hocok.passwordmanager.ui.screen.auth.components.PasswordTextField
import com.hocok.passwordmanager.ui.screen.auth.components.ShowLogo
import com.hocok.passwordmanager.ui.screen.auth.login.LoginEvent
import com.hocok.passwordmanager.ui.screen.auth.login.LoginViewModel
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun RegistrationScreen(){

    val viewModel = viewModel<RegistrationViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        AuthContent(
            firstTextFieldIsVisible = uiState.value.isVisible,
            firstTextFieldOnVisibleChange = {viewModel.registrationEvent(RegistrationEvent.VisibleChange)},
            firstTextFieldValue = uiState.value.password,
            firstTextFieldOnValueChange = { viewModel.registrationEvent(RegistrationEvent.PasswordChange(it))},
            onContinueButton = {

            },
            title = R.string.create_password,
            modifier = Modifier.weight(2f)
        ){
            PasswordTextField(
                value = uiState.value.repeatPassword,
                onValueChange = {viewModel.registrationEvent(RegistrationEvent.RepeatPasswordChange(it))},
                isVisible = uiState.value.isRepeatVisible,
                onVisibleChange = {viewModel.registrationEvent(RegistrationEvent.RepeatVisibleChange)},
                text = R.string.password_repeat,
                modifier = Modifier.padding(top = 20.dp),
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview(){
    PasswordManagerTheme {
        RegistrationScreen()
    }
}