package com.mitsuki.armorydemo.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.mitsuki.armory.base.activity.BaseActivity
import com.mitsuki.armory.base.ext.viewBinding
import com.mitsuki.armory.base.nsd.NsdConnect
import com.mitsuki.armory.base.nsd.NsdState
import com.mitsuki.armory.base.nsd.NsdUtils
import com.mitsuki.armory.base.sundries.DeviceName
import com.mitsuki.armorydemo.databinding.ActivityNsdBinding

class NsdActivity : BaseActivity<ActivityNsdBinding>(ActivityNsdBinding::inflate),
    NsdManager.RegistrationListener,
    NsdManager.DiscoveryListener {

    private val mName by lazy { DeviceName(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.nsdDeviceName.text = mName.name()
        NsdConnect.nsdName = mName.name()
        NsdConnect.nsdPort = 25252

        lifeLaunch(Lifecycle.State.STARTED) {
            launchCollect(NsdConnect.nsdStatusFlow) {
                binding.nsdStatus.text = it.toString()
            }
        }

        binding.nsdRegister.setOnClickListener {
            NsdConnect.connect(this, NsdState.Discoverable)
        }

        binding.nsdDiscover.setOnClickListener {
            NsdConnect.connect(this, NsdState.Discovery)

        }

        binding.nsdStop.setOnClickListener {
            NsdConnect.connect(this, NsdState.None)
        }

//        binding.nsdDiscover.setOnClickListener {
//            NsdUtils.discoverServices(this, this)
//        }
//
//        binding.nsdStopDiscover.setOnClickListener {
//            NsdUtils.stopServiceDiscovery(this, this)
//        }


    }

    private fun toast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    /* NsdManager.RegistrationListener */
    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        toast("onRegistrationFailed: $serviceInfo  $errorCode")
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        toast("onUnregistrationFailed: $serviceInfo  $errorCode")
    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
        toast("onServiceRegistered: $serviceInfo")

    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
        toast("onServiceUnregistered: $serviceInfo")
    }

    /*  NsdManager.DiscoveryListener */
    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
    }

    override fun onDiscoveryStarted(serviceType: String?) {
    }

    override fun onDiscoveryStopped(serviceType: String?) {
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
    }

}