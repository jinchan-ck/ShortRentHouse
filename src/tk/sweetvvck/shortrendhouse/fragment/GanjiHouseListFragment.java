package tk.sweetvvck.shortrendhouse.fragment;

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

import de.passsy.holocircularprogressbar.HoloCircularProgressBar;

public class GanjiHouseListFragment extends Fragment {
	public static Activity activity;

	public static Fragment instance;

	@SuppressWarnings("unused")
	private static SlidingMenu _sm;

	public MyWebView mWebView;
	private Dialog progressDialog;
	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public HoloCircularProgressBar getProgressbar() {
		return progressbar;
	}

	private HoloCircularProgressBar progressbar;

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
		mWebView.loadUrl("http://www.3g.ganji.com/bj_fang10/");
		progressDialog = new Dialog(activity, R.style.myDialogTheme);
		View progressView = inflater.inflate(R.layout.progressbar, null);
		progressbar = (HoloCircularProgressBar) progressView.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
		return rootView;
	}

	public static Fragment getInstance(SlidingMenu sm) {
		if (instance == null) {
			instance = new GanjiHouseListFragment();
		}
		_sm = sm;
		return instance;
	}
}