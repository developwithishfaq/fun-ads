package com.funn.ads.listener

interface FunMainListener {
    fun getStringFromRemote(key: String): String
    fun getBooleanFromRemote(key: String): Boolean
    fun getLongFromRemote(key: String): Long
}