package tk.sweetvvck.shortrendhouse.fragment;

import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.shortrendhouse.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class WubaHouseListFragment extends Fragment {
	public static Activity activity;

	public static Fragment instance;

	private static SlidingMenu _sm;

	public MyWebView mWebView;
	private Dialog progressDialog;
	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public LoadingCircleView getProgressbar() {
		return progressbar;
	}

	private LoadingCircleView progressbar;

	public MyWebView getmWebView() {
		return mWebView;
	}

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.house_list, container, false);
		activity = getActivity();
		mWebView = (MyWebView) rootView.findViewById(R.id.m_webview);
		mWebView.loadUrl("http://webapp.58.com/bj/duanzu/?formatsource=list_topcate&cversion=4.4.0.0&androidtype=centerhisfilter;os=android");
		progressDialog = new Dialog(activity, R.style.myDialogTheme);
		View progressView = inflater.inflate(R.layout.progressbar, null);
		progressbar = (LoadingCircleView) progressView.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
		_sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		return rootView;
	}

	public static Fragment getInstance(SlidingMenu sm) {
		if (instance == null) {
			instance = new WubaHouseListFragment();
		}
		_sm = sm;
		return instance;
	}
}
