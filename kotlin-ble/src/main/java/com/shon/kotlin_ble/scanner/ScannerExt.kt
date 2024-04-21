package com.shon.kotlin_ble.scanner

import android.bluetooth.le.ScanResult
import com.shon.kotlin_ble.KBleManager

private var kBleScanner: KBleScanner? = null
fun scanDevices(
    time: Long = 20_000,
    scanFilters: List<BleScanFilter> = emptyList(),
    scanCallback: (List<ScanResult>) -> Unit
) {
    if (kBleScanner == null) {
        val nativeManager = KBleManager.instance.getNativeManager()
        kBleScanner = KBleScanner(nativeManager.adapter.bluetoothLeScanner)
    }
    kBleScanner?.startScanner(time, scanFilters, scanCallback)
}

fun stopScanDevice() {
    kBleScanner?.stopScanner()
}