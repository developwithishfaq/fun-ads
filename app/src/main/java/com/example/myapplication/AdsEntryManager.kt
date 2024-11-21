package com.example.myapplication

import com.monetization.adsmain.commons.addNewController
import com.monetization.core.listeners.ControllersListener

object AdsEntryManager {

    fun initControllers() {
        val controllersListener = object : ControllersListener {

        }
        AdKeys.entries.forEach { entry ->
            addNewController(
                adKey = entry.name,
                adType = entry.adType,
                adIdsList = entry.adIds,
                listener = controllersListener,
                bannerAdType = entry.bannerAdType
            )
        }
    }
}