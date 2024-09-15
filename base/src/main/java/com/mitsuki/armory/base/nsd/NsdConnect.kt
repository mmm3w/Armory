package com.mitsuki.armory.base.nsd

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object NsdConnect {

    var nsdName: String? = null
    var nsdPort: Int = 0

    //nsd的状态
    val nsdStatus: NsdState get() = mNsdStatus.value
    val nsdStatusFlow: StateFlow<NsdState> get() = mNsdStatus
    private val mNsdStatus: MutableStateFlow<NsdState> = MutableStateFlow(NsdState.None)

    //nsd的事件
    private val mNsdEvent: MutableSharedFlow<NsdEvent> = MutableSharedFlow()
    val nsdEventFlow: SharedFlow<NsdEvent> get() = mNsdEvent

    private val mScope by lazy { CoroutineScope(Dispatchers.Default) }

    private val mNsdRegistrationListener by lazy {
        object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(info: NsdServiceInfo?) {
                Log.d("NsdConnect", "onServiceRegistered $info")
                mNsdStatus.update { NsdState.Discoverable }
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoverableRegistered(info)) }
            }

            override fun onRegistrationFailed(
                info: NsdServiceInfo?, code: Int
            ) {
                Log.d("NsdConnect", "onRegistrationFailed $info $code")
                mScope.launch {
                    mNsdEvent.emit(NsdEvent.DiscoverableRegistrationFailed(info, code))
                }
            }

            override fun onServiceUnregistered(info: NsdServiceInfo?) {
                Log.d("NsdConnect", "onServiceUnregistered $info")
                mNsdStatus.update { NsdState.None }
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoverableUnregistered(info)) }
            }

            override fun onUnregistrationFailed(info: NsdServiceInfo?, code: Int) {
                Log.d("NsdConnect", "onUnregistrationFailed $info $code")
                mScope.launch {
                    mNsdEvent.emit(NsdEvent.DiscoverableUnregistrationFailed(info, code))
                }
            }
        }
    }

    private val mNsdDiscoveryListener by lazy {
        object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(type: String?) {
                Log.d("NsdConnect", "onDiscoveryStarted $type")
                mNsdStatus.update { NsdState.Discovery }
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryStarted(type)) }
            }

            override fun onStartDiscoveryFailed(type: String?, code: Int) {
                Log.d("NsdConnect", "onStartDiscoveryFailed $type $code")
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryStartFailed(type, code)) }
            }

            override fun onDiscoveryStopped(type: String?) {
                Log.d("NsdConnect", "onDiscoveryStopped $type")
                mNsdStatus.update { NsdState.None }
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryStopped(type)) }
            }

            override fun onStopDiscoveryFailed(type: String?, code: Int) {
                Log.d("NsdConnect", "onStopDiscoveryFailed $type $code")
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryStopFailed(type, code)) }
            }


            override fun onServiceFound(info: NsdServiceInfo?) {
                Log.d("NsdConnect", "onServiceFound $info")
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryServiceFound(info)) }
            }

            override fun onServiceLost(info: NsdServiceInfo?) {
                Log.d("NsdConnect", "onServiceLost $info")
                mScope.launch { mNsdEvent.emit(NsdEvent.DiscoveryServiceLost(info)) }
            }
        }
    }

    fun connect(context: Context, mode: NsdState): Boolean {
        if (mode == nsdStatus) {
            return false
        }
        if (mode == NsdState.None) {
            return when (mNsdStatus.value) {
                NsdState.Discovery -> NsdUtils.stopServiceDiscovery(context, mNsdDiscoveryListener)
                NsdState.Discoverable -> NsdUtils.unregisterService(
                    context, mNsdRegistrationListener
                )

                else -> false
            }
        } else {
            if (mNsdStatus.value == NsdState.None) {
                if (mode == NsdState.Discovery) {
                    return NsdUtils.discoverServices(context, mNsdDiscoveryListener)
                } else if (mode == NsdState.Discoverable) {
                    val name = nsdName ?: ""
                    return if (name.isEmpty() || nsdPort !in 1..65535) {
                        false
                    } else {
                        NsdUtils.registerService(context, name, nsdPort, mNsdRegistrationListener)
                    }
                }
            }
            return false
        }
    }
}