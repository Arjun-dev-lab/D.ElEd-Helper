package com.askproductions.deledhelper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*

class WebViewActivity : AppCompatActivity() {

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var adView: AdView
    private val handler = Handler(Looper.getMainLooper())
    private val adInterval = 300000L // 5 minutes in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // Initialize Facebook Audience Network
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(false)

        // Load URL passed from the MainActivity
        val url = intent.getStringExtra("url") ?: "https://www.google.com"
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

        // Set up Banner Ad
        adView = AdView(this, "3894902787424141_3903244319923321", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.adContainer).addView(adView)
        adView.loadAd()

        // Set up Interstitial Ad
        interstitialAd = InterstitialAd(this, "3894902787424141_3894903594090727")
        loadInterstitialAd()

        // Schedule interstitial ad to show every 5 minutes
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (interstitialAd.isAdLoaded) {
                    interstitialAd.show()
                } else {
                    loadInterstitialAd()
                }
                handler.postDelayed(this, adInterval)
            }
        }, adInterval)
    }

    private fun loadInterstitialAd() {
        interstitialAd.loadAd(
            interstitialAd.buildLoadAdConfig()
                .withAdListener(object : InterstitialAdListener {
                    override fun onInterstitialDisplayed(ad: Ad?) {}
                    override fun onInterstitialDismissed(ad: Ad?) {
                        loadInterstitialAd() // Reload after dismissed
                    }
                    override fun onError(ad: Ad?, error: AdError) {}
                    override fun onAdLoaded(ad: Ad?) {}
                    override fun onAdClicked(ad: Ad?) {}
                    override fun onLoggingImpression(ad: Ad?) {}
                })
                .build()
        )
    }

    override fun onDestroy() {
        adView.destroy()
        interstitialAd.destroy()
        handler.removeCallbacksAndMessages(null) // Stop the handler
        super.onDestroy()
    }

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
