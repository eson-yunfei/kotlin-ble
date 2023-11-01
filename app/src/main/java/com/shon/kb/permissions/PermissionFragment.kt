package com.shon.kb.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 *
 * @Author xiao
 * @Date 2023-10-18 14:23
 *
 */
class PermissionFragment : Fragment() {

    private  val PERMISSIONS_REQUEST_CODE = 88

    private val requestResult = MutableStateFlow<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    fun safeRequestPermissions(vararg permissions: String): StateFlow<Boolean?> {
        activity?.let {
            it.hasPermissions( *permissions) { granted, needRequest ->
                if (granted) {
                    return@hasPermissions
                }
                needRequest?.let { list ->
                    val array: Array<String> = Array(list.size) { i ->
                        return@Array list[i]
                    }
                    requestPermissions(array, PERMISSIONS_REQUEST_CODE)

                }

            }
        }
        return requestResult.asStateFlow()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode != PERMISSIONS_REQUEST_CODE){
            return
        }
        val grantList = mutableListOf<Boolean>()
        permissions.forEachIndexed { index, _ ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                grantList.add(true)
            }
        }
        lifecycleScope.launch {
            requestResult.emit(grantList.size == grantResults.size)
        }
    }


}