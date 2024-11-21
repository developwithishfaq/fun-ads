package com.funn.ads.sdk

import android.content.Context
import androidx.annotation.RawRes
import com.funn.ads.configs.FunConfigs
import com.funn.ads.constants.readJsonFromRawFile
import com.funn.ads.domain.model.ControllerModel
import com.funn.ads.units.interAds.logFunSdk
import com.monetization.core.listeners.ControllersListener


fun FunAdsSdk.init(
    context: Context,
    @RawRes localRes: Int,
    controllers: List<ControllerModel>,
    remoteFetchSupportedKey: String = "FunXAdsSupported",
    remoteFetchFileKey: String = "AutoFile",
    controllersListener: ControllersListener? = null,
    onInit: () -> Unit
) {
    val listener = FunConfigs.getFunRemoteListener() ?: throw IllegalArgumentException("Please Set setFunConfigsListener")
    val remoteFetch = listener.getBooleanFromRemote(remoteFetchSupportedKey)
    val jsonFile = if (remoteFetch) {
        listener.getStringFromRemote(remoteFetchFileKey).toAdsJsonModel()
    } else {
        localRes.readJsonFromRawFile(context).toAdsJsonModel()
    }
    logFunSdk("Fetched(remotely=$remoteFetch),File=$jsonFile ")
    jsonFile?.let {
        initialize(
            jsonModel = it,
            localControllers = controllers,
            controllersListener = controllersListener
        ) {
            onInit.invoke()
        }
    } ?: kotlin.run {
        onInit.invoke()
    }

}