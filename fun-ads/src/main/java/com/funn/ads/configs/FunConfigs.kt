package com.funn.ads.configs

import com.funn.ads.listener.FunMainListener

object FunConfigs {

    private var listener: FunMainListener? = null

    fun setFunConfigsListener(listener: FunMainListener) {
        this.listener = listener
    }

    fun getFunRemoteListener() = listener


}