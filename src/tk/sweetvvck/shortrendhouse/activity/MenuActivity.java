package tk.sweetvvck.shortrendhouse.activity;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.RentHouseSideMenuFragment;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, mFrag).commit();
		} else {
			mFrag = (RentHouseSideMenuFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
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
	}
}
