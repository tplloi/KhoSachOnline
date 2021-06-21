package loitp.com.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.core.utilities.LAppResource;
import com.views.LToast;
import com.wang.avi.AVLoadingIndicatorView;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import loitp.com.R;
import loitp.com.util.Const;
import loitp.com.util.Help;
import loitp.com.view.LWebView;

public class AsyncTaskRead extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private String link;
    private LWebView wv;
    private AVLoadingIndicatorView avloadingIndicatorView;
    private int positionWebview;
    private String strHTML;

    // 0 doInBkg ok
    // 1 err
    private int state = 0;

    public AsyncTaskRead(Context mContext, String link, LWebView wv, int positionWebview) {
        this.mContext = mContext;
        // this.help = new Help(mContext);
        this.link = link;
        this.wv = wv;
        this.positionWebview = positionWebview;
        this.avloadingIndicatorView = ((Activity) mContext).findViewById(R.id.avLoadingIndicatorView);
    }

    @Override
    protected void onPreExecute() {
        wv.setVisibility(View.INVISIBLE);
        wv.clearView();
        avloadingIndicatorView.setVisibility(View.VISIBLE);

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        final String newPage;
        try {
            boolean swReadNightModeState = Help.getBooleanFromData(Const.SETTING_READ_NIGHT_MODE);
            String theme = "";
            if (swReadNightModeState) {
                theme = "color:black; background-color: white;";
            } else {
                theme = "color:white; background-color: black;";
            }

            Document document = Jsoup.connect(link).get();

            // Elements newsRawTag = doc.select("div#content-home");
            Elements newsRawTag = document.select("div[class=story-detail-content]");
            newPage = newsRawTag.html();

            HtmlCleaner htmlCleaner = new HtmlCleaner();
            CleanerProperties cleanerProp = htmlCleaner.getProperties();
            TagNode tagNode = new HtmlCleaner(cleanerProp).clean(newPage);
            SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(cleanerProp);

//       String strPish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/"
//       + "font6.ttf" + "\")}body {font-family: MyFont;font-size: medium;text-align: justify;" + theme
//       + "}</style></head><body>";


            String strPish = "<html><head><style type=\"text/css\">body {text-align: justify;" + theme
                    + "}</style></head><body>";

            String strPas = "<br/><br/><br/><br/></html>";
            strHTML = strPish
                    + "<br/><br/></br></br> Cám ơn vì đã sử dụng ứng dụng của mình. Chúc các bạn luôn đọc sách vui vẻ:)"
                    + htmlSerializer.getAsString(tagNode) + strPas;

            // LLog.d(BUG, strHTML);

        } catch (Exception e) {
            // LLog.d(BUG, "err 1: " + e.toString());
            state = 1;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        wv.setVisibility(View.VISIBLE);


        avloadingIndicatorView.setVisibility(View.GONE);

        if (state == 1)
            LToast.show(LAppResource.application.getString(R.string.err_ko_tai_dc_data));
        else {
            wv.loadDataWithBaseURL(null, strHTML, "text/html", "charset=UTF-8", null);

            wv.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    wv.scrollTo(0, positionWebview);
                    //LLog.d(TAG, "positionWebview: " + positionWebview);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });
        }

        super.onPostExecute(result);
    }
}
