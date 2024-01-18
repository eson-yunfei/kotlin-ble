package com.shon.kb.ui.views

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(scanResult: ScanResult,onItemClick:()->Unit){

    Column {
        Row (modifier = Modifier.wrapContentHeight()){
            Icon(Icons.Filled.Warning, contentDescription = null,
               modifier = Modifier.wrapContentSize().align(alignment = Alignment.CenterVertically) )
            Column(modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable {
                    onItemClick()
                }) {
                Text(text = scanResult.device.name)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = scanResult.device.address)
            }
        }
        Divider()
    }
}

@Preview
@Composable
fun DeviceItemPreview(){
    Box {
//        DeviceItem()
    }
}