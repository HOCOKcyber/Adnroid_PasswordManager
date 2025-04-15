package com.hocok.passwordmanager.domain.model

object PasswordManagerUtils {
    const val SHIFT = 10

    fun crypto(password: String, shift: Int): String{
        return password.map { char ->
            when {
                char.isLetter() -> {
                    val base = if (char.isUpperCase()) 'A' else 'a'
                    val shiftedChar = ((char.code - base.code + shift) % 26 + 26) % 26 + base.code
                    shiftedChar.toChar()
                }
                else -> char
            }
        }.joinToString("")
    }
}