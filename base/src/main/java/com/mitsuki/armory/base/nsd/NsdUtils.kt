package com.mitsuki.armory.base.nsd

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

object NsdUtils {

    fun registerService(
        context: Context,
        name: String,
        port: Int,
        listener: NsdManager.RegistrationListener,
    ): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                registerService(NsdServiceInfo().also {
                    it.serviceName = name
                    it.port = port
                    it.serviceType = "_http._tcp."
                }, NsdManager.PROTOCOL_DNS_SD, listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun unregisterService(context: Context, listener: NsdManager.RegistrationListener): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                unregisterService(listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun discoverServices(context: Context, listener: NsdManager.DiscoveryListener): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun stopServiceDiscovery(context: Context, listener: NsdManager.DiscoveryListener): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                stopServiceDiscovery(listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun resolveService(
        context: Context,
        info: NsdServiceInfo,
        listener: NsdManager.ResolveListener,
    ): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                @Suppress("DEPRECATION") resolveService(info, listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun stopServiceResolution(
        context: Context,
        listener: NsdManager.ResolveListener,
    ): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                stopServiceResolution(listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun registerServiceInfoCallback(
        context: Context, info: NsdServiceInfo, listener: NsdManager.ServiceInfoCallback
    ): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                registerServiceInfoCallback(info, Runnable::run, listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun unregisterServiceInfoCallback(
        context: Context, listener: NsdManager.ServiceInfoCallback
    ): Boolean {
        return try {
            ContextCompat.getSystemService(context, NsdManager::class.java)?.run {
                unregisterServiceInfoCallback(listener)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}