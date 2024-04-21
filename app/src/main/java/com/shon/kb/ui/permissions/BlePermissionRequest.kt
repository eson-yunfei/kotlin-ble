package com.shon.kb.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
