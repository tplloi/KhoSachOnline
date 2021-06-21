package loitp.com.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebSettings.RenderPriority
import android.webkit.WebView
import android.webkit.WebViewClient
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.google.android.gms.ads.AdRequest
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_read.*
import loitp.com.R
import loitp.com.db.SQLiteHelper
import loitp.com.model.OneChap
import loitp.com.service.AsyncTaskRead
import loitp.com.util.Const
import loitp.com.util.Help.getBooleanFromData
import loitp.com.util.Help.getIntFromData
import loitp.com.view.LWebView.OnScrollChangedCallback

@LogTag("MainActivity")
@IsFullScreen(false)
class ActRead : BaseFontActivity() {
    companion object {
        const val KEY_POSITION = "position"
        const val KEY_POSITION_WEBVIEW = "positionWebview"
        const val KEY_EXTRA = "extra"
        const val KEY_OBJECT = "objects"
    }

    private var mPos = 0 // position of ArrayList<OneChap>
    private var listChap = ArrayList<OneChap>()
    private var positionWebview = 0
    private var asyncTaskRead: AsyncTaskRead? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_read
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        mPos = intent.getIntExtra(KEY_POSITION, 0)
        positionWebview = intent.getIntExtra(KEY_POSITION_WEBVIEW, 0)
        val extra = intent.getBundleExtra(KEY_EXTRA)
        extra?.getSerializable(KEY_OBJECT)?.let { list ->
            if (list is ArrayList<*>) {
                list.forEach {
                    if (it is OneChap) {
                        listChap.add(it)
                    }
                }
            }
        }
        loadAllBook(mPos)
    }

    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView.destroy()
        asyncTaskRead?.cancel(true)
        super.onDestroy()
    }

    private fun setupViews() {
        setupWebView()
        adView.loadAd(
            AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("33F2CB83BAADAD6C").addTestDevice("8FA8E91902B43DCB235ED2F6BBA9CAE0")
                .addTestDevice("7DA8A5B216E868636B382A7B9756A4E6")
                .addTestDevice("179198315EB7B069037C5BE8DEF8319A")
                .addTestDevice("A1EC01C33BD69CD589C2AF605778C2E6").build()
        )
        wv.onScrollChangedCallback = object : OnScrollChangedCallback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                positionWebview = oldt
                if (oldt > t) {
                    if (lnHead.visibility == View.GONE) {
                        lnHead.visibility = View.VISIBLE
                    }
                } else {
                    if (lnHead.visibility == View.VISIBLE) {
                        lnHead.visibility = View.GONE
                    }
                }
            }
        }
        btBack.setSafeOnClickListener {
            onBackPressed()
        }
        btPrev.setSafeOnClickListener {
            if (mPos > 0) {
                mPos--
                loadAllBook(mPos)
            } else {
                showShortInformation(getString(R.string.chuong_dau_tien))
            }
        }
        btZoomOut.setSafeOnClickListener {
            zoomOut()
        }
        btMark.setSafeOnClickListener {
            savePositionChapToBookFavourite(true)
        }
        btZoomIn.setSafeOnClickListener {
            zoomIn()
        }
        btNext.setSafeOnClickListener {
            if (mPos < listChap.size - 1) {
                mPos++
                loadAllBook(mPos)
            } else {
                showShortInformation(getString(R.string.chuong_cuoi_cung))
            }
        }
        if (getBooleanFromData(Const.READ_FROM_FAVOURITE)) {
            lnMark.visibility = View.VISIBLE
        } else {
            lnMark.visibility = View.GONE
        }
    }

    private fun loadAllBook(position: Int) {
        val titleChap = listChap[position].title
        tvTitle.text = titleChap
        val url = "http://thichtruyen.vn" + listChap[position].link
        asyncTaskRead?.cancel(true)
        asyncTaskRead = AsyncTaskRead(
            link = url,
            positionWebview = positionWebview,
            onPreExecute = {
                wv?.visibility = View.INVISIBLE
                wv?.clearView()
                avLoadingIndicatorView?.visibility = View.VISIBLE
            },
            onPostExecute = { strHTML, positionWebview ->
                wv?.visibility = View.VISIBLE
                avLoadingIndicatorView?.visibility = View.GONE
                if (strHTML.isNullOrEmpty()) {
                    showShortInformation(getString(R.string.err_ko_tai_dc_data))
                } else {
                    wv?.loadDataWithBaseURL(null, strHTML, "text/html", "charset=UTF-8", null)
                    wv?.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            wv.scrollTo(0, positionWebview)
                        }

                        override fun onReceivedError(
                            view: WebView,
                            errorCode: Int,
                            description: String,
                            failingUrl: String
                        ) {
                        }
                    }
                }
            }
        )
        asyncTaskRead?.execute()
    }

    override fun onResume() {
        adView.resume()
        super.onResume()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = wv.settings
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.setRenderPriority(RenderPriority.HIGH)
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.setAppCacheEnabled(false)
        webSettings.loadsImagesAutomatically = true
        webSettings.setGeolocationEnabled(false)
        webSettings.setNeedInitialFocus(false)
        webSettings.saveFormData = false
        webSettings.builtInZoomControls = true
        wv.settings.javaScriptEnabled = true

        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= 10) {
            // neu api <=10
        } else {
            // neu api >10 thi an nut zoom di
            webSettings.displayZoomControls = false
        }
    }

    private fun savePositionChapToBookFavourite(showToast: Boolean) {
        val sqLiteHelper = SQLiteHelper()
        sqLiteHelper.updateBook(getIntFromData(Const.FAVOURITE_BOOK), mPos, positionWebview)
        if (showToast) {
            showShortInformation(getString(R.string.marked) + ": " + (mPos + 1))
        }
    }

    private fun zoomIn() {
        val settings = wv.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= 14) {
            // api <=14
            settings.textSize = WebSettings.TextSize.LARGER
        } else {
            // api>14
            settings.textZoom = (settings.textZoom * 1.1).toInt()
        }
    }

    private fun zoomOut() {
        val settings = wv.settings
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion <= 14) {
            // api <=14
            settings.textSize = WebSettings.TextSize.SMALLEST
        } else {
            // api>14
            settings.textZoom = (settings.textZoom / 1.1).toInt()
        }
    }

    override fun onBackPressed() {
        if (getBooleanFromData(Const.READ_FROM_FAVOURITE)) {
            savePositionChapToBookFavourite(false)
        }
        super.onBackPressed()
    }
}
