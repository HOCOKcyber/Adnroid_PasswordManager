package com.hocok.passwordmanager.ui.screen.auth.registration

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.screen.auth.components.AuthContent
import com.hocok.passwordmanager.ui.screen.auth.components.PasswordTextField
import com.hocok.passwordmanager.ui.screen.auth.components.ShowLogo
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun RegistrationScreen(
    toHome : () -> Unit,
    information: String = stringResource(R.string.create_password)
){
    val viewModel = viewModel<RegistrationViewModel>(factory = RegistrationViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    RegistrationScreenContent(
        uiState = uiState,
        onFirstValueChange = {viewModel.onEvent(RegistrationEvent.PasswordChange(it))},
        onFirstVisibleChange = {viewModel.onEvent(RegistrationEvent.VisibleChange)},
        onRepeatValueChange = {viewModel.onEvent(RegistrationEvent.RepeatPasswordChange(it))},
        onRepeatVisibleChange = {viewModel.onEvent(RegistrationEvent.RepeatVisibleChange)},
        information = information,
        onSubmit = {
            if (uiState.password.length < 8) Toast.makeText(context, "Пароль должен быть не менее 8 символов",Toast.LENGTH_SHORT).show()
            else viewModel.onEvent(RegistrationEvent.Submit(toHome))
                   },
    )
}


@Composable
fun RegistrationScreenContent(
    uiState: RegistrationState,
    onSubmit: () -> Unit,
    onFirstValueChange: (String) -> Unit,
    onRepeatValueChange: (String) -> Unit,
    onFirstVisibleChange: () -> Unit,
    onRepeatVisibleChange: () -> Unit,
    information: String,
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowLogo(
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        AuthContent(
            onContinueButton = onSubmit,
            title = R.string.registration,
            information = information,
            modifier = Modifier.weight(2f)
        ){
            PasswordTextField(
                value = uiState.password,
                onValueChange = {onFirstValueChange(it)},
                isVisible = uiState.isVisible,
                onVisibleChange = onFirstVisibleChange,
                modifier = Modifier.padding(bottom = 20.dp),
                errorMessage = uiState.errorMessage
            )
            PasswordTextField(
                value = uiState.repeatPassword,
                onValueChange = {onRepeatValueChange(it)},
                isVisible = uiState.isRepeatVisible,
                onVisibleChange = onRepeatVisibleChange,
                text = R.string.password_repeat,
                errorMessage = uiState.repeatErrorMessage
            )
        }
    }
}


@Preview(
    showBackground = true,
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
    )
@Preview(
    showBackground = true,
    widthDp = 355
)
@Composable
fun RegistrationScreenPreview(){
    PasswordManagerTheme(
    ) {
        RegistrationScreenContent(
            uiState = RegistrationState(),
            onSubmit = {},
            onFirstValueChange = {},
            onFirstVisibleChange = {},
            onRepeatValueChange = {},
            onRepeatVisibleChange = {},
            information = stringResource(R.string.create_password)
        )
    }
}