package com.mitsuki.armorydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.callback.DefaultCallback
import com.mitsuki.armory.httprookie.convert.StringConvert
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        HttpRookie.get<String>("https://www.baidu.com") {
            convert = StringConvert()
            callback = DefaultCallback(
                onSuccess = {
                    Log.e("asdf", "${it.requireBody()}")
                },
                onError = {
                    Log.e("asdf", "error")
                }
            )
        }.enqueue()
    }
}