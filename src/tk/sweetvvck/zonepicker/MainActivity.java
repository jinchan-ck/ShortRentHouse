package tk.sweetvvck.zonepicker;

import java.util.ArrayList;
import java.util.List;

import tk.sweetvvck.shortrendhouse.R;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initProvince();
		inflater = getLayoutInflater();
		
		View dialogView = inflater.inflate(R.layout.zone_picker_dialog, null);
		lvZone = (ListView) dialogView.findViewById(R.id.lv_zone);
		lvZone.setAdapter(provinceAdapter);
		provinceDialog = new Dialog(this);
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
				cityList = new ListView(MainActivity.this);
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
						zoneList = new ListView(MainActivity.this);
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
								Toast.makeText(MainActivity.this, province + ":" + city + ":" + zone, 1).show();
							}
						});
						zoneDialog = new Dialog(MainActivity.this);
						zoneDialog.setContentView(zoneList);
						zoneDialog.setTitle("选择地区：");
						zoneDialog.show();
					}

				});
				cityDialog = new Dialog(MainActivity.this);
				cityDialog.setContentView(cityList);
				cityDialog.setTitle("选择城市：");
				cityDialog.show();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
