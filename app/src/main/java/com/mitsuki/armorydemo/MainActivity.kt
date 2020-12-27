package com.mitsuki.armorydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.convert.StringConvert
import com.mitsuki.armory.httprookie.request.header
import com.mitsuki.armory.httprookie.request.urlParams
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        HttpRookie.get<String>("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm") {
//            convert = StringConvert()
//            urlParams("tel" to "13858386438")
//            callback(
//                onSuccess = {
//                    Log.e("asdf", "${it.requireBody()}")
//                    main_content?.text = "4444"
//                    main_content?.text = it.requireBody()
//                }
//            )
//        }.enqueue()

        HttpRookie.get<String>("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm") {
            urlParams("tel" to "13858386438")
            convert = StringConvert()
        }
            .enqueueObservable()
            .subscribe(
                object : Observer<Response<String?>> {
                    override fun onComplete() {
                        Log.e("asdf", "onComplete")
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Log.e("asdf", "onSubscribe")
                    }

                    override fun onNext(t: Response<String?>) {
                        runOnUiThread {
                            main_content?.text = t.body
                        }
                        Log.e("asdf", "onNext")
                    }

                    override fun onError(e: Throwable?) {
                        Log.e("asdf", "onError  $e")
                    }

                })

        main_content?.text = "3333"
    }
}