package tk.sweetvvck.shortrendhouse.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;

import tk.sweetvvck.constants.SocialConfig;
import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.entity.SinaResp;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.RentHouseSideMenuFragment;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.MySharedPreferences;
import tk.sweetvvck.utils.WebTool;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class LoginActivity extends SlidingFragmentActivity {

	private Weibo mWeibo;

	public static Oauth2AccessToken accessToken;

	/**
	 * SsoHandler 仅当sdk支持sso时有效，
	 */
	private SsoHandler mSsoHandler;

	private Context context;

	private Dialog progressDialog;

	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public LoadingCircleView getProgressbar() {
		return progressbar;
	}

	private LoadingCircleView progressbar;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				SinaResp resp = (SinaResp) msg.obj;
				_rentMenu.changeCurrentView(_rentMenu.getMenu_function(), null, -1, resp);
				LoginActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};
	
	private static RentHouseSideMenuFragment _rentMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.login);
		setBehindContentView(R.layout.new_page_behind);
		slidingMenuInit();
		mWeibo = Weibo.getInstance(SocialConfig.APP_KEY,
				SocialConfig.REDIRECT_URL);
		accessToken = AccessTokenKeeper.readAccessToken(context);
		if (accessToken.isSessionValid()) {
			String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.format(new java.util.Date(accessToken.getExpiresTime()));
			System.out.println(("access_token 仍在有效期内,无需再次登录: \naccess_token:"
					+ accessToken.getToken() + "\n有效期：" + date));
		} else {
			System.out.println(("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
					+ "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证"));
		}
		progressDialog = new Dialog(this, R.style.myDialogTheme);
		View progressView = getLayoutInflater().inflate(R.layout.progressbar,
				null);
		progressbar = (LoadingCircleView) progressView
				.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
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
				LoginActivity.this.finish();
			}
		});
	}

	public void sinaLogin(View v) {
		mSsoHandler = new SsoHandler(this, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener());
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {

			String code = values.getString("code");
			if (code != null) {
				Toast.makeText(context, "认证code成功", Toast.LENGTH_SHORT).show();
				return;
			}
			String uid = values.getString("uid");
			MySharedPreferences.put_String("uid", uid);
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (LoginActivity.accessToken.isSessionValid()) {
				@SuppressWarnings("unused")
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(LoginActivity.accessToken
								.getExpiresTime()));

				AccessTokenKeeper.keepAccessToken(context, accessToken);
				Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show();
				String baseUrl = "https://api.weibo.com/2/users/show.json";
				final String url = baseUrl + "?uid=" + uid + "&access_token=" + accessToken.getToken();
				//TODO 成功的回调
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							String result = WebTool.getDataFromUrl(url);
							Gson json = new Gson();
							SinaResp resp = json.fromJson(result, SinaResp.class);
							if(resp != null && resp.getAvatar_large() != null){
								MySharedPreferences.put_String("avatarUrl", resp.getAvatar_large());
							}
							if(resp != null && resp.getName() != null){
								MySharedPreferences.put_String("username", resp.getName());
							}
							Message msg = new Message();
							msg.what = 0;
							msg.obj = resp;
							handler.sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(context, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(context, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	public static Intent createLoginIntent(
			RentHouseSideMenuFragment rentHouseSideMenuFragment) {
		Intent intent = new Intent(rentHouseSideMenuFragment.getActivity(), LoginActivity.class);
		_rentMenu = rentHouseSideMenuFragment;
		return intent;
	}
}
