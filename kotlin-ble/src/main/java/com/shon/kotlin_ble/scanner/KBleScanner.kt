package com.shon.kotlin_ble.scanner

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.text.TextUtils
import android.util.Log
import com.shon.kotlin_ble.core.KBLEScope
import com.shon.kotlin_ble.core.toNative
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *
 * @Author xiao
 * @Date 2023-10-18 16:24
 *
 */
internal class KBleScanner(private val bluetoothLeScanner: BluetoothLeScanner) {

    private var scanRunning = false
    private var scanFlow: Flow<List<ScanResult>>? = null

    fun startScanner(
        time: Long,
        scanFilters: List<BleScanFilter>,
        scanCallback: (List<ScanResult>) -> Unit
    ) {
        Log.d("KBleScanner", "startScanner() : scanRunning = $scanRunning")
        if (scanRunning) return
        scanRunning = true
        scanFlow = createScanFlow(time, scanFilters)
        scanFlow?.onEach { scanCallback(it) }?.launchIn(KBLEScope)
    }

    fun stopScanner() {
        Log.d("KBleScanner", "stopScanner() ")
        scanRunning = false
    }

    @SuppressLint("MissingPermission")
    private fun createScanFlow(
        times: Long,
        scanFilters: List<BleScanFilter>
    ): Flow<List<ScanResult>> =
        callbackFlow {

            val timeJob = scanTimerJob(times) { close() }
            listenerStopJob {
                timeJob?.cancel()
                close()
            }

            val scanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    Log.d("KBleScanner", "onScanResult  =  $result")
                }
                override fun onScanFailed(errorCode: Int) { close() }

                override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                    results?.filter {
                        val deviceName = it.device.name
                        if (TextUtils.isEmpty(deviceName)){
                            false
                        }else {
                            Log.d("KBleScanner", "device =  $it")
                            true
                        }
                    }?.let {
                        trySend(it)
                    }
                }
            }
            Log.d("KBleScanner", "scan flow created")
            bluetoothLeScanner.startScan(scanFilters.toNative(), getScanSetting(), scanCallback)

            awaitClose {
                scanRunning = false
                bluetoothLeScanner.stopScan(scanCallback)
                scanFlow = null
                Log.d("KBleScanner", "scan flow closed ,stop scanner ")
            }
        }

    private fun CoroutineScope.scanTimerJob(times: Long, callback: () -> Unit): Job? {
        if (times <= 0) {
            return null
        }
        return launch {
            delay(times)
            Log.d("KBleScanner", "scanTimerJob finished , try close scanner ")
            callback.invoke()
        }
    }

    private fun CoroutineScope.listenerStopJob(callback: () -> Unit): Job {
        return launch {
            while (scanRunning) {
                delay(10)
            }
            Log.d("KBleScanner", "scanRunning changed , listenerStopJob finished , try close scanner ")
            callback.invoke()
            cancel()
        }
    }

    private fun getScanSetting(): ScanSettings {
        return ScanSettings.Builder()
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(1000)
//            .setLegacy(false)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .build()
    }
}