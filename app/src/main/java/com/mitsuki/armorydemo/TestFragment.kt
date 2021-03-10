package com.mitsuki.armorydemo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.load
import com.mitsuki.armory.imagegesture.ImageGesture
import com.mitsuki.armory.imagegesture.StartType

class TestFragment : Fragment(R.layout.fragment_test) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageView>(R.id.test_image)?.apply {
            ImageGesture(this).apply { startType = StartType.TOP }
            load("https://hbimg.huabanimg.com/2aa0ad9ca161207917d91098564e66ecc7c56bafa1672-YJD6J0_fw658/format/webp")
        }

    }



}