package com.example.myapplication

import com.remote.firebaseconfigs.SdkRemoteConfigController
import com.remote.firebaseconfigs.listeners.SdkConfigListener

var TestRemoteConfigCheck = true

class MyRemoteConfigController {

    fun fetch(onFetched: () -> Unit) {
        SdkRemoteConfigController.fetchRemoteConfig(
            defaultXml = R.xml.remote_config_defaults,
            fetchOutTimeInSeconds = 8,
            handlerDelayInSeconds = 8,
            fetchIntervalInSeconds = 0,
            callback = object : SdkConfigListener {
                override fun onDismiss() {
                    assignRemoteConfigs(forUpdate = false)
                    onFetched.invoke()
                }

                override fun onFailure(error: String) {

                }

                override fun onSuccess() {

                }
            },
            onUpdate = {
                assignRemoteConfigs(forUpdate = true)
            }
        )
    }

    private fun assignRemoteConfigs(forUpdate: Boolean) {
        SdkRemoteConfigController.apply {
            TestRemoteConfigCheck = getRemoteConfigBoolean("TestRemoteConfigCheck", def = true)
        }
    }

}