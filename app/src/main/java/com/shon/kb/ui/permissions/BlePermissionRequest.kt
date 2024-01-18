package com.shon.kb.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.shon.kb.ui.views.ScanFilterTitleLayout


val blePermissionArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
//        Manifest.permission.BLUETOOTH_ADMIN
    )
} else {
    mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_ADMIN,
    )
}

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BlePermissionRequest (task:()->Unit){
//    val permissionState = rememberPermissionState(permission = "")
//    permissionState.status
    val blePermissionState = rememberMultiplePermissionsState(permissions = blePermissionArray){
        val result = it.values.reduce { acc, next -> acc && next }
        Log.d("TAG", "result  = $result ")
        if (result){
            task.invoke()
        }
    }

    ScanFilterTitleLayout(onInputClick = {

    },onBtnClick = {
        if (blePermissionState.allPermissionsGranted){
            task.invoke()
        }else {
            blePermissionState.launchMultiplePermissionRequest()
        }
    })

}
