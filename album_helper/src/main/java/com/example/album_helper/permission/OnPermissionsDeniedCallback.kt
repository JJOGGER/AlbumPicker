package com.example.album_helper.permission

interface OnPermissionsDeniedCallback {

    fun onPermissionsDenied(requestCode: Int, deniedPermissions: Array<String>)

}
