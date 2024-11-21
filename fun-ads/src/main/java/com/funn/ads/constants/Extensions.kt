package com.funn.ads.constants

import android.content.Context
import com.monetization.core.counters.CounterStrategies
import java.lang.Exception

fun Int.readJsonFromRawFile(context: Context): String {
    try {
        val inputStream = context.resources.openRawResource(this)
        return inputStream.bufferedReader().use { it.readText() }
    } catch (_: Exception) {
        return ""
    }
}


fun String?.toIntOrZero(def: Int = 0): Int {
    return this?.toIntOrNull() ?: def
}

fun String.toCounterStrategy(): CounterStrategies {
    return if (this.equals("Keep", true)) CounterStrategies.KeepSameValue
    else if (this.equals("Zero", true)) CounterStrategies.ResetToZero
    else if (this.equals("Half", true)) CounterStrategies.HalfValue
    else CounterStrategies.SetStartingTo(this.toIntOrZero())
}