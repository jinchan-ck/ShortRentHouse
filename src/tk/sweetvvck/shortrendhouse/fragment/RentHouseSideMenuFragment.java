package tk.sweetvvck.shortrendhouse.fragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tk.sweetvvck.constants.SocialConfig;
import tk.sweetvvck.entity.SinaResp;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.MainActivity;
import tk.sweetvvck.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.baidu.social.core.BaiduSocialException;
import com.baidu.social.core.BaiduSocialListener;
import com.baidu.social.core.Utility;
import com.baidu.sociallogin.BaiduSocialLogin;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class RentHouseSideMenuFragment extends Fragment {

	private Context context;
	private LinearLayout layout = null;
	private ListView menu_function;

	private ImageView imgAvatar;
	private TextView tvUsername;
	
	private Drawable avatarDrawable;

	final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				imgAvatar.setImageBitmap(Utils.getRoundRectBitmap(avatarDrawable, 5));
				break;

			default:
				break;
			}
		}
	};

	private BaiduSocialLogin socialLogin;

	private final static String appKey = SocialConfig.mbApiKey;

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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = container.getContext();
		socialLogin = BaiduSocialLogin.getInstance(context, appKey);
		socialLogin.supportWeiBoSso(SocialConfig.SINA_SSO_APP_KEY);
		initViews(inflater, container);
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
		imgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (socialLogin
						.isAccessTokenValid(Utility.SHARE_TYPE_SINA_WEIBO)) {
					socialLogin.getUserInfoWithShareType(context,
							Utility.SHARE_TYPE_SINA_WEIBO,
							new UserInfoListener());
				} else
					socialLogin.authorize((Activity) context,
							Utility.SHARE_TYPE_SINA_WEIBO,
							new UserInfoListener());

			}
		});
		initListener();
	}

	class UserInfoListener implements BaiduSocialListener {

		@Override
		public void onAuthComplete(Bundle values) {
		}

		@Override
		public void onApiComplete(String responses) {
			final String responseStr = responses;
			handler.post(new Runnable() {
				@Override
				public void run() {
					String info = Utility.decodeUnicode(responseStr);
					Gson json = new Gson();
					final SinaResp resp = json.fromJson(info, SinaResp.class);
					tvUsername.setText(resp.getUsername());
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							avatarDrawable = loadImageFromNetwork(resp.getHeadurl());
							handler.sendEmptyMessage(0);
						}
					}).start();
				}
			});
		}

		@Override
		public void onError(BaiduSocialException e) {
//			final String error = e.toString();
			handler.post(new Runnable() {
				@Override
				public void run() {
				}
			});
		}
	}

	private Drawable loadImageFromNetwork(String imageUrl) {
		
		imageUrl = imageUrl.replace("/50/", "/180/");
		
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}

		return drawable;
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		menu_function.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changeCurrentView(parent, view, position);
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
	public void changeCurrentView(AdapterView<?> parent, View view, int position) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			((ImageView) parent.getChildAt(i).findViewById(
					R.id.menu_function_icon)).setColorFilter(getResources()
					.getColor(R.color.icon_dark_blue));
			((TextView) parent.getChildAt(i).findViewById(
					R.id.menu_function_content)).setTextColor(getResources()
					.getColor(R.color.font_dark_blue));
		}
		ImageView icon = (ImageView) view.findViewById(R.id.menu_function_icon);
		TextView content = (TextView) view
				.findViewById(R.id.menu_function_content);
		icon.setColorFilter(Color.WHITE, Mode.SRC_IN);
		content.setTextColor(Color.WHITE);
		if (MainActivity.mCurrentFlag == position) {
			((MainActivity) getActivity()).toggle();
			return;
		}
		MainActivity.mCurrentFlag = position;
		MainActivity menu = (MainActivity) getActivity();
		switch (position) {
		case 0:
			cur_fragment = WubaHouseListFragment.getInstance(sm);
			break;
		case 1:
			cur_fragment = GanjiHouseListFragment.getInstance(sm);

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

		String[] txts = { "五八同城", "赶集生活" };

		int[] icons = { R.drawable.menu_uni, R.drawable.menu_con };
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
