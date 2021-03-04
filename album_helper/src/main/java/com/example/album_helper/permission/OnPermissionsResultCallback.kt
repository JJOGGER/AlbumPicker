package com.example.album_helper.permission

interface OnPermissionsResultCallback {

    fun onPermissionsGranted(requestCode: Int)

    fun onPermissionsDenied(requestCode: Int, deniedPermissions: Array<String>)

}