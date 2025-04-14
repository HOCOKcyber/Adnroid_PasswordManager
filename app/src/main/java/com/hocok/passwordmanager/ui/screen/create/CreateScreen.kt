package com.hocok.passwordmanager.ui.screen.create

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun CreateScreen(
    paddingValue: PaddingValues
){
    val viewModel: CreateViewModel = viewModel<CreateViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    CreateContent(
        uiState = uiState,
        onSymbolsChange = {viewModel.onEvent(CreateEvent.CheckedSymbols)},
        onNumbersChange = {viewModel.onEvent(CreateEvent.CheckedNumbers)},
        onUpperLetterChange = {viewModel.onEvent(CreateEvent.CheckedUpperLetter)},
        onLengthChange = {viewModel.onEvent(CreateEvent.ChangeLength(it))},
        onPasswordChange = {viewModel.onEvent(CreateEvent.ChangePassword(it))},
        onDomainChange = {viewModel.onEvent(CreateEvent.ChangeDomain(it))},
        onServiceChange = {viewModel.onEvent(CreateEvent.ChangeService(it))},
        onLoginChange = {viewModel.onEvent(CreateEvent.ChangeLogin(it))},
        onSliderChange = {viewModel.onEvent(CreateEvent.ChangeSlider(it))},
        generatePassword = {viewModel.onEvent(CreateEvent.GeneratePassword)},
        checkLength = {viewModel.onEvent(CreateEvent.CheckLength)},
        modifier = Modifier.padding(paddingValue).padding(horizontal = 10.dp)
    )
}


@Composable
fun CreateContent(
    uiState: CreateState,
    onLoginChange: (String) -> Unit,
    onServiceChange: (String) -> Unit,
    onDomainChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLengthChange: (String) -> Unit,
    onSliderChange: (Float) -> Unit,
    onNumbersChange: (Boolean) -> Unit,
    onSymbolsChange: (Boolean) -> Unit,
    onUpperLetterChange: (Boolean) -> Unit,
    generatePassword: () -> Unit,
    checkLength: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        AccountDataInputSection(
            name = stringResource(R.string.login),
            value = uiState.login,
            onValueChange = onLoginChange,
            placeholder = stringResource(R.string.login_placeholder),
            modifier = Modifier.padding(bottom = 20.dp),
            iconId = R.drawable.person_image
        )
        AccountDataInputSection(
            name = stringResource(R.string.service),
            value = uiState.service,
            onValueChange = onServiceChange,
            placeholder = stringResource(R.string.service_placeholder),
            iconId = R.drawable.article_image,
            modifier = Modifier.padding(bottom = 20.dp),
        )
        AccountDataInputSection(
            name = stringResource(R.string.domain),
            value = uiState.domain,
            onValueChange = onDomainChange,
            placeholder = stringResource(R.string.domain_placeholder),
            iconId = R.drawable.link_image,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        AccountDataInputSection(
            name = stringResource(R.string.password),
            placeholder = stringResource(R.string.password_placeholder),
            value = uiState.password,
            onValueChange = onPasswordChange,
            iconId = R.drawable.refresh_image,
            isLeadingIcon = false,
            onClick = generatePassword,
            modifier = Modifier.padding(20.dp)
        )
        ParametersSection(
            modifier = Modifier.padding(20.dp),
            isSymbols = uiState.isSymbols,
            isNumbers = uiState.isNumbers,
            isUpperLetter = uiState.isUpperLetter,
            lengthValue = uiState.length,
            onLengthChange = onLengthChange,
            onNumbersChange = onNumbersChange,
            onSymbolsChange = onSymbolsChange,
            onUpperLetterChange = onUpperLetterChange,
            onSliderChange = onSliderChange,
            checkLength = checkLength,
        )
        StyleButton(
            onClick = {},
            textRes = R.string.save,
        )
    }
}

@Composable
fun AccountDataInputSection(
    name: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    isLeadingIcon: Boolean = true,
    onClick: () -> Unit = {},
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            textAlign = if (isLeadingIcon) TextAlign.Start else TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = if (isLeadingIcon) { -> Icon(painterResource(iconId), contentDescription = name)}
                        else null,
            trailingIcon = if (!isLeadingIcon) { ->
                        IconButton(onClick = onClick)
                        { Icon(painterResource(iconId), contentDescription = name) }
                        } else null
        )
    }
}

@Composable
fun ParametersSection(
    lengthValue: String,
    onLengthChange: (String) -> Unit,
    onNumbersChange: (Boolean) -> Unit,
    isNumbers: Boolean,
    onSymbolsChange: (Boolean) -> Unit,
    isSymbols: Boolean,
    onUpperLetterChange: (Boolean) -> Unit,
    isUpperLetter: Boolean,
    onSliderChange: (Float) -> Unit,
    checkLength: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.lenght),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
            BasicTextField(
                value = lengthValue,
                onValueChange = onLengthChange,
                textStyle = MaterialTheme.typography.bodySmall,
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        checkLength()
                        defaultKeyboardAction(ImeAction.Done)
                    }
                )
            ){ textField ->
                Box(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.outline,RoundedCornerShape(5.dp))
                    .padding(vertical = 5.dp, horizontal = 5.dp)
                    .width(50.dp)
                ){
                    textField()
                }
            }
        }
        Slider(
            value = try { lengthValue.toFloat()} catch (e: NumberFormatException) {8f},
            onValueChange = onSliderChange,
            steps = 12,
            valueRange = 8f..20f
        )
        Row {
            CheckedSection(
                name = stringResource(R.string.numbers),
                onCheckedChange = onNumbersChange,
                value = isNumbers,
                modifier = Modifier.weight(1f)
            )
            CheckedSection(
                name = stringResource(R.string.symbols),
                onCheckedChange = onSymbolsChange,
                value = isSymbols,
                modifier = Modifier.weight(1f)
            )
        }
        CheckedSection(
            name = stringResource(R.string.upper_letters),
            onCheckedChange = onUpperLetterChange,
            value = isUpperLetter,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CheckedSection(
    value: Boolean,
    name: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(end = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp
        )
        Checkbox(
            checked = value,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun CreateContentPreview(){
    PasswordManagerTheme {
        CreateContent(
            uiState = CreateState(),
            onSymbolsChange = {},
            onNumbersChange = {},
            onUpperLetterChange = {},
            onLengthChange = {},
            onPasswordChange = {},
            onDomainChange = {},
            onServiceChange = {},
            onLoginChange = {},
            onSliderChange = {},
            generatePassword = {},
            checkLength = {}
        )
    }
}