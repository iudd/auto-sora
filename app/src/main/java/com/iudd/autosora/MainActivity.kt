package com.iudd.autosora

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iudd.autosora.data.AppDatabase
import com.iudd.autosora.data.VideoLink
import com.iudd.autosora.extractor.VideoLinkExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlInput: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnBack: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnRefresh: ImageButton
    private lateinit var fabExtract: FloatingActionButton

    private lateinit var database: AppDatabase
    private lateinit var videoLinkExtractor: VideoLinkExtractor

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize database and extractor
        database = AppDatabase.getDatabase(this)
        videoLinkExtractor = VideoLinkExtractor()

        // Initialize views
        initializeViews()

        // Setup WebView
        setupWebView()

        // Setup event listeners
        setupEventListeners()

        // Load default page
        loadDefaultPage()
    }

    private fun initializeViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        webView = findViewById(R.id.webview)
        urlInput = findViewById(R.id.url_input)
        progressBar = findViewById(R.id.progress_bar)
        btnBack = findViewById(R.id.btn_back)
        btnForward = findViewById(R.id.btn_forward)
        btnRefresh = findViewById(R.id.btn_refresh)
        fabExtract = findViewById(R.id.fab_extract)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false // Let WebView handle the URL
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                urlInput.setText(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                updateNavigationButtons()
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }
    }

    private fun setupEventListeners() {
        // URL input
        urlInput.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                navigateToUrl()
                return@setOnKeyListener true
            }
            false
        }

        // Navigation buttons
        btnBack.setOnClickListener { if (webView.canGoBack()) webView.goBack() }
        btnForward.setOnClickListener { if (webView.canGoForward()) webView.goForward() }
        btnRefresh.setOnClickListener { webView.reload() }

        // Go button
        findViewById<View>(R.id.btn_go).setOnClickListener { navigateToUrl() }

        // Extract links FAB
        fabExtract.setOnClickListener { extractVideoLinks() }
    }

    private fun navigateToUrl() {
        val url = urlInput.text.toString().trim()
        if (url.isNotEmpty()) {
            val finalUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            webView.loadUrl(finalUrl)
        }
    }

    private fun loadDefaultPage() {
        webView.loadUrl("https://www.baidu.com")
    }

    private fun updateNavigationButtons() {
        btnBack.isEnabled = webView.canGoBack()
        btnBack.alpha = if (webView.canGoBack()) 1.0f else 0.5f

        btnForward.isEnabled = webView.canGoForward()
        btnForward.alpha = if (webView.canGoForward()) 1.0f else 0.5f
    }

    private fun extractVideoLinks() {
        lifecycleScope.launch {
            try {
                // Show loading
                fabExtract.isEnabled = false
                Toast.makeText(this@MainActivity, "正在提取视频链接...", Toast.LENGTH_SHORT).show()

                // Extract from WebView
                videoLinkExtractor.extractFromWebView(webView) { webViewLinks ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            // Save to database
                            database.videoLinkDao().insertVideoLinks(webViewLinks)

                            withContext(Dispatchers.Main) {
                                val count = webViewLinks.size
                                Toast.makeText(
                                    this@MainActivity,
                                    "成功提取并保存了 $count 个视频链接",
                                    Toast.LENGTH_LONG
                                ).show()
                                fabExtract.isEnabled = true
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "保存失败: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                fabExtract.isEnabled = true
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "提取失败: ${e.message}", Toast.LENGTH_SHORT).show()
                fabExtract.isEnabled = true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}