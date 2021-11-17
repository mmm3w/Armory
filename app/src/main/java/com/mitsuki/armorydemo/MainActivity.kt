package com.mitsuki.armorydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.mitsuki.armory.inputmeasure.InputHeight
import com.mitsuki.armorydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        InputHeight.callback {
            ViewCompat.animate(binding.mainInput).also { viewAnimator ->
                viewAnimator.cancel()
                viewAnimator.translationY((-it).toFloat())
                    .setDuration(150)
                    .start()
            }
        }
        InputHeight.bindMeasure(this)
    }

}