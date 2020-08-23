package com.mitsuki.armorydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mitsuki.armory.noresult.NoResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NoResult.startActivityForResult(
            this,
            Intent(),
            10001
        ) { requestCode: Int, resultCode: Int, intent: Intent? ->

        }
    }
}