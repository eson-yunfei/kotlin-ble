package com.shon.kotlin_ble.api

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.shon.kotlin_ble.client.ClientGattEvent
import com.shon.kotlin_ble.core.KBLEScope
import com.shon.kotlin_ble.core.data.BleGattOperationStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * @Author xiao
 * @Date 2023-10-20 17:00
 *
 */
class DiscoverServicesApi(gatt: BluetoothGatt) : GattApi<ClientGattEvent.ServicesDiscovered>(gatt) {
    private var discoveryServiceCallback: (() -> Unit)? = null

    override fun onEvent(event: ClientGattEvent.ServicesDiscovered) {
        if (event.status == BleGattOperationStatus.GATT_SUCCESS) {
            discoveryServiceCallback?.invoke()
        }
    }

    suspend fun discoverServices(): Boolean = suspendCoroutine {
        discoverServicesFlow().onEach { result ->
            it.resume(result)
        }.launchIn(KBLEScope)
    }

    @SuppressLint("MissingPermission")
    private fun discoverServicesWithCallback(callback: () -> Unit) {
        discoveryServiceCallback = callback
        gatt.discoverServices()
    }

    private fun discoverServicesFlow(): Flow<Boolean> = callbackFlow {
        discoverServicesWithCallback {
            trySend(true)
        }
        awaitClose { discoveryServiceCallback = null }
    }
}