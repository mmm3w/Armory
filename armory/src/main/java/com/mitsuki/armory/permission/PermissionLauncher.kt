package com.mitsuki.armory.permission

interface PermissionLauncher {
    fun launch(action: (Boolean) -> Unit)
}