package com.mitsuki.armorydemo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.convert.FileConvert
import com.mitsuki.armorydemo.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }

        binding.mainText.text = path()
//        HttpRookie.get<>()
//
//        HttpRookie.get<File>("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4") {
//            convert = FileConvert(cacheDir.path, "test.mp4")
//            callback(onFinish = {
//                Log.e("asdf", "finish")
//            },
//                onError = {
//                    Log.e("asdf", "${it.throwable}")
//                },
//                onSuccess = {
//
//                })
//        }.enqueue()
    }


    private fun path(): String {
        return "cacheDir:$cacheDir\n" + //  /data/data/包名/cache 内部缓存路径，内存不够时候会被优先删除
                "filesDir$filesDir"  //  /data/data/包名/files 跟随应用的内部存储
    }

}