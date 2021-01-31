package com.mitsuki.armorydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armorydemo.databinding.ActivityMainBinding
import com.mitsuki.systemoverlay.overlayPermission
import com.mitsuki.systemoverlay.OverlayManager
import com.mitsuki.systemoverlay.SideOverlay
import com.mitsuki.systemoverlay.SimpleOverlay


class MainActivity : AppCompatActivity() {

    private val testView by lazy {
        SideOverlay(applicationContext).apply { layout(R.layout.view_test) }
    }

    private val controlView by lazy {
        SimpleOverlay(applicationContext).apply { layout(R.layout.view_control) }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        overlayPermission {
            it?.apply { startActivity(this) }
        }

        testView.setOnClickListener {
            OverlayManager.switch(controlView)
        }

        binding.btnAdd.setOnClickListener {
            OverlayManager.switch(testView)
        }

        binding.btnRemove.setOnClickListener {
            OverlayManager.exit()
        }

    }

}