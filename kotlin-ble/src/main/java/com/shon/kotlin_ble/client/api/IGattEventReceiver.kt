package com.shon.kotlin_ble.client.api

import com.shon.kotlin_ble.client.ClientBleGattCallback
import com.shon.kotlin_ble.client.ClientGattEvent
import com.shon.kotlin_ble.core.KBLEScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 *
 * @Author xiao
 * @Date 2023-10-20 15:57
 *
 */
interface IGattEventReceiver<T : ClientGattEvent> {

    fun observerEvent(gattEvent: (ClientGattEvent) -> Unit) {
        ClientBleGattCallback.gattCallback.event.onEach {
            gattEvent(it)
        }.launchIn(KBLEScope)
    }

    fun onEvent(event: T)


}