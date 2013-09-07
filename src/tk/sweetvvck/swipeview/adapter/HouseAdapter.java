package tk.sweetvvck.swipeview.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.entity.Photo;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.swipeview.SwipeListView;
import tk.sweetvvck.utils.HttpUtils;
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

public class HouseAdapter extends BaseAdapter {

	private List<HouseInfo> data;
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
		return data.size();
	}

	@Override
	public HouseInfo getItem(int position) {
		return data.get(position);
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
			holder.bAction3 = (ImageButton) convertView
					.findViewById(R.id.example_row_b_action_3);
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
				star_state = !star_state;
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

		holder.bAction3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "button 3 onclick", Toast.LENGTH_SHORT)
						.show();
			}
		});
		holder.bAction4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeListView.dismiss(position + 1);
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
		ImageButton bAction3;
		ImageButton bAction4;
	}
}
