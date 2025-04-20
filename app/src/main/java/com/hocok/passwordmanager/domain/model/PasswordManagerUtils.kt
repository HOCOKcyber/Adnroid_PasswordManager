package com.hocok.passwordmanager.domain.model

object PasswordManagerUtils {
    const val SHIFT = 10

    private const val LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBERS = "0123456789"
    private const val SYMBOLS = "!@#$%^&*?_"
    private const val UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

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

    fun createRandomPassword(
        length: Int,
        isNumber: Boolean = false,
        isSymbols: Boolean = false,
        isUpperLetter: Boolean = false
    ): String {
        var newPassword = ""
        val alphabetList = mutableListOf(LOWER_LETTERS)
        if (isNumber) alphabetList.add(NUMBERS)
        if (isSymbols) alphabetList.add(SYMBOLS)
        if (isUpperLetter) alphabetList.add(UPPER_LETTERS)

        for (i in 1..length){
            val alphabet = alphabetList.random()
            newPassword += alphabet.random()
        }

        // isNumber == true and нет чисел в newPassword
        if (isNumber &&
            newPassword.split("").any{ it in NUMBERS }) newPassword += NUMBERS.random()
        if (isSymbols &&
            newPassword.split("").any{ it in SYMBOLS }) newPassword += SYMBOLS.random()
        if (isUpperLetter &&
            newPassword.split("").any{ it in UPPER_LETTERS }) newPassword += UPPER_LETTERS.random()

        return newPassword
    }

    fun createMaskPassword(mask: String): String{
        var newPassword = ""

        for (symbol in mask){
            newPassword += when(symbol){
                'n' -> NUMBERS.random()
                'l' -> LOWER_LETTERS.random()
                'L' -> UPPER_LETTERS.random()
                's' -> SYMBOLS.random()
                else -> return ""
            }
        }
        return newPassword
    }
}