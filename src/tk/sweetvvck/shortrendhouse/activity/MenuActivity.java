package tk.sweetvvck.shortrendhouse.activity;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.RentHouseSideMenuFragment;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MenuActivity extends SlidingFragmentActivity {

	protected RentHouseSideMenuFragment mFrag;
	public static int mCurrentFlag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			mFrag = new RentHouseSideMenuFragment(getSlidingMenu());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame, mFrag).commit();
		} else {
			mFrag = (RentHouseSideMenuFragment) this
					.getSupportFragmentManager().findFragmentById(
							R.id.menu_frame);
			mFrag.setSm(getSlidingMenu());
		}
		initSlidingMenu();
	}

	/**
	 * 初始化SlidingMenu
	 */
	private void initSlidingMenu() {
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(1.0f);
		sm.setBehindScrollScale(0);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				// TODO
				switch (mCurrentFlag) {
				case -1:
					function.setVisible(true);
					break;
				case 0:
					// TODO 58同城界面的Actionbar
					function.setVisible(false);
					break;

				case 1:
					// TODO 赶集网界面的Actionbar
					function.setVisible(false);
					break;

				case 2:
					// 日历界面的actionbar
					break;

				default:
					break;
				}
			}
		});
	}

	private MenuItem function;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		function = menu.findItem(R.id.actionbar_function);
		return true;
	}

	// ActionBar MenuItem点击事件
	private ActionBarListener actionBarListener;

	public interface ActionBarListener {
		public void onItemClick(int MenuItemID);
	}

	public void setActionBarListener(ActionBarListener actionBarListener) {
		this.actionBarListener = actionBarListener;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		actionBarListener.onItemClick(itemId);
		return true;
	}
}
