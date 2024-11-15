package com.askproductions.deledhelper

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.RewardedVideoListener

class MainActivity : AppCompatActivity() {

    private lateinit var fbTopAdView: AdView
    private lateinit var fbBottomAdView: AdView
    private var ironSourceBanner: IronSourceBannerLayout? = null
    private var websiteUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Facebook Audience Network
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(true) // Set to 'true' for testing

        // Initialize IronSource
        IronSource.init(this, "2020eb075") // Replace with your IronSource app key

        // Check for internet connectivity and show a popup if there’s no connection
        if (!isInternetConnected()) {
            showNoInternetDialog()
        }

        // Set up Facebook and IronSource banner ads
        setupBannerAds()

        // Set up IronSource rewarded ads
        setupRewardedAd()

        // Set up buttons with rewarded ad actions
        findViewById<Button>(R.id.button1).setOnClickListener {
            showRewardedAd("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/malayalam-d.el.ed.html")
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            showRewardedAd("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/school%20resource/resource.html")
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            showRewardedAd("https://arjun-dev-lab.github.io/EduVerseApps/malayalam%20d.el.ed/HandBooks/handbook.html")
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            showRewardedAd("https://arjun-dev-lab.github.io/EduVerseApps/D.El.Ed%20Helper/about.html")
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun setupBannerAds() {
        // Facebook Banner Ad
        fbTopAdView = AdView(this, "3894902787424141_3894903337424086", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.topAdContainer1).addView(fbTopAdView)
        fbTopAdView.loadAd()

        // IronSource Banner Ad
        ironSourceBanner = IronSource.createBanner(this, ISBannerSize.BANNER)
        findViewById<LinearLayout>(R.id.bottomAdContainer1).addView(ironSourceBanner)

        ironSourceBanner?.bannerListener = object : BannerListener {
            override fun onBannerAdLoaded() {
                Toast.makeText(this@MainActivity, "IronSource banner ad loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onBannerAdLoadFailed(error: IronSourceError?) {
                Toast.makeText(
                    this@MainActivity,
                    "IronSource banner load failed: ${error?.errorMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onBannerAdClicked() {}
            override fun onBannerAdScreenPresented() {}
            override fun onBannerAdScreenDismissed() {}
            override fun onBannerAdLeftApplication() {}
        }

        ironSourceBanner?.let { IronSource.loadBanner(it) }
    }

    private fun setupRewardedAd() {
        IronSource.setRewardedVideoListener(object : RewardedVideoListener {
            override fun onRewardedVideoAdOpened() {}
            override fun onRewardedVideoAdClosed() {}
            override fun onRewardedVideoAvailabilityChanged(available: Boolean) {
                if (available) {
                    Toast.makeText(this@MainActivity, "Rewarded ad available", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onRewardedVideoAdStarted() {}
            override fun onRewardedVideoAdEnded() {}
            override fun onRewardedVideoAdRewarded(placement: com.ironsource.mediationsdk.model.Placement) {
                websiteUrl?.let { openWebsite(it) }
            }
            override fun onRewardedVideoAdShowFailed(error: IronSourceError?) {}
            override fun onRewardedVideoAdClicked(placement: com.ironsource.mediationsdk.model.Placement?) {}
        })
    }

    private fun showRewardedAd(url: String) {
        websiteUrl = url
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo()
        } else {
            Toast.makeText(this, "Ad not loaded yet. Opening website...", Toast.LENGTH_SHORT).show()
            openWebsite(url)
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(this, WebViewActivity::class.java).apply {
            putExtra("url", url)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        fbTopAdView.destroy()
        fbBottomAdView.destroy()
        ironSourceBanner?.let { IronSource.destroyBanner(it) }
        super.onDestroy()
    }
}
