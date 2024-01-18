package com.shon.kb.ui.compose

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.WindowMetrics
import android.view.inspector.WindowInspector
import androidx.appcompat.app.WindowDecorActionBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.magnifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shon.kb.ui.permissions.BlePermissionRequest
import com.shon.kb.ui.views.ScanDeviceList
import com.shon.kb.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(lifecycleOwner: LifecycleOwner){

    val mainViewModel:MainViewModel = viewModel()

    var scanResultList by remember { mutableStateOf<List<ScanResult>>(emptyList()) }

    mainViewModel.scanList.observe(lifecycleOwner){
        scanResultList = it
    }

    Scaffold(
        topBar = { HomeAppBar() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {

       Box(Modifier.padding(top = 70.dp)) {
           Column(modifier = Modifier.fillMaxHeight()) {
               BlePermissionRequest() {
                   mainViewModel.scanDevice()
               }

               ScanDeviceList(list = scanResultList, onItemClick ={

               })
           }
       }
    }


}