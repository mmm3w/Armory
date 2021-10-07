package com.mitsuki.armorydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armorydemo.databinding.ActivityMainBinding
import com.mitsuki.armory.base.permission.readStorePermissionLauncher

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val readStorePermissionLauncher = readStorePermissionLauncher()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }


    }

    override fun onResume() {
        super.onResume()
        readStorePermissionLauncher.launch {  }
    }
}