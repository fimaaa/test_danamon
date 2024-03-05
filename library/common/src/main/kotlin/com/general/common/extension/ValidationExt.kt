package com.general.common.extension

import android.util.Patterns
import java.util.regex.Pattern

fun String.isFullNameValid(): Boolean {
    val p = Pattern.compile("^[a-z A-Z'-]{0,30}\$")
    return p.matcher(this).matches()
}

fun String.isMobileNumberValid(): Boolean {
    val p = Pattern.compile("^((?:\\+62|62)|0)8\\d{6,12}\$")
    return p.matcher(this).matches()
}

fun String.isEmailValid(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordValid(): Boolean = this.isNotEmpty() && this.length >= 8 &&
        (Pattern.compile("^(?=.*[a-z A-Z])(?=.*[A-Z])(?=.*\\d)[a-z A-Z\\d\$&+,:;=\\\\?@#|/'<>.^*()%!-]+$")
            .matcher(this).matches())

fun String.isPasswordTmoneyValid(): Boolean = this.isNotEmpty() && this.length >= 8 &&
        (Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d).{8,}\$")
            .matcher(this).matches())

fun String.maskPhoneNumber(): String {
    return "••••••••" + this.takeLast(4)
}

fun String.isPassportValid(): Boolean {
    val p = Pattern.compile("^(?!^0+\$)[A-Z]\\d{7}\$")
    return p.matcher(this).matches()
}

fun String.isKtpValid(): Boolean {
    val p = Pattern.compile("^\\d{16}\$")
    return p.matcher(this).matches()
}

fun String.isSimValid(): Boolean {
    val p = Pattern.compile("^\\d{12}\$")
    return p.matcher(this).matches()
}

fun String.isUrl(): Boolean {
    val pattern = Pattern.compile("^(http|https)://.*")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}