package tk.sweetvvck.shortrendhouse.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tk.sweetvvck.entity.SinaResp;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.LoginActivity;
import tk.sweetvvck.shortrendhouse.activity.MainActivity;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.FileOptService;
import tk.sweetvvck.utils.MySharedPreferences;
import tk.sweetvvck.utils.Utils;
import tk.sweetvvck.utils.WebTool;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.weibo.sdk.android.Oauth2AccessToken;

public class RentHouseSideMenuFragment extends Fragment {
	private Context context;
	private LinearLayout layout = null;
	private ListView menu_function;

	public static Oauth2AccessToken accessToken;

	private ImageView imgAvatar;

	public ImageView getImgAvatar() {
		return imgAvatar;
	}

	public void setImgAvatar(ImageView imgAvatar) {
		this.imgAvatar = imgAvatar;
	}

	public TextView getTvUsername() {
		return tvUsername;
	}

	public void setTvUsername(TextView tvUsername) {
		this.tvUsername = tvUsername;
	}

	private TextView tvUsername;

	public ListView getMenu_function() {
		return menu_function;
	}

	Fragment cur_fragment;
	private SlidingMenu sm;

	public void setSm(SlidingMenu sm) {
		this.sm = sm;
	}

	public RentHouseSideMenuFragment() {

	}

	public RentHouseSideMenuFragment(SlidingMenu slidingMenu) {
		this.sm = slidingMenu;
	}

	private Drawable avatarDrawable;

	public Drawable getAvatarDrawable() {
		return avatarDrawable;
	}

	public void setAvatarDrawable(Drawable avatarDrawable) {
		this.avatarDrawable = avatarDrawable;
	}

	final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap bmp = Utils
						.getRoundedCornerBitmap(((BitmapDrawable) avatarDrawable)
								.getBitmap());
				imgAvatar.setBackground(null);
				imgAvatar.setImageBitmap(bmp);
				break;

			default:
				break;
			}
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = container.getContext();
		accessToken = AccessTokenKeeper.readAccessToken(context);
		initViews(inflater, container);
		if (accessToken.isSessionValid()) {
			String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.format(new java.util.Date(accessToken.getExpiresTime()));
			System.out.println(("access_token 仍在有效期内,无需再次登录: \naccess_token:"
					+ accessToken.getToken() + "\n有效期：" + date));

			final String avatarUrl = MySharedPreferences.get_String(
					"avatarUrl", null);
			String username = MySharedPreferences.get_String("username", "");
			tvUsername.setText(username);
			new Thread(new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (avatarUrl != null) {
						if (Utils.isNetworkConnected(context)) {
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
					handler.sendEmptyMessage(0);
				}
			}).start();

		} else {
			System.out.println(("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
					+ "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证"));
		}
		return layout;
	}

	/**
	 * @param inflater
	 * @param container
	 */
	private void initViews(LayoutInflater inflater, ViewGroup container) {
		layout = (LinearLayout) inflater.inflate(R.layout.mail_menu, null);
		menu_function = (ListView) layout.findViewById(R.id.menu_function);
		menu_function.setAdapter(new FunctionAdapter(context));
		imgAvatar = (ImageView) layout.findViewById(R.id.avatar);
		tvUsername = (TextView) layout.findViewById(R.id.tv_username);
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		menu_function.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changeCurrentView(parent, view, position, null);
			}
		});
		imgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				accessToken = AccessTokenKeeper.readAccessToken(context);
				if (accessToken.isSessionValid()) {
					SinaResp resp = new SinaResp();
					resp.setName(MySharedPreferences.get_String("username", ""));
					resp.setAvatar_large(MySharedPreferences.get_String(
							"avatarUrl", null));
					changeCurrentView(menu_function, null, -1, resp);
					return;
				}
				Intent intent = LoginActivity
						.createLoginIntent(RentHouseSideMenuFragment.this);
				context.startActivity(intent);
			}
		});
	}

	Fragment last_fragment;
	List<Integer> already_opened = new ArrayList<Integer>();

	/**
	 * 根据position改变当先的展示
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 */
	public void changeCurrentView(AdapterView<?> parent, View view,
			int position, SinaResp resp) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			((ImageView) parent.getChildAt(i).findViewById(
					R.id.menu_function_icon)).setColorFilter(getResources()
					.getColor(R.color.icon_dark_blue));
			((TextView) parent.getChildAt(i).findViewById(
					R.id.menu_function_content)).setTextColor(getResources()
					.getColor(R.color.font_dark_blue));
		}
		if (position != -1) {
			ImageView icon = (ImageView) view
					.findViewById(R.id.menu_function_icon);
			TextView content = (TextView) view
					.findViewById(R.id.menu_function_content);
			icon.setColorFilter(Color.WHITE, Mode.SRC_IN);
			content.setTextColor(Color.WHITE);
		}
		changeContentView(position, resp, this);
	}

	/**
	 * @param position
	 */
	private void changeContentView(int position, SinaResp resp,
			RentHouseSideMenuFragment rentMenu) {

		if (position == -1 && MainActivity.mCurrentFlag == -1) {
			
		} else if (MainActivity.mCurrentFlag == position) {
			((MainActivity) getActivity()).toggle();
			return;
		}
		MainActivity.mCurrentFlag = position;
		MainActivity menu = (MainActivity) getActivity();
		switch (position) {
		case -1:
			// TODO 添加用户中心flagment
			cur_fragment = UserCenterFragment.getInstance(sm, resp, rentMenu);
			break;
		case 0:
			cur_fragment = WubaHouseListFragment.getInstance(sm);
			break;
		case 1:
			cur_fragment = GanjiHouseListFragment.getInstance(sm);
			break;
		case 2:
			cur_fragment = VVHouseListFragment.getInstance(sm);
			break;
		}
		FragmentTransaction ft = menu.getSupportFragmentManager()
				.beginTransaction();
		ft.replace(R.id.main_layout, cur_fragment);
		ft.commit();
		menu.getSlidingMenu().showContent();
	}

	static class ViewHolder {
		ImageView menu_function_icon;
		TextView menu_function_content;
	}

	class FunctionAdapter extends BaseAdapter {

		String[] txts = { "五八同城", "赶集生活", "VV短租" };

		int[] icons = { R.drawable.menu_uni, R.drawable.menu_con, R.drawable.menu_con};
		ViewHolder holder;

		public FunctionAdapter(Context context) {

		}

		@Override
		public int getCount() {
			return txts.length;
		}

		@Override
		public Object getItem(int position) {
			return txts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.menu_function, null);
				holder = new ViewHolder();
				holder.menu_function_icon = (ImageView) convertView
						.findViewById(R.id.menu_function_icon);
				holder.menu_function_content = (TextView) convertView
						.findViewById(R.id.menu_function_content);
				if (position == 0) {
					holder.menu_function_icon.setColorFilter(Color.WHITE);
					holder.menu_function_content.setTextColor(Color.WHITE);
				} else {
					holder.menu_function_icon.setColorFilter(getResources()
							.getColor(R.color.icon_dark_blue));
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.menu_function_icon.setImageResource(icons[position]);
			holder.menu_function_icon.setFocusable(false);
			holder.menu_function_content.setText(txts[position]);

			return convertView;
		}
	}
}
