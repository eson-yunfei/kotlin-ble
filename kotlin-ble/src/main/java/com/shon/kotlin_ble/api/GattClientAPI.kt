package com.shon.kotlin_ble.api

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import android.util.Log
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission
import com.shon.kotlin_ble.client.ClientBleGattCallback
import com.shon.kotlin_ble.client.ClientGattEvent
import com.shon.kotlin_ble.core.toHexString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.Method
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * @Author xiao
 * @Date 2023-10-19 14:17
 *
 */
class GattClientAPI(private val gatt: BluetoothGatt) {

    private var discoveryServiceCallback: ((Boolean) -> Unit)? = null

    private val characteristicApi: CharacteristicApi = CharacteristicApi(gatt)
    private val discoverServicesApi:DiscoverServicesApi = DiscoverServicesApi(gatt)

    init {
        ClientBleGattCallback.gattCallback.event.onEach {
            when (it) {
                is ClientGattEvent.ServicesDiscovered -> discoveryServiceCallback?.invoke(true)
                is ClientGattEvent.CharacteristicEvent -> characteristicApi.onEvent(it)

                else -> {}
            }
        }.launchIn(CoroutineScope(SupervisorJob() + Dispatchers.Default))
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun enableCharacteristicNotification(
        serviceUUID: UUID,
        characteristicUUID: UUID
    ) {
        val characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
        characteristicApi.enableCharacteristicNotification(characteristic)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun writeDescriptor(descriptor: BluetoothGattDescriptor, value: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(descriptor, value)
        } else @Suppress("DEPRECATION") {
            descriptor.value = value
            gatt.writeDescriptor(descriptor)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun readDescriptor(descriptor: BluetoothGattDescriptor) {
        gatt.readDescriptor(descriptor)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun requestMtu(@IntRange(from = 23, to = 517) mtu: Int) {
        gatt.requestMtu(mtu)
    }

    suspend fun discoverServices(): Boolean  = discoverServicesApi.discoverServices()

    suspend fun writeCharacteristic(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        value: ByteArray
    ): Boolean {
        return suspendCoroutine { continuation ->

            callbackFlow {
                characteristicApi.writeWithResult(serviceUUID, characteristicUUID, value) {
                    Log.d("TAG", "writeCharacteristic: ${it.toHexString()}")

                    if (value.toHexString() == it.toHexString()) {
                        trySend(true)
                        close()
                        return@writeWithResult true
                    }
                    return@writeWithResult false
                }
                awaitClose {}
            }.onEach {
                continuation.resume(it)
            }.launchIn(CoroutineScope(Dispatchers.IO))
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        gatt.disconnect()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun clearServicesCache() {
        try {
            val refreshMethod: Method = gatt.javaClass.getMethod("refresh")
            refreshMethod.invoke(gatt)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun close() {
        gatt.close()
    }



}