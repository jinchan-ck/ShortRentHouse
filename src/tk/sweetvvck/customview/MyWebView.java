/**
 * 
 */
package tk.sweetvvck.customview;

import tk.sweetvvck.external.External;
import tk.sweetvvck.shortrendhouse.activity.HouseDetailActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author 程科
 *
 */
public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
		init();
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyWebView(Context context, boolean isGame) {
		super(context);
		init();
		// this.isGame = isGame;
	}

	private void init() {
		WebSettings setting = this.getSettings();
		setting.setSavePassword(true);
		setting.setJavaScriptEnabled(true);// 支持JS
		setting.setLoadWithOverviewMode(true);
		//setting.setUserAgentString("os=android");在本应用中不要添加，可能是58的屏蔽
		setClickable(true);
		setFocusable(true);
		setLongClickable(true);

		//使用localStorage则必须打开  
		setting.setDomStorageEnabled(true);

		setHorizontalScrollBarEnabled(false);
		setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		this.setWebViewClient(new MyWebViewClient(this.getContext()));
		this.setWebChromeClient(new MyWebChromeClient(this.getContext()));

		this.addJavascriptInterface(new External(this), "stub");
		this.setChildrenDrawingCacheEnabled(true);
		this.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_LOW);
	}

	protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar,
			int l, int t, int r, int b) {
		scrollBar.setColorFilter(0xffff0000, Mode.DST_IN);
		scrollBar.setBounds(l, t, r, b);
		scrollBar.draw(canvas);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void loadUrl(final String url) {
		try {
			Context c = this.getContext();
			if(url.startsWith("http://i.m.")){
				Intent intent = new Intent(c, HouseDetailActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("channel", "wuba");
				c.startActivity(intent);
				return;
			}
			if (c instanceof Activity) {
				((Activity) c).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MyWebView.super.loadUrl(url);
					}
				});
			} else {
				super.loadUrl(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
	}
}
