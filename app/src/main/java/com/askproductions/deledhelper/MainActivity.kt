package com.askproductions.deledhelper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*

class MainActivity : AppCompatActivity() {

    private lateinit var adView: AdView
    private var rewardedVideoAd: RewardedVideoAd? = null
    private var websiteUrl: String? = null // Store URL to open after ad is shown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Facebook Audience Network
        AudienceNetworkAds.initialize(this)
        AdSettings.setTestMode(true)

        // Initialize and load rewarded video ad
        rewardedVideoAd = RewardedVideoAd(this, "3894902787424141_3894905060757247")
        rewardedVideoAd?.loadAd(
            rewardedVideoAd?.buildLoadAdConfig()?.withAdListener(object : RewardedVideoAdListener {
                override fun onError(ad: Ad?, error: AdError) {
                    Toast.makeText(this@MainActivity, "Error loading ad: ${error.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                override fun onAdLoaded(ad: Ad?) {
                    // Ad loaded successfully
                }

                override fun onAdClicked(ad: Ad?) {}
                override fun onLoggingImpression(ad: Ad?) {}

                override fun onRewardedVideoCompleted() {
                    websiteUrl?.let { openWebsite(it) } // Open the stored URL after ad completes
                }

                override fun onRewardedVideoClosed() {
                    // Reload the ad after it's closed
                    rewardedVideoAd?.loadAd()
                }
            })?.build()
        )

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

        // Set up Facebook Banner Ad
        adView = AdView(this, "3894902787424141_3894903337424086", AdSize.BANNER_HEIGHT_50)
        findViewById<LinearLayout>(R.id.adContainer).addView(adView)
        adView.loadAd()
    }

    // Method to show rewarded ad
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
        adView.destroy()
        rewardedVideoAd?.destroy()
        super.onDestroy()
    }
}
