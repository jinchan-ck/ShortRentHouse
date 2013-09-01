package tk.sweetvvck.shortrendhouse.activity;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.MailSideMenuFragment;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MenuActivity extends SlidingFragmentActivity {

	protected MailSideMenuFragment mFrag;
	public static int mCurrentFlag = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			mFrag = new MailSideMenuFragment(getSlidingMenu());
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, mFrag).commit();
		} else {
			mFrag = (MailSideMenuFragment) this.getSupportFragmentManager()
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
