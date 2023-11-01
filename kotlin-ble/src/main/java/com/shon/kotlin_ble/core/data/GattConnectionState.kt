package com.shon.kotlin_ble.core.data

import android.bluetooth.BluetoothProfile

/**
 *
 * @Author xiao
 * @Date 2023-10-19 11:54
 *
 */
enum class GattConnectionState(internal val value: Int) {

    /**
     * Device is connected.
     */
    STATE_DISCONNECTED(BluetoothProfile.STATE_DISCONNECTED),

    /**
     * Connection has been initiated.
     */
    STATE_CONNECTING(BluetoothProfile.STATE_CONNECTING),

    /**
     * Device is disconnected.
     */
    STATE_CONNECTED(BluetoothProfile.STATE_CONNECTED),

    /**
     * Disconnection has been initiated.
     */
    STATE_DISCONNECTING(BluetoothProfile.STATE_DISCONNECTING);

    companion object {
        fun create(value: Int): GattConnectionState {
            return values().firstOrNull { it.value == value }
                ?: throw IllegalStateException("Cannot create GattConnectionState for value: $value")
        }
    }
}