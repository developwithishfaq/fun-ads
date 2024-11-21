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
        AdsEntryManager.initControllers()
        initialize {
            /*binding.loadInter.setOnActionClickListener(activity = this, actionKey = "LoadBtn") {
                Toast.makeText(this@SecondActivity, "Ad Shown = $it", Toast.LENGTH_SHORT).show()
            }*/

            AdmobNativeAdsManager.getAllController().forEach {
                logFunSdk("Inter = ${it.getAdKey()}:${it.getAdId()}")
            }

            /*

                        binding.loadInter.setOnClickListener {
                            FullScreenAdsShowManagerFun.showFullScreenAd(
                                placementKey = "MainInter",
                                key = "CommonInter",
                                adType = AdType.INTERSTITIAL,
                                onAdDismiss = { it, msg ->
                                    Toast.makeText(this@SecondActivity, "Ad Shown = $it", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                activity = this,
                                isInstantAd = true
                            )
                        }
                        binding.adFrame.sdkNativeAdFun(
                            adLayout = LayoutInfo.LayoutByName(NativeTemplates.SmallNative),
                            adKey = AdKeys.Test.name,
                            placementKey = "MainScreenAd",
                            lifecycle = lifecycle,
                            activity = this
                        )
            */

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