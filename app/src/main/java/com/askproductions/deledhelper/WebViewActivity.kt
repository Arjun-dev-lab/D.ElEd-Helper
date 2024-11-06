package com.askproductions.deledhelper

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // Get the URL passed from the MainActivity or use a default one
        val url = intent.getStringExtra("url") ?: "https://www.google.com"
        val webView = findViewById<WebView>(R.id.webView)

        // Set the WebViewClient to open links inside the WebView
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // Load the URL in the WebView
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webView)

        // Check if the WebView can go back in its history
        if (webView.canGoBack()) {
            webView.goBack() // Go back to the previous page in history
        } else {
            super.onBackPressed() // If not, perform default back action (exit activity)
        }
    }
}
