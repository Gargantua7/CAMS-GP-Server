package com.gargantua7.cams.gp.server

import java.util.*

/**
 * @author Gargantua7
 */
fun matchPassword(password: String) =
    "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9~!@#\$%^&*]{8,16}\$".toRegex().matches(password)

fun matchUsername(username: String): Boolean {
    if (!"^[0-9]{12}\$".toRegex().matches(username)) return false
    val year = Calendar.getInstance().get(Calendar.YEAR)
    return username.substring(0..3).toInt() <= year
}

fun matchPhone(phone: String?) =
    phone.isNullOrBlank() || "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}\$".toRegex().matches(phone)

fun matchWechat(wechat: String?) =
    wechat.isNullOrBlank() || "^[a-zA-Z][-_a-zA-Z0-9]{5,19}\$".toRegex().matches(wechat)
