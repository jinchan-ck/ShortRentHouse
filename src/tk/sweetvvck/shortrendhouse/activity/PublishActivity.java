package tk.sweetvvck.shortrendhouse.activity;

import java.util.ArrayList;
import java.util.List;

import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.zonepicker.DBManager;
import tk.sweetvvck.zonepicker.MyAdapter;
import tk.sweetvvck.zonepicker.MyListItem;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

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
	
	/**上传内容*/
	private TextView spZone;
	private EditText etPlace;
	private EditText etLocation;
	private Spinner spHouseType;
	private EditText etPrice;
	private EditText etTitle;
	private EditText etDiscription;
	private EditText etContact;
	private EditText etPhoneNum;

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

	private void initViews() {
		spZone = (TextView) this.findViewById(R.id.sp_zone);
		etPlace = (EditText) this.findViewById(R.id.et_place);
		etLocation = (EditText) this.findViewById(R.id.et_location);
		spHouseType = (Spinner) this.findViewById(R.id.sp_type);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.house_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spHouseType.setAdapter(adapter);
		etPrice = (EditText) this.findViewById(R.id.et_price);
		etTitle = (EditText) this.findViewById(R.id.et_title);
		etDiscription = (EditText) this.findViewById(R.id.et_discription);
		etContact = (EditText) this.findViewById(R.id.et_contact);
		etPhoneNum = (EditText) this.findViewById(R.id.et_phone_num);
		initListener();
	}

	private void initListener() {
		spZone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initProvince();
				inflater = getLayoutInflater();
				
				View dialogView = inflater.inflate(R.layout.zone_picker_dialog, null);
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
						province = ((MyListItem) parent.getItemAtPosition(position))
								.getName();
						String pcode = ((MyListItem) parent
								.getItemAtPosition(position)).getPcode();
						initCity(pcode);
						cityList = new ListView(context);
						cityList.setAdapter(cityAdapter);
						cityList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								city = ((MyListItem) parent.getItemAtPosition(position))
										.getName();
								String pcode = ((MyListItem) parent
										.getItemAtPosition(position)).getPcode();
								initZone(pcode);
								zoneList = new ListView(context);
								zoneList.setAdapter(zoneAdapter);
								zoneList.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent,
											View view, int position, long id) {
										zone = ((MyListItem) parent.getItemAtPosition(position))
												.getName();
										zoneDialog.cancel();
										cityDialog.cancel();
										provinceDialog.cancel();
										spZone.setText(province + " " + city + " " + zone);
										Toast.makeText(context, province + ":" + city + ":" + zone, Toast.LENGTH_SHORT).show();
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
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.house_detail_actionbar, menu);
		return true;
	}
}
