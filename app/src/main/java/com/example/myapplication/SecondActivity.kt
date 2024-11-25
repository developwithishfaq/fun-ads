package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySecondBinding
import com.funn.ads.units.interAds.logFunSdk
import com.monetization.nativeads.AdmobNativeAdsManager

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this, "HI", Toast.LENGTH_SHORT).show()
        initialize {
            AdmobNativeAdsManager.getAllController().forEach {
                logFunSdk("Inter = ${it.getAdKey()}:${it.getAdId()}")
            }

            /*binding.adFrame.initPlacement(
                activity = this@SecondActivity,
                placementKey = "MainAdEnable",
                lifecycle = lifecycle,
            )*/
        }

    }

    fun initialize(onInit: () -> Unit) {

    }
}