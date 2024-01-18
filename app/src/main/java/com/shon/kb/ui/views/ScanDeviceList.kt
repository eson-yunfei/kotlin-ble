package com.shon.kb.ui.views

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@SuppressLint("MissingPermission")
@Composable
fun ScanDeviceList(list: List<ScanResult>, onItemClick: (ScanResult) -> Unit){
    LazyColumn {
        items(count = list.size,
            itemContent = {
                val scanResult = list[it]
                DeviceItem(scanResult = scanResult){
                    onItemClick.invoke(scanResult)
                }
            })
    }
}