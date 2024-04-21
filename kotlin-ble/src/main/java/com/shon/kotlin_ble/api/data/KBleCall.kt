package com.shon.kotlin_ble.api.data

/**
 *
 * @Author xiao
 * @Date 2023-10-20 16:42
 *
 */
//data class KBleCall(val serviceUUID: UUID, val characteristicUUID: UUID) {
//
//    fun characteristic(gatt: BluetoothGatt): BluetoothGattCharacteristic =
//        gatt.getService(serviceUUID).getCharacteristic(characteristicUUID)
//
//}

data class KBleWriteCall(val byteArray: ByteArray){



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KBleWriteCall

        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }

}
