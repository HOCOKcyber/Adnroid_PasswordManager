package com.hocok.passwordmanager.ui.screen.create

import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    toHome: () -> Unit,
    id: Int? = null,
){
    val viewModel: CreateViewModel = viewModel<CreateViewModel>(factory = CreateViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (id != null) LaunchedEffect(key1 = true) { viewModel.fetchAccount(id)}

    CreateContent(
        uiState = uiState,
        onSymbolsChange = {viewModel.onEvent(CreateEvent.CheckedSymbols)},
        onNumbersChange = {viewModel.onEvent(CreateEvent.CheckedNumbers)},
        onUpperLetterChange = {viewModel.onEvent(CreateEvent.CheckedUpperLetter)},
        onLengthChange = {viewModel.onEvent(CreateEvent.ChangeLength(it))},
        onPasswordChange = {viewModel.onEvent(CreateEvent.ChangePassword(it))},
        onMaskChange = {viewModel.onEvent(CreateEvent.ChangeMask(it))},
        onDomainChange = {viewModel.onEvent(CreateEvent.ChangeDomain(it))},
        onServiceChange = {viewModel.onEvent(CreateEvent.ChangeService(it))},
        onLoginChange = {viewModel.onEvent(CreateEvent.ChangeLogin(it))},
        onSliderChange = {viewModel.onEvent(CreateEvent.ChangeSlider(it))},
        generateRandomPassword = {viewModel.onEvent(CreateEvent.GenerateRandomPassword)},
        checkLength = {viewModel.onEvent(CreateEvent.CheckLength)},
        onSave = {viewModel.onEvent(CreateEvent.OnSave)},
        toHome = toHome,
        generateMaskPassword = {viewModel.onEvent(CreateEvent.GenerateMaskPassword(context))},
        onChangePasswordMethod = {viewModel.onEvent(CreateEvent.ChangePasswordMethod)},
        modifier = modifier.padding(horizontal = 10.dp).fillMaxSize().verticalScroll(rememberScrollState())
    )
}


@Composable
fun CreateContent(
    uiState: CreateState,
    toHome: () -> Unit,
    onLoginChange: (String) -> Unit,
    onServiceChange: (String) -> Unit,
    onDomainChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onMaskChange: (String) -> Unit,
    onLengthChange: (String) -> Unit,
    onSliderChange: (Float) -> Unit,
    onNumbersChange: (Boolean) -> Unit,
    onSymbolsChange: (Boolean) -> Unit,
    onUpperLetterChange: (Boolean) -> Unit,
    generateRandomPassword: () -> Unit,
    generateMaskPassword: () -> Unit,
    onChangePasswordMethod: () -> Unit,
    checkLength: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = onChangePasswordMethod,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.change_password_method),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        AnimatedVisibility(
            uiState.isRandomPassword
        ) {
            Column {
                PasswordCreator(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    placeholder = stringResource(R.string.password_input),
                    onClick = generateRandomPassword
                )
                RandomParametersSection(
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
            }
        }
        AnimatedVisibility(!uiState.isRandomPassword) {
            Column {
                PasswordCreator(
                    value = uiState.mask,
                    onValueChange = onMaskChange,
                    placeholder = stringResource(R.string.password_mask_input),
                    onClick = generateMaskPassword
                )
                MaskParametersSection(
                    textInformation = when{
                        uiState.maskError.isNotEmpty() -> uiState.maskError
                        uiState.password.isNotEmpty() -> uiState.password
                        else -> stringResource(R.string.here_password)
                    },
                    textInformationColor =  if (uiState.maskError.isNotEmpty()) MaterialTheme.colorScheme.error
                                            else MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.weight(1f))
        StyleButton(
            onClick = {
                coroutineScope.launch {
                    if (uiState.domain.isEmpty() ||
                        uiState.login.isEmpty() ||
                        uiState.password.isEmpty() ||
                        uiState.service.isEmpty()) {
                         Toast.makeText(context, context.getString(R.string.fill_all_input), Toast.LENGTH_SHORT).show()
                    } else {
                        onSave()
                        toHome()
                    }
                }
            },
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
){
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = { Icon(painterResource(iconId), contentDescription = name)},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = Color.Transparent,
            )
        )

}

@Composable
fun PasswordCreator(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = modifier.fillMaxWidth().padding(20.dp),
        trailingIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    painterResource(R.drawable.refresh_image),
                    contentDescription = stringResource(R.string.create_password))
            }},
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = Color.Transparent,
        )
    )
}

@Composable
fun RandomParametersSection(
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
                color = MaterialTheme.colorScheme.primary,
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
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Checkbox(
            checked = value,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun MaskParametersSection(
    textInformation: String,
    textInformationColor: Color = MaterialTheme.colorScheme.primary,
){
    Box{
        SelectionContainer{
            Text(
                text = textInformation,
                style = MaterialTheme.typography.bodyMedium,
                color = textInformationColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )
        }
    }
    Text(
        text = stringResource(R.string.mask_instruction),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
    )

        Text(
            text = stringResource(R.string.lower_letter) + " - l",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        Text(
            text = stringResource(R.string.upper_letters) + " - L",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.numbers) + " - n",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(R.string.symbols) + " - s",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Preview(
    showBackground = true,
    widthDp = 355
)
@Composable
fun CreateContentPreview(){
    PasswordManagerTheme() {
        CreateContent(
            uiState = CreateState().copy(isRandomPassword = false, password = "12345wdwqqd"),
            onSymbolsChange = {},
            onNumbersChange = {},
            onUpperLetterChange = {},
            onLengthChange = {},
            onPasswordChange = {},
            onDomainChange = {},
            onServiceChange = {},
            onLoginChange = {},
            onSliderChange = {},
            generateRandomPassword = {},
            checkLength = {},
            onSave = {},
            toHome = {},
            onMaskChange = {},
            onChangePasswordMethod = {},
            generateMaskPassword = {}
        )
    }
}