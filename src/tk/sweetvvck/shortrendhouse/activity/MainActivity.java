package tk.sweetvvck.shortrendhouse.activity;

import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.WubaHouseListFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends MenuActivity {

	public WubaHouseListFragment houseListFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SlidingMenu sm = getSlidingMenu();
		houseListFragment = (WubaHouseListFragment) WubaHouseListFragment.getInstance(sm);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_layout, houseListFragment)
				.commit();
		setSlidingActionBarEnabled(false);
	}

	private long exitTime = 0;

	private MyWebView webview;

	@Override
	/**
	 * 当Menu打开的时候返回true不做处理，当Menu未打开时，如果当先的Flag不是首页即邮件列表页，点击返回键则返回到首页,然后再进行再按一次推出逻辑处理
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			webview = ((WubaHouseListFragment) WubaHouseListFragment
					.getInstance(getSlidingMenu())).getmWebView();
			if (webview != null && webview.canGoBack()) {
				webview.goBack();
				return true;
			}

			/**
			 * 如果菜单被划开，点击返回键则关闭菜单
			 */
			if (getSlidingMenu().isMenuShowing()) {
				return true;
			}

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
