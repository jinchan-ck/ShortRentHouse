package tk.sweetvvck.shortrendhouse.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.swipeview.SwipeListView;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.HttpUtils;
import tk.sweetvvck.utils.MySharedPreferences;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.Oauth2AccessToken;

public class UserCenterAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private OnTabSelectListener onTabSelectListener;
	private Context context;
	private Handler handler;

	public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
		this.onTabSelectListener = onTabSelectListener;
	}

	public UserCenterAdapter(Context mContext, Handler handler) {
		context = mContext;
		inflater = LayoutInflater.from(mContext);
		this.handler = handler;
	}

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

	boolean firstFlag = true;

	ViewHolder holder = null;
	View mSearchBar = null;
	EditText mSearchBar_EditText;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.user_center_item, null);
			holder = new ViewHolder();

			holder.tab1 = (ImageButton) convertView
					.findViewById(R.id.tab_button_1);
			holder.tab2 = (ImageButton) convertView
					.findViewById(R.id.tab_button_2);
			// 蓝色条
			holder.tab_button_bg_1 = (ImageView) convertView
					.findViewById(R.id.tab_button_bg_1);
			holder.tab_button_bg_2 = (ImageView) convertView
					.findViewById(R.id.tab_button_bg_2);

			holder.content1 = (LinearLayout) convertView
					.findViewById(R.id.ll_my_publish_houses);
			holder.content2 = (LinearLayout) convertView
					.findViewById(R.id.ll_my_favorite_houses);

			holder.lvPublishHouseList = (SwipeListView) convertView
					.findViewById(R.id.lv_my_publish_houses);
			holder.lvFavoriteHouseList = (SwipeListView) convertView
					.findViewById(R.id.lv_my_favorite_houses);

			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		// mSearchBar = inflater.inflate(R.layout.search_bar, null);
		// holder.lvFavoriteHouseList.addHeaderView(mSearchBar);
		// holder.lvPublishHouseList.addHeaderView(mSearchBar);
		// mSearchBar_EditText = (EditText) mSearchBar
		// .findViewById(R.id.list_search);

		if (firstFlag) {
			firstFlag = false;
			new Thread(new Runnable() {

				@Override
				public void run() {
					List<HouseInfo> publishData = getMyPulishHouses();
					List<HouseInfo> favoriteData = getMyFavoriteHouses();

					Message msg = new Message();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("publishData", publishData);
					map.put("favoriteData", favoriteData);
					map.put("publishListView", holder.lvPublishHouseList);
					map.put("favoriteListView", holder.lvFavoriteHouseList);
					msg.what = 1;
					msg.obj = map;
					handler.sendMessage(msg);
				}
			}).start();
		}

		// initListener(holder.lvFavoriteHouseList);
		// initListener(holder.lvPublishHouseList);
		holder.tab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.content1.setVisibility(View.VISIBLE);
				holder.content2.setVisibility(View.GONE);
				holder.tab_button_bg_1
						.setBackgroundResource(R.drawable.tab_bg_select);
				holder.tab_button_bg_2
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				onTabSelectListener.onSelected(1);
			}
		});
		holder.tab2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				holder.content1.setVisibility(View.GONE);
				holder.content2.setVisibility(View.VISIBLE);
				holder.tab_button_bg_1
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				holder.tab_button_bg_2
						.setBackgroundResource(R.drawable.tab_bg_select);
				onTabSelectListener.onSelected(2);
			}
		});
		notifyDataSetChanged();
		onTabSelectListener.getHolder(holder);
		return convertView;
	}

	protected List<HouseInfo> getMyFavoriteHouses() {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		if (!token.isSessionValid()) {
			return null;
		}
		String uid = MySharedPreferences.get_String("uid", UUID.randomUUID()
				+ "");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair = new BasicNameValuePair("uid", uid);
		nameValuePairs.add(nameValuePair);
		String result = HttpUtils.getData(HttpUtils.GET_FAVORITE_URL,
				nameValuePairs);
		Gson gson = new Gson();
		List<HouseInfo> houses = gson.fromJson(result,
				new TypeToken<List<HouseInfo>>() {
				}.getType());
		if (houses != null && !houses.isEmpty()) {
			return houses;
		}
		return null;
	}

	private List<HouseInfo> getMyPulishHouses() {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		if (!token.isSessionValid()) {
			return null;
		}
		String uid = MySharedPreferences.get_String("uid", UUID.randomUUID()
				+ "");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair nameValuePair = new BasicNameValuePair("uid", uid);
		nameValuePairs.add(nameValuePair);
		String result = HttpUtils.getData(HttpUtils.GET_MYPUBLISHHOUSES_URL,
				nameValuePairs);
		Gson gson = new Gson();
		List<HouseInfo> houses = gson.fromJson(result,
				new TypeToken<List<HouseInfo>>() {
				}.getType());
		if (houses != null && !houses.isEmpty()) {
			return houses;
		}
		return null;
	}

	public class ViewHolder {

		public ImageButton tab1;
		public ImageButton tab2;

		public LinearLayout content1;
		public LinearLayout content2;

		public ImageView tab_button_bg_1;
		public ImageView tab_button_bg_2;

		// content1
		public SwipeListView lvPublishHouseList;
		public SwipeListView lvFavoriteHouseList;
	}

	public interface OnTabSelectListener {
		void onSelected(int position);

		void getHolder(ViewHolder convertView);
	}
}