package com.mitsuki.armorydemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armory.inputmeasure.*
import com.mitsuki.armorydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val inputMeasure by lazy { InputMeasurePopupWindow(this) }
    private var tag = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("asdf", this.hashCode().toString())
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        refreshSize()
        lifecycle.addObserver(inputMeasure)
        //隐藏导航栏 隐藏状态栏
        //真隐藏（看不见）
        //假隐藏（看的见，但是布局会延伸上去）
        binding.hintText.setOnClickListener {
            when (tag) {
                0 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "恢复", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "隐藏状态栏", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "隐藏导航栏", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "隐藏状态栏和导航栏", Toast.LENGTH_SHORT).show()
                }
                4 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "假隐藏状态栏", Toast.LENGTH_SHORT).show()
                }
                5 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "假隐藏导航栏", Toast.LENGTH_SHORT).show()
                }
                6 -> {
                    window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    Toast.makeText(this, "假隐藏状态栏和导航栏", Toast.LENGTH_SHORT).show()
                }
            }
            tag++
            if (tag > 6) {
                tag = 0
            }
            refreshSize()
        }

    }

    private fun refreshSize() {
        binding.hintText.text = "screenWidth:$screenWidth\n" +
                "screenHeight:$screenHeight\n\n" +
                "displayWidth：$displayWidth\n" +
                "displayHeight：$displayHeight\n"

    }


}