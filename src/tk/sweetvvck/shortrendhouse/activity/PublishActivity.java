package tk.sweetvvck.shortrendhouse.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.net.req.PublishReq;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.HttpUtils;
import tk.sweetvvck.utils.MySharedPreferences;
import tk.sweetvvck.utils.UploadUtils;
import tk.sweetvvck.zonepicker.DBManager;
import tk.sweetvvck.zonepicker.MyAdapter;
import tk.sweetvvck.zonepicker.MyListItem;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.weibo.sdk.android.Oauth2AccessToken;

public class PublishActivity extends SlidingFragmentActivity {

	private Context context;
	private Dialog progressDialog;
	private DBManager dbm;
	private SQLiteDatabase db;
	private MyAdapter provinceAdapter;
	private MyAdapter cityAdapter;
	private MyAdapter zoneAdapter;
	private LayoutInflater inflater;
	private ListView lvZone;
	private String province;
	private String city;
	private String zone;
	private Dialog provinceDialog;
	private Dialog cityDialog;
	private Dialog zoneDialog;
	private ListView cityList;
	private ListView zoneList;

	/** 上传内容 */
	private LinearLayout llUploadImage;
	private TextView spZone;
	private EditText etPlace;
	private EditText etLocation;
	private Spinner spHouseType;
	private EditText etPrice;
	private EditText etTitle;
	private EditText etDiscription;
	private EditText etContact;
	private EditText etPhoneNum;
	private RadioGroup rgIdentity;

	/** 选择图片相关 */
	/**
	 * 选择文件
	 */
	public static final int TO_SELECT_PHOTO = 3;
	private HashMap<String, String> images = new HashMap<String, String>();
	private String picPath = null;
	private static final String TAG = "uploadImage";

	/** 发布到服务器的返回值 */
	private String result;

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
				Toast.makeText(context, result, Toast.LENGTH_LONG)
				.show();
				System.out.println("result--->" + result);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(result != null && result.contains("成功")){
					PublishActivity.this.finish();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_publish);
		setBehindContentView(R.layout.new_page_behind);
		initViews();
		actionbarInit();
		slidingMenuInit();
		progressDialogInit();
	}

	public void selectImage(View v) {
		Intent intent = new Intent(this, SelectPicActivity.class);
		startActivityForResult(intent, TO_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i(TAG, "最终选择的图片=" + picPath);
			Bitmap bm = UploadUtils.getSmallBitmap(picPath);
			// Bitmap bm = BitmapFactory.decodeFile(picPath);
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(bm);
			imageView.setLayoutParams(new LayoutParams(250, 200));
			llUploadImage.addView(imageView);
			String imgName = picPath.substring(picPath.lastIndexOf("/"));
			images.put(imgName, UploadUtils.bitmaptoString(bm));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initViews() {
		llUploadImage = (LinearLayout) this.findViewById(R.id.ll_upload_image);
		spZone = (TextView) this.findViewById(R.id.sp_zone);
		etPlace = (EditText) this.findViewById(R.id.et_place);
		etLocation = (EditText) this.findViewById(R.id.et_location);
		spHouseType = (Spinner) this.findViewById(R.id.sp_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.house_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spHouseType.setAdapter(adapter);
		etPrice = (EditText) this.findViewById(R.id.et_price);
		etTitle = (EditText) this.findViewById(R.id.et_title);
		etDiscription = (EditText) this.findViewById(R.id.et_discription);
		etContact = (EditText) this.findViewById(R.id.et_contact);
		etPhoneNum = (EditText) this.findViewById(R.id.et_phone_num);
		rgIdentity = (RadioGroup) this.findViewById(R.id.rg_identity);
		((RadioButton) rgIdentity.getChildAt(0)).setChecked(true);
		initListener();
	}

	private void initListener() {
		spZone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initProvince();
				inflater = getLayoutInflater();

				View dialogView = inflater.inflate(R.layout.zone_picker_dialog,
						null);
				lvZone = (ListView) dialogView.findViewById(R.id.lv_zone);
				lvZone.setAdapter(provinceAdapter);
				provinceDialog = new Dialog(context);
				provinceDialog.setContentView(dialogView);
				provinceDialog.setTitle("选择省份：");
				provinceDialog.show();
				lvZone.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						province = ((MyListItem) parent
								.getItemAtPosition(position)).getName();
						String pcode = ((MyListItem) parent
								.getItemAtPosition(position)).getPcode();
						initCity(pcode);
						cityList = new ListView(context);
						cityList.setAdapter(cityAdapter);
						cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								city = ((MyListItem) parent
										.getItemAtPosition(position)).getName();
								String pcode = ((MyListItem) parent
										.getItemAtPosition(position))
										.getPcode();
								initZone(pcode);
								zoneList = new ListView(context);
								zoneList.setAdapter(zoneAdapter);
								zoneList.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										zone = ((MyListItem) parent
												.getItemAtPosition(position))
												.getName();
										zoneDialog.cancel();
										cityDialog.cancel();
										provinceDialog.cancel();
										spZone.setText(province + " " + city
												+ " " + zone);
										Toast.makeText(
												context,
												province + ":" + city + ":"
														+ zone,
												Toast.LENGTH_SHORT).show();
									}
								});
								zoneDialog = new Dialog(context);
								zoneDialog.setContentView(zoneList);
								zoneDialog.setTitle("选择地区：");
								zoneDialog.show();
							}

						});
						cityDialog = new Dialog(context);
						cityDialog.setContentView(cityList);
						cityDialog.setTitle("选择城市：");
						cityDialog.show();
					}
				});
			}
		});
	}

	/**
	 * 初始化进度框
	 */
	private void progressDialogInit() {
		progressDialog = new Dialog(this, R.style.myDialogTheme);
		View progressView = getLayoutInflater().inflate(R.layout.progressbar,
				null);
		progressbar = (LoadingCircleView) progressView
				.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
	}

	/**
	 * 初始化Actionbar
	 */
	private void actionbarInit() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_background));
		actionBar
				.setIcon(getResources().getDrawable(R.drawable.actionbar_back));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
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
				PublishActivity.this.finish();
			}
		});
	}

	private void initZone(String pcode) {
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {
			String sql = "select * from district where pcode='" + pcode + "'";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(2);
				String name = new String(bytes, "gbk");
				MyListItem myListItem = new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(2);
			String name = new String(bytes, "gbk");
			MyListItem myListItem = new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
		}
		dbm.closeDatabase();
		db.close();

		zoneAdapter = new MyAdapter(this, list);
	}

	private void initCity(String pcode) {
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {
			String sql = "select * from city where pcode='" + pcode + "'";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(2);
				String name = new String(bytes, "gbk");
				MyListItem myListItem = new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(2);
			String name = new String(bytes, "gbk");
			MyListItem myListItem = new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
		}
		dbm.closeDatabase();
		db.close();
		cityAdapter = new MyAdapter(this, list);
	}

	public void initProvince() {
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();
		try {
			String sql = "select * from province";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(2);
				String name = new String(bytes, "gbk");
				MyListItem myListItem = new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(2);
			String name = new String(bytes, "gbk");
			MyListItem myListItem = new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
		}
		dbm.closeDatabase();
		db.close();
		provinceAdapter = new MyAdapter(this, list);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			toggle();
			break;
		case R.id.publish:
			publishData();
			break;
		}
		return true;
	}

	/**
	 * 获取填写的数据并上传到服务器
	 */
	private void publishData() {
		PublishReq req = getEditData();
		if(req == null){
			return;
		}
		Gson json = new Gson();
		String jso = json.toJson(req);
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("bean", jso));
		if (progressDialog != null) {
			progressDialog.show();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				result = HttpUtils.getData(HttpUtils.PUBLISH_URL,
						nameValuePairs);
				handler.sendEmptyMessage(0);
			}
		}).start();
		Toast.makeText(context, "Publish~~", Toast.LENGTH_LONG).show();
	}

	private PublishReq getEditData() {
		HouseInfo content = new HouseInfo();
		String zone = spZone.getText().toString().trim();
		String place = etPlace.getText().toString().trim();
		String location = etLocation.getText().toString().trim();
		String houseType = (String) spHouseType.getSelectedItem();
		String price = etPrice.getText().toString().trim();
		String title = etTitle.getText().toString().trim();
		String discription = etDiscription.getText().toString().trim();
		String contact = etContact.getText().toString().trim();
		String phoneNum = etPhoneNum.getText().toString().trim();
		String identity = ((RadioButton) rgIdentity.getChildAt(rgIdentity
				.getCheckedRadioButtonId() - 1)).getText().toString();
		if (contact != null && !contact.equals("")) {
			content.setContact(contact);
		} else {
			Toast.makeText(context, "联系人不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (discription != null && !discription.equals("")) {
			content.setDiscription(discription);
		} else {
			Toast.makeText(context, "房屋描述不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (houseType != null && !houseType.equals("")) {
			content.setHouseType(houseType);
		} else {
			Toast.makeText(context, "房屋类型不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		content.setIdentity(identity);
		if (!images.isEmpty()) {
			content.setImages(images);
		} else {
			Toast.makeText(context, "至少上传一张图片", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (location != null && !location.equals("")) {
			content.setLocation(location);
		} else {
			Toast.makeText(context, "地段不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (place != null && !place.equals("")) {
			content.setPlace(place);
		} else {
			Toast.makeText(context, "位置不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (price != null && !price.equals("")) {
			content.setPrice(price);
		} else {
			Toast.makeText(context, "价格不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (title != null && !title.equals("")) {
			content.setTitle(title);
		} else {
			Toast.makeText(context, "标题不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (zone != null && !zone.equals("")) {
			content.setZone(zone);
		} else {
			Toast.makeText(context, "区域不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		if (phoneNum != null && !phoneNum.equals("")) {
			content.setPhoneNum(phoneNum);
		} else {
			Toast.makeText(context, "联系电话不能为空", Toast.LENGTH_SHORT).show();
			return null;
		}
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
		String uid = null;
		if (token.isSessionValid()) {
			uid = MySharedPreferences.get_String("uid", null);
		} else {
			uid = null;
			Toast.makeText(context, "您还未登录，或者登陆过期，请重新登陆", Toast.LENGTH_SHORT).show();
			return null;
		}
		PublishReq req = new PublishReq();
		req.setUid(uid);
		req.setPublishContent(content);
		return req;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.publish, menu);
		return true;
	}
}
