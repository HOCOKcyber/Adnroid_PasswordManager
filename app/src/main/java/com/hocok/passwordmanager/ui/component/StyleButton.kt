package com.hocok.passwordmanager.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StyleButton(
    onClick:() -> Unit,
    @StringRes textRes: Int,
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
        ) {
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}