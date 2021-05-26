package com.ravisugara.sharebill

import android.Manifest
import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun permissionRequest(context: FragmentActivity?): Boolean {
    var status = false
    Dexter.withActivity(context as Activity?)
        .withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object :
            MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                status = report.areAllPermissionsGranted()
            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
                token.continuePermissionRequest()
            }
        }).check()
    return status

}