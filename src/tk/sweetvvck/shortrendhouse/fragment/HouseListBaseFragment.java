package tk.sweetvvck.shortrendhouse.fragment;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Mail、Calendar、ContactsFragment的父类
 * 生命周期依次是init()、initViews()、initActionBarListener()、bandData4View()方法
 * @author 程科
 */
public abstract class HouseListBaseFragment extends Fragment {

	protected static SlidingMenu _sm;
	protected Activity activity;
	protected ProgressDialog progressDialog;
	protected View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this instanceof VVHouseListFragment) {
			rootView = inflater.inflate(R.layout.mail_list_activity, container, false);
		} 
		init(inflater);
		return rootView;
	}

	/**
	 * 初始化Fragment展示内容
	 * @param inflater
	 */
	public void init(LayoutInflater inflater) {
		initViews(inflater);
		initActionBarListener();
		bandData4View();
	}

	/**
	 * 初始化布局
	 */
	public abstract void initViews(LayoutInflater inflater);

	/**
	 * 初始化actionbarListener
	 */
	public abstract void initActionBarListener();

	/**
	 * 为布局绑定数据
	 */
	public void bandData4View() {
		progressDialog = Utils.createSimpleProgressDialog(progressDialog,
				getActivity(), "Loading");
	}

	/**
	 * 初始化监听器
	 */
	public abstract void initListener();
}