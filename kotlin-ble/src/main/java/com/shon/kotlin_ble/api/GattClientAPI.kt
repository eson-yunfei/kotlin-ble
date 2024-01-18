package com.shon.kotlin_ble.api

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import android.util.Log
import androidx.annotation.IntRange
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

    @SuppressLint("MissingPermission")
    fun enableCharacteristicNotification(
        serviceUUID: UUID,
        characteristicUUID: UUID
    ) {
        val characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
        characteristicApi.enableCharacteristicNotification(characteristic)
    }

    @SuppressLint("MissingPermission")
    fun writeDescriptor(descriptor: BluetoothGattDescriptor, value: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(descriptor, value)
        } else @Suppress("DEPRECATION") {
            descriptor.value = value
            gatt.writeDescriptor(descriptor)
        }
    }

    @SuppressLint("MissingPermission")
    fun readDescriptor(descriptor: BluetoothGattDescriptor) {
        gatt.readDescriptor(descriptor)
    }

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
    fun disconnect() {
        gatt.disconnect()
    }

    fun clearServicesCache() {
        try {
            val refreshMethod: Method = gatt.javaClass.getMethod("refresh")
            refreshMethod.invoke(gatt)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun close() {
        gatt.close()
    }



}