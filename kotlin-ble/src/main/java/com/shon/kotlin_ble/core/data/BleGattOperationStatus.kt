package com.shon.kotlin_ble.core.data

import android.bluetooth.BluetoothGatt
import android.os.Build

/**
 *
 * @Author xiao
 * @Date 2023-10-19 15:21
 *
 */
enum class BleGattOperationStatus(val value: Int)  {

    /**
     * Unknown error.
     */
    GATT_UNKNOWN(-1),

    /**
     * Most generic GATT error code.
     */
    GATT_ERROR(133),

    /**
     * A GATT operation completed successfully.
     */
    GATT_SUCCESS(BluetoothGatt.GATT_SUCCESS),

    /**
     * A remote device connection is congested.
     */
    GATT_CONNECTION_CONGESTED(BluetoothGatt.GATT_CONNECTION_CONGESTED),

    /**
     * A GATT operation failed, errors other than the above.
     */
    GATT_FAILURE(BluetoothGatt.GATT_FAILURE),

    /**
     * Insufficient authentication for a given operation.
     */
    GATT_INSUFFICIENT_AUTHENTICATION(BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION),

    /**
     * Insufficient encryption for a given operation.
     */
    GATT_INSUFFICIENT_ENCRYPTION(BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION),

    /**
     * A write operation exceeds the maximum length of the attribute.
     */
    GATT_INVALID_ATTRIBUTE_LENGTH(BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH),

    /**
     * A read or write operation was requested with an invalid offset.
     */
    GATT_INVALID_OFFSET(BluetoothGatt.GATT_INVALID_OFFSET),

    /**
     * GATT read operation is not permitted.
     */
    GATT_READ_NOT_PERMITTED(BluetoothGatt.GATT_READ_NOT_PERMITTED),

    /**
     * The given request is not supported.
     */
    GATT_REQUEST_NOT_SUPPORTED(BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED),

    /**
     * GATT write operation is not permitted.
     */
    GATT_WRITE_NOT_PERMITTED(BluetoothGatt.GATT_WRITE_NOT_PERMITTED),

    /**
     * Insufficient authorization for a given operation.
     */
    GATT_INSUFFICIENT_AUTHORIZATION(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            BluetoothGatt.GATT_INSUFFICIENT_AUTHORIZATION
        } else {
            8
        }
    );

    val isSuccess
        get() = this == GATT_SUCCESS

    companion object {
        fun create(value: Int): BleGattOperationStatus {
            return values().firstOrNull { it.value == value } ?: GATT_UNKNOWN
        }
    }
}