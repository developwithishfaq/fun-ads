package com.example.myapplication

import android.content.Context
import com.funn.ads.domain.model.ControllerModel
import com.funn.ads.sdk.FunAdsSdk
import com.funn.ads.sdk.init
import com.monetization.core.listeners.ControllersListener

object AdsEntryManager {

    fun initControllers(context: Context, onInitialized: () -> Unit) {
        val controllersListener = object : ControllersListener {

        }
        FunAdsSdk.init(
            context = context,
            localRes = R.raw.auto,
            controllers = AdKeys.entries.map {
                ControllerModel(
                    adKey = it.name,
                    adType = it.adType,
                    adIds = it.adIds,
                    bannerAdType = it.bannerAdType
                )
            },
            controllersListener = controllersListener
        ) {
            onInitialized.invoke()
        }
    }
}