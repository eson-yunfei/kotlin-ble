package com.shon.kb.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 *
 * @Author xiao
 * @Date 2023-10-18 14:34
 *
 */


fun Context.hasPermission(permission: String): Boolean {
    val result = ContextCompat.checkSelfPermission(this, permission)
    return PackageManager.PERMISSION_GRANTED == result
}

fun Context.hasPermissions(
    vararg permissions: String,
    checkResult: (granted: Boolean, needRequest: List<String>?) -> Unit
) {

    val needRequest = mutableListOf<String>()
    permissions.forEach { permission ->
        val hasPermission = hasPermission(permission)
        if (!hasPermission) {
            needRequest.add(permission)
        }
    }

    if (needRequest.isEmpty()) {
        checkResult(true, null)
    } else {
        checkResult(false, needRequest)
    }
}
