package com.gb.vale.uivalulibrary.manager.permission

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY

class UiTayPermissionManager(
    private val activity: ComponentActivity? = null,
    private val fragment: Fragment? = null,
    private val onDeny: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}
    private var resultSinglePermission: ActivityResultLauncher<String>? = null
    private var resultMultiplePermissions: ActivityResultLauncher<Array<String>>? = null
    private var permission = UI_TAY_EMPTY

    init {
        if (activity != null)
            initActivity()
        else
            initFragment()
    }

    private fun initFragment() {
        resultSinglePermission =
            fragment?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                when {
                    granted -> onGranted()
                    else -> onDeny()
                }
            }
        resultMultiplePermissions =
            fragment?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
                var isGranted = false
                map.entries.forEach {
                    isGranted = it.value
                }
                if (isGranted)
                    onGranted()
                else onDeny()
            }
    }

    private fun initActivity() {
        resultSinglePermission = activity?.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted){
                onGranted()
            }else{
                onDeny()
            }
        }

        resultMultiplePermissions =
            activity?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
                var isGranted = false
                map.entries.forEach {
                    isGranted = it.value
                }
                if (isGranted) onGranted() else onDeny()
            }
    }

    fun requestPermission(required: String, onGranted: () -> Unit) {
        this.onGranted = onGranted
        resultSinglePermission?.launch(required)
    }

    fun requestPermissions(required: Array<String>, onGranted: () -> Unit) {
        this.onGranted = onGranted
        resultMultiplePermissions?.launch(required)
    }


}