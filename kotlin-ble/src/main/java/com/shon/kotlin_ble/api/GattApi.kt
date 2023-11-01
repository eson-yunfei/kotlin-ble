package com.shon.kotlin_ble.api

import android.bluetooth.BluetoothGatt
import com.shon.kotlin_ble.client.ClientGattEvent
import com.shon.kotlin_ble.client.api.IGattEventReceiver

/**
 *
 * @Author xiao
 * @Date 2023-10-20 10:58
 *
 */
abstract class GattApi<T : ClientGattEvent>(protected val gatt: BluetoothGatt) :IGattEventReceiver<T>{

}