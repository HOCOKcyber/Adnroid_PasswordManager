package com.hocok.passwordmanager.ui.screen.auth.login.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onVisibleChange: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = MaterialTheme.typography.bodySmall,
        placeholder = {
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onVisibleChange
            ) {
                Icon(
                    painter = painterResource(if (isVisible) R.drawable.visible_password else R.drawable.not_visible_password),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        singleLine = true,
        maxLines = 1,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    )

}

@Preview
@Composable
fun PasswordTextFieldPreview(){
    PasswordManagerTheme {
        PasswordTextField(
            isVisible = false,
            value = "",
            onValueChange = {},
            onVisibleChange = {}
        )
    }
}