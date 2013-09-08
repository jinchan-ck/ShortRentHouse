package tk.sweetvvck.swipeview.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.entity.Photo;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.swipeview.SwipeListView;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.HttpUtils;
import tk.sweetvvck.utils.MySharedPreferences;
import tk.sweetvvck.utils.Utils;
import tk.sweetvvck.utils.WebTool;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;

public class HouseAdapter extends BaseAdapter {

	public static final int HANDLE_FAVORITE = 5;

	private List<HouseInfo> data;

	public List<HouseInfo> getData() {
		return data;
	}

	public void setData(List<HouseInfo> data) {
		this.data = data;
	}

	private Context context;
	Dialog dialog;
	SwipeListView swipeListView;

	private Handler handler;

	public HouseAdapter(Context context, List<HouseInfo> data,
			SwipeListView swipeListView, Handler handler) {
		this.context = context;
		this.data = data;
		this.swipeListView = swipeListView;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		if (data != null)
			return data.size();
		return 0;
	}

	@Override
	public HouseInfo getItem(int position) {
		if (data != null)
			return data.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	View people_info_dialog_view;
	ImageView people_icon;
	TextView people_name;
	TextView people_email;
	Button btn_mailto;
	Button btn_profile;

	MailListItem mListItem;

	boolean star_state = false;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HouseInfo item = getItem(position);
		ViewHolder holder;
		final LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = li.inflate(R.layout.mail_list_item, parent, false);
			holder = new ViewHolder();

			holder.ivImage = (ImageView) convertView
					.findViewById(R.id.example_row_iv_image);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.example_row_tv_name);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.example_row_tv_title);
			holder.tvMessage = (TextView) convertView
					.findViewById(R.id.example_row_tv_message);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.example_row_tv_time);

			Utils.setFontType(context, holder.tvName, "Roboto_Bold.ttf");
			Utils.setFontType(context, holder.tvTitle, null);
			Utils.setFontType(context, holder.tvMessage, null);
			Utils.setFontType(context, holder.tvTime, null);

			holder.bAction1 = (ImageButton) convertView
					.findViewById(R.id.example_row_b_action_1);
			holder.bAction2 = (ImageButton) convertView
					.findViewById(R.id.example_row_b_action_2);
			holder.bAction4 = (ImageButton) convertView
					.findViewById(R.id.example_row_b_action_4);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		((SwipeListView) parent).recycle(convertView, position);

		Set<Photo> photos = item.getPhotos();
		List<Photo> photoList = new ArrayList<Photo>();
		photoList.addAll(photos);
		String path = photoList.get(0).getPath();
		final String avatarUrl = HttpUtils.BASE_URL + path;
		final ViewHolder tempHolder = holder;
		// TODO 修改图片类型为圆角
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bmp = Utils.getRoundRectBitmap(
						WebTool.loadImageFromNetwork(avatarUrl), 5);
				Message msg = new Message();
				msg.what = 3;
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("bitmap", bmp);
				args.put("imageView", tempHolder.ivImage);
				msg.obj = args;
				handler.sendMessage(msg);
			}
		}).start();
		holder.tvName.setText(item.getTitle());
		holder.tvTitle.setText(item.getPrice());
		holder.tvMessage.setText(item.getDiscription());
		holder.tvTime.setText(item.getHouseType());

		holder.ivImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		holder.bAction1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageButton btn1 = (ImageButton) v;
				int btn1_id = !star_state ? R.drawable.list_item_star_light
						: R.drawable.list_item_star;
				btn1.setImageDrawable(Utils.ResIDToDrawable(context, btn1_id));
				new Thread(new Runnable() {
					@Override
					public void run() {
						String uid = null;
						Oauth2AccessToken token = AccessTokenKeeper
								.readAccessToken(context);
						if (token.isSessionValid()) {
							uid = MySharedPreferences.get_String("uid",
									UUID.randomUUID() + "");
						} else {

						}
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						NameValuePair nameValuePair1 = new BasicNameValuePair(
								"houseId", data.get(position).getId() + "");
						NameValuePair nameValuePair2 = new BasicNameValuePair(
								"uid", uid);
						nameValuePairs.add(nameValuePair1);
						nameValuePairs.add(nameValuePair2);
						String optUrl = !star_state ? HttpUtils.FAVORITE_URL
								: HttpUtils.REMOVE_FAVORITE_URL;
						star_state = !star_state;
						String result = HttpUtils.getData(optUrl,
								nameValuePairs);
						Message msg = new Message();
						msg.what = HANDLE_FAVORITE;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});

		holder.bAction2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				DatePickerDialog dp = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								StringBuffer sb = new StringBuffer();
								sb.append(String.format("%d-%02d-%02d", year,
										monthOfYear + 1, dayOfMonth));
								Toast.makeText(context, sb, Toast.LENGTH_SHORT)
										.show();
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH));
				dp.show();
			}
		});

		holder.bAction4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeListView.dismiss(position);
				new Thread(new Runnable() {

					@Override
					public void run() {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						NameValuePair nameValuePair = new BasicNameValuePair(
								"houseId", getItem(position).getId() + "");
						nameValuePairs.add(nameValuePair);
						String result = HttpUtils.getData(
								HttpUtils.DELETE_HOUSE_URL, nameValuePairs);
						Message msg = new Message();
						msg.what = 4;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView ivImage;
		TextView tvTitle;
		TextView tvName;
		TextView tvMessage;
		TextView tvTime;

		ImageButton bAction1;
		ImageButton bAction2;
		ImageButton bAction4;
	}
}
