package com.mitsuki.armorydemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armorydemo.systemalert.AlertLayout
import com.mitsuki.armorydemo.systemalert.AlertManager
import com.mitsuki.armorydemo.systemalert.overlayPermission
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val testView by lazy {
        AlertLayout(this,R.layout.view_test)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_content?.setOnClickListener {
            overlayPermission {
                it?.apply { startActivity(this) } ?: {
                    AlertManager.addView(testView)
                }()
            }
        }
    }


}