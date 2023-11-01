package com.shon.kotlin_ble.core.data

/**
 *
 * @Author xiao
 * @Date 2023-10-19 14:43
 *
 */
enum class BleWriteType(val value: Int) {
    /**
     * Write characteristic, requesting acknowledgement by the remote device.
     */
    DEFAULT(2),

    /**
     * Write characteristic without requiring a response by the remote device.
     */
    NO_RESPONSE(1),

    /**
     * Write characteristic including authentication signature.
     */
    SIGNED(4);
}