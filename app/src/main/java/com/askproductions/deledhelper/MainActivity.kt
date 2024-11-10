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

class MainActivity : AppCompatActivity() {

    private lateinit var topAdView1: AdView
    private lateinit var topAdView2: AdView
    private lateinit var bottomAdView1: AdView
    private lateinit var bottomAdView2: AdView
    private var rewardedVideoAd: RewardedVideoAd? = null
    private var websiteUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Facebook Audience Network
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(false)

        // Check for internet connectivity and show a popup if there’s no connection
        if (!isInternetConnected()) {
            showNoInternetDialog()
        }

        // Load Rewarded Video Ad
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

        // Set up Facebook Banner Ads
        setupBannerAds()
    }

    // Method to check internet connectivity
    private fun isInternetConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Show dialog if no internet
    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    // Set up banner ads
    private fun setupBannerAds() {
        // Top banner ad 1
        topAdView1 = AdView(this, "3894902787424141_3894903337424086", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.topAdContainer1).addView(topAdView1)
        topAdView1.loadAd()

        // Top banner ad 2
        topAdView2 = AdView(this, "3894902787424141_3903266989921054", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.topAdContainer2).addView(topAdView2)
        topAdView2.loadAd()

        // Bottom banner ad 1
        bottomAdView1 = AdView(this, "3894902787424141_3903267293254357", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.bottomAdContainer1).addView(bottomAdView1)
        bottomAdView1.loadAd()

        // Bottom banner ad 2
        bottomAdView2 = AdView(this, "3894902787424141_3903267706587649", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.bottomAdContainer2).addView(bottomAdView2)
        bottomAdView2.loadAd()
    }

    // Method to set up rewarded ad
    private fun setupRewardedAd() {
        rewardedVideoAd = RewardedVideoAd(this, "3894902787424141_3894905060757247")
        rewardedVideoAd?.loadAd(
            rewardedVideoAd?.buildLoadAdConfig()?.withAdListener(object : RewardedVideoAdListener {
                override fun onError(ad: Ad?, error: AdError) {
                    Toast.makeText(this@MainActivity, "Error loading ad: ${error.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onAdLoaded(ad: Ad?) {}
                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}

                override fun onRewardedVideoCompleted() {
                    websiteUrl?.let { openWebsite(it) }
                }

                override fun onRewardedVideoClosed() {
                    rewardedVideoAd?.loadAd()
                }
            })?.build()
        )
    }

    private fun showRewardedAd(url: String) {
        websiteUrl = url
        if (rewardedVideoAd?.isAdLoaded == true) {
            rewardedVideoAd?.show()
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
        topAdView1.destroy()
        topAdView2.destroy()
        bottomAdView1.destroy()
        bottomAdView2.destroy()
        rewardedVideoAd?.destroy()
        super.onDestroy()
    }
}