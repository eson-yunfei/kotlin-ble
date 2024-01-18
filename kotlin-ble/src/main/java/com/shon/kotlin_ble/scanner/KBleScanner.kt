package com.shon.kotlin_ble.scanner

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log
import com.shon.kotlin_ble.core.toNative
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 *
 * @Author xiao
 * @Date 2023-10-18 16:24
 *
 */
internal class KBleScanner(private val bluetoothLeScanner: BluetoothLeScanner) {

    private var stopFlag = false


    fun stopScanner() {
        stopFlag = true
    }

    @SuppressLint("MissingPermission")
    fun startScan(
        times: Long = 15_000,
        scanFilters: List<BleScanFilter> = emptyList()
    ): Flow<List<ScanResult>> = callbackFlow {

        stopFlag = false
        val timeJob = scanTimerJob(times) { close() }
        listenerStopJob{
            timeJob?.cancel()
            close()
        }

        val scanCallback = object : ScanCallback() {
            override fun onScanFailed(errorCode: Int) {
                close()
            }
            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                results?.filter {
                    !it.device.name.isNullOrEmpty()
                }?.let {
                    trySend(it)
                }
            }
        }
        bluetoothLeScanner.startScan(scanFilters.toNative(), getScanSetting(), scanCallback)
        awaitClose {
            Log.d("TAG", "startScan: closed")
            stopFlag = true
            bluetoothLeScanner.stopScan(scanCallback)
        }
    }

    private fun CoroutineScope.scanTimerJob(times: Long, callback: () -> Unit): Job? {
        if (times <= 0) {
            return null
        }
        return launch {
            delay(times)
            callback.invoke()
        }
    }

    private fun CoroutineScope.listenerStopJob(callback: () -> Unit):Job{
        return launch {
            while (!stopFlag) { delay(10) }
            callback.invoke()
            cancel()
        }
    }

    private fun getScanSetting(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(1000)
            .setLegacy(false)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .build()
    }
}