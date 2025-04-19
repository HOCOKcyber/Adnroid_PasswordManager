package com.hocok.passwordmanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hocok.passwordmanager.R

val monstserrat = FontFamily(
    Font(R.font.montserrat_light, weight = FontWeight.Light),
    Font(R.font.montserrat_medium, weight = FontWeight.Medium),
    Font(R.font.montserrat_bold, weight = FontWeight.Bold),
)


val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = monstserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = monstserrat,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = monstserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = monstserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),

)