package com.shon.kotlin_ble.core

import java.util.Locale

/**
 *
 * @Author xiao
 * @Date 2023-10-20 11:43
 *
 */

private const val HEX_CHARS: String = "0123456789ABCDEF"

private fun toByte(c: Char): Byte {
    return HEX_CHARS.indexOf(c).toByte()
}


fun ByteArray.toDisplayString(): String {
    return "(0x) " + this.joinToString(":") {
        "%02x".format(it).uppercase()
    }
}

fun ByteArray.toHexString(): String {
    return this.joinToString("") {
        "%02x".format(it).uppercase()
    }
}

fun String.hexToByteArray(): ByteArray {
    val uppercase = this.trim().uppercase(Locale.getDefault())
    val len = uppercase.length / 2
    val hexChars = uppercase.toCharArray()
    val result = ByteArray(len)
    for (i in 0 until len) {
        val pos = i * 2
        result[i] = (toByte(hexChars[pos]).toInt() shl 4 or toByte(hexChars[pos + 1]).toInt()).toByte()
    }
    return result
}
