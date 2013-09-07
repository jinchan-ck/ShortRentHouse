package tk.sweetvvck.shortrendhouse.activity;

import java.io.IOException;

import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.external.External;
import tk.sweetvvck.shortrendhouse.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class VVHouseDetailActivity extends SlidingFragmentActivity {

	@SuppressWarnings("unused")
	private Context context;
	private MyWebView webview;

	private Dialog progressDialog;

	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public LoadingCircleView getProgressbar() {
		return progressbar;
	}

	private LoadingCircleView progressbar;

	public MyWebView getmWebView() {
		return webview;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				/** 使用loaddatawithbaseurl解决中文乱码 */
				webview.loadDataWithBaseURL("", data, "text/html", "utf-8", "");
				break;
			default:
				break;
			}
		}
	};
	private String url;
	private String data = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		actionBar
				.setIcon(getResources().getDrawable(R.drawable.actionbar_back));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);

		setContentView(R.layout.house_detail);
		setBehindContentView(R.layout.new_page_behind);
		slidingMenuInit();
		progressDialog = new Dialog(this, R.style.myDialogTheme);
		View progressView = getLayoutInflater().inflate(R.layout.progressbar,
				null);
		progressbar = (LoadingCircleView) progressView
				.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
		progressDialog.show();
		webview = (MyWebView) this.findViewById(R.id.house_list_webview);

		Intent intent = getIntent();
		String channel = intent.getStringExtra("channel");
		url = intent.getStringExtra("url");
		if (channel != null && channel.equalsIgnoreCase("wuba")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						data = External.remove58(url);
						handler.sendEmptyMessage(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}else if(channel != null && channel.equalsIgnoreCase("ganji")){
			webview.loadUrl(url);
		}
	}

	/**
	 * 初始化SlidingMenu
	 */
	private void slidingMenuInit() {
		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setMode(SlidingMenu.LEFT);
		sm.setFadeEnabled(false);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.zero_offset);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(1.0f);
		sm.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
			@Override
			public void onOpened() {
				VVHouseDetailActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			toggle();
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.house_detail_actionbar, menu);
		return true;
	}
}
