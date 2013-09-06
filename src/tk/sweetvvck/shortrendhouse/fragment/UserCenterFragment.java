package tk.sweetvvck.shortrendhouse.fragment;

import java.io.IOException;

import tk.sweetvvck.customview.ListViewPro;
import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.entity.SinaResp;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.LoginActivity;
import tk.sweetvvck.shortrendhouse.activity.MenuActivity;
import tk.sweetvvck.shortrendhouse.activity.MenuActivity.ActionBarListener;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.FileOptService;
import tk.sweetvvck.utils.Utils;
import tk.sweetvvck.utils.WebTool;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.weibo.sdk.android.Oauth2AccessToken;

public class UserCenterFragment extends Fragment {
	public static Activity activity;

	public static Fragment instance;

	private ImageView imgAvatar;
	private TextView tvUsername;

	private static SlidingMenu _sm;

	private Dialog progressDialog;

	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public LoadingCircleView getProgressbar() {
		return progressbar;
	}

	private LoadingCircleView progressbar;

	private View rootView;

	private ListViewPro lvBackground;
	public static View itemHead1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		rootView = inflater.inflate(R.layout.user_center, container, false);
		lvBackground = (ListViewPro) rootView.findViewById(R.id.lv_background);
		itemHead1 = inflater.inflate(R.layout.user_center_header, null);
		lvBackground.addHeaderView(itemHead1);
		lvBackground.setAdapter(new BackgroundAdapter());
		imgAvatar = (ImageView) itemHead1.findViewById(R.id.avatar);
		tvUsername = (TextView) itemHead1.findViewById(R.id.tv_username);
		progressDialog = new Dialog(activity, R.style.myDialogTheme);
		View progressView = inflater.inflate(R.layout.progressbar, null);
		progressbar = (LoadingCircleView) progressView
				.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
		setUserInfo();
		imgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Oauth2AccessToken accessToken = AccessTokenKeeper
						.readAccessToken(activity);
				if (!accessToken.isSessionValid()) {
					Intent intent = LoginActivity.createLoginIntent(_rentMenu);
					activity.startActivity(intent);
				}
			}
		});
		initActionBarListener();
		return rootView;
	}

	class BackgroundAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img = new ImageView(activity);
			img.setBackground(getResources()
					.getDrawable(R.drawable.ic_launcher));
			return img;
		}
	}

	private Drawable avatarDrawable;

	final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap bmp = Utils
						.getRoundedCornerBitmap(((BitmapDrawable) avatarDrawable)
								.getBitmap());
				tvUsername.setText(_resp.getName());
				imgAvatar.setBackground(null);
				imgAvatar.setImageBitmap(bmp);
				_rentMenu.getImgAvatar().setImageBitmap(bmp);
				_rentMenu.getTvUsername().setText(_resp.getName());
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;

			default:
				break;
			}
		}
	};

	public void setUserInfo() {
		if (progressDialog != null)
			progressDialog.show();
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (_resp != null) {
					String avatarUrl = _resp.getAvatar_large();

					avatarDrawable = _rentMenu.getAvatarDrawable();

					if (avatarDrawable == null
							|| avatarDrawable == activity.getResources()
									.getDrawable(R.drawable.people_icon0)) {
						if (avatarUrl != null) {
							if (Utils.isNetworkConnected(activity)) {
								avatarDrawable = WebTool
										.loadImageFromNetwork(avatarUrl);
								if (avatarDrawable != null) {
									try {
										FileOptService.saveImage("avatar.jpg",
												avatarDrawable);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									avatarDrawable = new BitmapDrawable(
											FileOptService.readImage("avatar.jpg"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} else {
							avatarDrawable = getResources().getDrawable(
									R.drawable.people_icon0);
						}
						if (avatarDrawable == null) {
							avatarDrawable = getResources().getDrawable(
									R.drawable.people_icon0);
						}
					}
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	private void initActionBarListener() {
		((MenuActivity) getActivity())
				.setActionBarListener(new ActionBarListener() {

					@Override
					public void onItemClick(int MenuItemID) {
						switch (MenuItemID) {
						case R.id.actionbar_function:
							AccessTokenKeeper.clear(activity);
							_sm.toggle();
							Bitmap bmp = Utils
									.getRoundedCornerBitmap(((BitmapDrawable) getResources()
											.getDrawable(
													R.drawable.people_icon0))
											.getBitmap());
							imgAvatar.setImageBitmap(bmp);
							tvUsername.setText("点击登录");
							_rentMenu.getImgAvatar().setImageBitmap(bmp);
							_rentMenu.getTvUsername().setText("点击登录");
							break;

						default:
							break;
						}
					}
				});
	}

	private static SinaResp _resp;
	private static RentHouseSideMenuFragment _rentMenu;

	public static Fragment getInstance(SlidingMenu sm, SinaResp resp,
			RentHouseSideMenuFragment rentMenu) {
		instance = new UserCenterFragment();
		_sm = sm;
		_resp = resp;
		_rentMenu = rentMenu;
		return instance;
	}
}
