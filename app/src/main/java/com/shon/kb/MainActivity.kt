package com.shon.kb

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.shon.kb.ui.theme.KotlinBleTheme
import com.shon.kotlin_ble.client.ClientBleGatt
import com.shon.kotlin_ble.scanner.KBleScanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {

    private lateinit var kBleScanner: KBleScanner

    private val serviceUUid:UUID = UUID.fromString("0000aa00-0000-1000-8000-00805f9b34fb")
    //    val filterUUID = ParcelUuid.fromString("0000aa00-0000-1000-8000-00805f9b34fb")
    private val writeUUid:UUID = UUID.fromString("0000aa02-0000-1000-8000-00805f9b34fb")
    private val notifyUUid:UUID = UUID.fromString("0000aa01-0000-1000-8000-00805f9b34fb")


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinBleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var scanResultList by remember {
                        mutableStateOf<List<ScanResult>>(emptyList())
                    }


                    Column {
                        Greeting("Android")

                        Button(onClick = {
                            kBleScanner = KBleScanner(this@MainActivity)
                            kBleScanner.startScan(0).onEach { list ->
                                scanResultList = list
                            }.launchIn(lifecycleScope)

                        }) {
                            Text(text = "申请权限")
                        }
                        DeviceList(list = scanResultList) {
                            kBleScanner.stopScanner()


                            lifecycleScope.launch {
                                val connectDevice =
                                    ClientBleGatt.connectDevice(
                                        this@MainActivity,
                                        it.device.address
                                    )

                                val discoverServices = connectDevice?.discoverServices()
                                discoverServices?.let {
                                    if (it){
                                        with(connectDevice) {
                                            enableCharacteristicNotification(serviceUUid, notifyUUid)

                                            delay(2000)
                                            val writeResult = writeCharacteristic(
                                                serviceUUid,
                                                writeUUid, byteArrayOf(0x02, 0x01)
                                            )
                                            Log.d("TAG", "writeResult: $writeResult")
                                        }
                                    }
                                }
//
                                Log.d("TAG", "connectDevice result: $connectDevice ")
                            }

                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@SuppressLint("MissingPermission")
@Composable
fun DeviceList(list: List<ScanResult>, onItemClick: (ScanResult) -> Unit) {
    LazyColumn {
        items(count = list.size,
            itemContent = {
                val scanResult = list[it]
                Column(modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        onItemClick(scanResult)
                    }) {
                    Text(text = scanResult.device.name)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = scanResult.device.address)
                }
            })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinBleTheme {
        Greeting("Android")
    }
}