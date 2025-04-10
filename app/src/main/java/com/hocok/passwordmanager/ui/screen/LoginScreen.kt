package com.hocok.passwordmanager.ui.screen

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(){
    Card {
        Text(
            text = "Test Theme",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    PasswordManagerTheme {
        LoginScreen()
    }
}
