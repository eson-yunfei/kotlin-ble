package com.shon.kotlin_ble.client

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Context
import com.shon.kotlin_ble.api.GattClientAPI
import com.shon.kotlin_ble.client.api.IGattEventReceiver
import com.shon.kotlin_ble.core.KBLEScope
import com.shon.kotlin_ble.core.data.BleGattConnectionStatus
import com.shon.kotlin_ble.core.data.GattConnectionState
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
 * @Date 2023-10-19 11:23
 *
 */
class ClientBleGatt private constructor() :
    IGattEventReceiver<ClientGattEvent.ConnectionStateChanged> {

    init {
        observerEvent {
            if (it is ClientGattEvent.ConnectionStateChanged){
                onConnectionStateChange(it.gatt, it.status, it.newState)
            }
        }
    }

    private var connectCallBack: ((BluetoothGatt?) -> Unit)? = null

    @SuppressLint("MissingPermission")
    fun connectDevice(context: Context, address: String): Flow<GattClientAPI?> = callbackFlow {
        connectCallBack = {
            it ?: kotlin.run { trySend(null) }
            it?.let { gatt ->
                trySend(GattClientAPI(gatt))
            }
            close()
        }
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val remoteDevice = bluetoothAdapter.getRemoteDevice(address)
        remoteDevice.connectGatt(context, false, ClientBleGattCallback.gattCallback)
        awaitClose { connectCallBack = null }
    }


    @SuppressLint("MissingPermission")
    private fun onConnectionStateChange(
        gatt: BluetoothGatt?,
        status: BleGattConnectionStatus,
        connectionState: GattConnectionState
    ) {
        when (connectionState) {
            GattConnectionState.STATE_CONNECTED -> connectCallBack?.invoke(gatt)
            GattConnectionState.STATE_DISCONNECTED -> {
                connectCallBack?.invoke(null)
                if (!status.isLinkLoss) {
                    gatt?.close()
                }
            }

            else -> {}
        }
    }

    companion object {
        private val clientBleGatt by lazy { ClientBleGatt() }

        suspend fun connectDevice(context: Context, address: String): GattClientAPI? {
            return suspendCoroutine { continuation ->
                clientBleGatt.connectDevice(context, address).onEach {
                    continuation.resume(it)
                }.launchIn(KBLEScope)
            }
        }

    }

    override fun onEvent(event: ClientGattEvent.ConnectionStateChanged) {


    }
}