package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySplashBinding
import com.funn.ads.domain.model.ControllerModel
import com.funn.ads.sdk.FunAdsSdk
import com.funn.ads.sdk.init
import com.monetization.core.listeners.ControllersListener
import com.remote.firebaseconfigs.SdkRemoteConfigController
import com.remote.firebaseconfigs.fetch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val configs = MyRemoteConfigController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configs.fetch {
            initSdk {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    private fun initSdk(onDone: () -> Unit) {
        FunAdsSdk.init(
            context = this@SplashActivity,
            localRes = R.raw.auto,
            controllers = AdKeys.entries.map {
                ControllerModel(
                    adKey = it.name,
                    adType = it.adType,
                    adIds = it.adIds,
                    bannerAdType = it.bannerAdType
                )
            },
            controllersListener = object : ControllersListener {},
        ) {
            onDone.invoke()
        }
    }
}