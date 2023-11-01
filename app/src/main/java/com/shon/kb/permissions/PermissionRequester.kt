package com.shon.kb.permissions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *
 * @Author xiao
 * @Date 2023-10-18 15:41
 *
 */



fun AppCompatActivity.requestPermission(
    vararg permission: String
): Boolean? {
    val fragment = createPermissionFragment(supportFragmentManager)
    return fragment.safeRequestPermissions(*permission).value
}

fun Fragment.requestPermission(
    vararg permission: String
): Boolean? {
    val fragment = createPermissionFragment(childFragmentManager)
    return fragment.safeRequestPermissions(*permission).value
}


private fun createPermissionFragment(fragmentManager: FragmentManager): PermissionFragment {
    val tag = "PermissionRequestFragment"
    var fragmentByTag = fragmentManager.findFragmentByTag(tag) as PermissionFragment?
    fragmentByTag?: run {
        fragmentByTag = PermissionFragment()
        fragmentManager
            .beginTransaction()
            .add(fragmentByTag!!, tag)
            .commitNow()
    }
    return fragmentByTag!!

}
