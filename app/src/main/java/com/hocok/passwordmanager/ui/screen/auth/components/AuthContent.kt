package com.hocok.passwordmanager.ui.screen.auth.components

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.hocok.passwordmanager.R
import com.hocok.passwordmanager.ui.component.StyleButton
import com.hocok.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun AuthContent(
    onContinueButton: () -> Unit,
    modifier: Modifier = Modifier,
    information: String = "",
    @StringRes title: Int = R.string.welcome,
    content: @Composable () -> Unit = {},
){
    val context = LocalContext.current
    val correctLanguage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales[0]?.toLanguageTag()?.split("-")?.first() ?: ""
    } else {
        AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first() ?: ""
    }
    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        Column (
            modifier = Modifier.fillMaxSize().padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            if (information.isNotEmpty()) Text(text = information, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(bottom = 10.dp))
            IconButton(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(if(correctLanguage == "ru") "en" else "ru")
                    } else {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(if (correctLanguage == "ru") "en" else "ru"))
                    }
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    painterResource(R.drawable.language),
                    contentDescription = stringResource(R.string.change_language),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.weight(1f))

//            HERE LOGIN CONTENT
//            OR REG CONTETN
            content()


            Spacer(Modifier.weight(1.5f))
            StyleButton(
                onClick = {
                    onContinueButton()
                },
                textRes = R.string.continue_text
            )
        }
    }
}

@Preview(
    locale = "en"
)
@Preview(
    locale = "ru"
)
@Composable
fun AuthContentPreview(){
    PasswordManagerTheme {
        AuthContent(
            onContinueButton = {},
            information = stringResource(R.string.create_new_password),
            title = R.string.registration,
        )
    }
}