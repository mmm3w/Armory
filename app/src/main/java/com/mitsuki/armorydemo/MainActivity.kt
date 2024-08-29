package com.mitsuki.armorydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mitsuki.armory.base.ext.dp2px
import com.mitsuki.armorydemo.databinding.ActivityMainBinding
import com.mitsuki.armory.base.ext.viewBinding
import com.mitsuki.armory.base.view.drawable.HeartDrawable


class MainActivity : ComponentActivity() {

    val binding by viewBinding(ActivityMainBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainAdapter.setOnClickListener {

        }

        val imageDrawable = HeartDrawable().apply {
            heartColor = Color.Red.toArgb()
            heartHintColor = 0xffaeaeae.toInt()
            heartHintStroke = dp2px(1.5f)
        }
        var isChecked = false
        binding.mainHeart.setImageDrawable(imageDrawable)
        binding.mainHeart.setOnClickListener {
            isChecked = !isChecked
            imageDrawable.setChecked(isChecked)
        }



    }
}