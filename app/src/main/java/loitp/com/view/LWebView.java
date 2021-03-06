package loitp.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class LWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public LWebView(final Context context) {
        super(context);
    }

    public LWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public LWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null)
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);

    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int l, int t, int oldl, int oldt);
    }
}