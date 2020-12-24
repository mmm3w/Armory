package com.mitsuki.armorydemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mitsuki.armory.httprookie.HttpRookie
import com.mitsuki.armory.httprookie.convert.StringConvert
import com.mitsuki.armory.httprookie.response.Response
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        HttpRookie.get<String>("https://www.baidu.com") {
//            convert = StringConvert()
//            callback(
//                onSuccess = {
//                    main_content?.text = "4444"
//                    main_content?.text = it.requireBody()
//                },
//                onError = {
//
//                }
//            )
//        }.enqueue()

        HttpRookie.get<String>("https://www.baidu.com") { convert = StringConvert() }
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