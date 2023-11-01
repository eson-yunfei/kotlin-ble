package com.shon.kotlin_ble.scanner

import android.os.ParcelUuid

/**
 *
 * @Author xiao
 * @Date 2023-10-20 15:18
 *
 */
data class BleScanFilter(
    val deviceAddress: String? = null, val deviceName: String? = null,
    val serviceUuid: ParcelUuid? = null,
)
