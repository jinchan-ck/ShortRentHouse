package tk.sweetvvck.shortrendhouse.fragment;

import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.MenuActivity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
		initActionBarListener();
		return rootView;
	}

	public void initActionBarListener() {
		((MenuActivity) getActivity())
				.setActionBarListener(new MenuActivity.ActionBarListener() {
					@Override
					public void onItemClick(int MenuItemID) {
						switch (MenuItemID) {
						// 刷新按钮点击事件
						case R.id.actionbar_function:
							Toast.makeText(getActivity(), "58 refresh",
									Toast.LENGTH_SHORT).show();
							mWebView.loadUrl("http://webapp.58.com/bj/duanzu/?formatsource=list_topcate&cversion=4.4.0.0&androidtype=centerhisfilter;os=android");
							break;
//						case R.id.action_write:
//							Intent intent = new Intent(getActivity(),
//									EditMailActivity.class);
//							getActivity().startActivity(intent);
//							break;
						default:
							break;
						}
					}
				});

		/**
		 * 注册这个监听器用于通知slidingmenu的CustomViewAbove是否拦截当前触摸事件，不向子View（
		 * SwipeListView）传递
		 */
//		_sm.setCustomInterceptTouchEventListener(new CustomInterceptTouchEventListener() {
//
//			@Override
//			public boolean interceptOrNot(CustomViewAbove customViewAbove,
//					MotionEvent ev) {
//				Log.d("swipe", String.format("onTouch %d", mPosition));
//				if (mPosition < 0)
//					return true;
//				if (notInterceptFlag && data != null && !data.isEmpty()
//						&& data.get(mPosition).isOpenedFlag()) {
//					return false;
//				} else {
//					return true;
//				}
//			}
//
//			@Override
//			public void onDrag() {
//				swipeListView.closeOpenedItems();
//			}
//		});
	}	
	
	public static Fragment getInstance(SlidingMenu sm) {
		if (instance == null) {
			instance = new WubaHouseListFragment();
		}
		_sm = sm;
		return instance;
	}
}
