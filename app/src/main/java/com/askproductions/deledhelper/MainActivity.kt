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
    private var ironSourceBanner: IronSourceBannerLayout? = null
    private var websiteUrl: String? = null // URL to open after user closes ad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Facebook Audience Network
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(true) // Use true for testing, false for production

        // Initialize IronSource
        IronSource.init(this, "2020eb075") // Replace with your IronSource app key

        // Check for internet connectivity
        if (!isInternetConnected()) {
            showNoInternetDialog()
        }

        // Set up banner and rewarded ads
        setupBannerAds()
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
            override fun onBannerAdLoaded() {}
            override fun onBannerAdLoadFailed(error: IronSourceError?) {}
            override fun onBannerAdClicked() {}
            override fun onBannerAdScreenPresented() {}
            override fun onBannerAdScreenDismissed() {}
            override fun onBannerAdLeftApplication() {}
        }

        ironSourceBanner?.let { IronSource.loadBanner(it) }
    }

    private fun setupRewardedAd() {
        IronSource.setRewardedVideoListener(object : RewardedVideoListener {
            override fun onRewardedVideoAdOpened() {
                Toast.makeText(this@MainActivity, "Ad started. Please watch to unlock the website.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdClosed() {
                // Open the website manually after ad closes
                websiteUrl?.let {
                    Toast.makeText(this@MainActivity, "Ad completed. Opening website...", Toast.LENGTH_SHORT).show()
                    openWebsite(it)
                }
                websiteUrl = null // Reset the URL after opening
            }

            override fun onRewardedVideoAvailabilityChanged(available: Boolean) {
                if (!available) {
                    Toast.makeText(this@MainActivity, "Rewarded ads are currently unavailable.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onRewardedVideoAdStarted() {}

            override fun onRewardedVideoAdEnded() {}

            override fun onRewardedVideoAdRewarded(placement: com.ironsource.mediationsdk.model.Placement) {
                // Mark ad completion as successful
                Toast.makeText(this@MainActivity, "Thank you for watching! You can now proceed.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdShowFailed(error: IronSourceError?) {
                Toast.makeText(this@MainActivity, "Failed to show ad: ${error?.errorMessage}", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdClicked(placement: com.ironsource.mediationsdk.model.Placement?) {}
        })
    }

    private fun showRewardedAd(url: String) {
        websiteUrl = url
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo()
        } else {
            // Notify user that ad is not available
            Toast.makeText(this, "Ad not available. Please try again later.", Toast.LENGTH_SHORT).show()
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
        ironSourceBanner?.let { IronSource.destroyBanner(it) }
        super.onDestroy()
    }
}
