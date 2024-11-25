package com.example.myapplication

import android.app.Activity
import android.app.Application
import com.funn.ads.configs.FunConfigs
import com.funn.ads.listener.FunMainListener
import com.google.firebase.FirebaseApp
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.listeners.RemoteConfigsProvider
import com.monetization.core.listeners.SdkDialogsListener
import com.monetization.core.listeners.SdkListener
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.utils.dialog.SdkDialogs
import com.monetization.core.utils.dialog.onAdLoadingDialogStateChange
import com.remote.firebaseconfigs.SdkRemoteConfigController

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)

        var sdkDialog: SdkDialogs? = null
        SdkConfigs.setDialogListener(object : SdkDialogsListener {
            override fun onAdLoadingDialogStateChange(
                activity: Activity,
                placementKey: String,
                adKey: String,
                adType: AdType,
                showDialog: Boolean,
                isForBlackBg: Boolean
            ) {
                if (showDialog) {
                    sdkDialog = SdkDialogs(activity)
                }
                sdkDialog?.onAdLoadingDialogStateChange(
                    showDialog = showDialog,
                    isForBlackBg = isForBlackBg
                )
            }
        })
        SdkConfigs.setRemoteConfigsListener(object : RemoteConfigsProvider {
            override fun getAdWidgetData(placementKey: String, adKey: String): AdsWidgetData? {
                return null
            }

            override fun isAdEnabled(placementKey: String, adKey: String, adType: AdType): Boolean {
                return true
            }
        })

        SdkConfigs.setListener(object : SdkListener {
            override fun canLoadAd(adType: AdType, placementKey: String, adKey: String): Boolean {
                return true
            }

            override fun canShowAd(adType: AdType, placementKey: String, adKey: String): Boolean {
                return true
            }
        }, true)

        FunConfigs.setFunConfigsListener(object : FunMainListener {
            override fun getStringFromRemote(key: String): String {
                return SdkRemoteConfigController.getRemoteConfigString(key)
            }

            override fun getBooleanFromRemote(key: String): Boolean {
                return SdkRemoteConfigController.getRemoteConfigBoolean(key)
            }

            override fun getLongFromRemote(key: String): Long {
                return SdkRemoteConfigController.getRemoteConfigLong(key)
            }
        })
    }
}