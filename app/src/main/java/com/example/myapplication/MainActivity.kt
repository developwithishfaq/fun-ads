package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.funn.ads.ad_units.natives.SdkNativeAdFun
import com.funn.ads.sdk.FunAdsSdk
import com.funn.ads.sdk.init
import com.funn.ads.units.interAds.adsClickable
import com.funn.ads.units.interAds.onAdsAction
import com.monetization.adsmain.commons.addNewController
import com.monetization.composeviews.SdkNativeAd
import com.monetization.core.listeners.ControllersListener
import com.monetization.core.ui.LayoutInfo
import com.monetization.nativeads.AdmobNativeAdsManager
import com.remote.firebaseconfigs.RemoteCommons.toConfigString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = {
                        Button(
                            onClick = {
                                finish()
                                startActivity(Intent(this@MainActivity, SplashActivity::class.java))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Restart")
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Button(
                            onClick = {
                                onAdsAction(activity = this@MainActivity, actionKey = "Humor1") {
                                    Toast
                                        .makeText(
                                            this@MainActivity,
                                            "Ad Shown : $it",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        ) {
                            Text("Action")
                        }
                        SdkNativeAdFun(
                            activity = this@MainActivity,
                            placementKey = "MainNativeEnabled",
                            adKey = "Main",
                            adLayout = LayoutInfo.LayoutByXmlView(com.monetization.nativeads.R.layout.native_split),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}