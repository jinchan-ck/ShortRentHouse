package tk.sweetvvck.zonepicker;

import java.util.List;

import tk.sweetvvck.shortrendhouse.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private List<MyListItem> myList;

	public MyAdapter(Context context, List<MyListItem> myList) {
		this.context = context;
		this.myList = myList;
	}

	public int getCount() {
		return myList.size();
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.zone_picker_item, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//			holder.tvPcode = (TextView) convertView.findViewById(R.id.tv_pcode);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		MyListItem myListItem = myList.get(position);
		holder.tvName.setText(myListItem.getName());
//		holder.tvPcode.setText(myListItem.getPcode());
		return convertView;
	}

	class ViewHolder{
		public TextView tvName;
//		public TextView tvPcode;
	}
	
	class MyAdapterView extends LinearLayout {
		public static final String LOG_TAG = "MyAdapterView";

		public MyAdapterView(Context context, MyListItem myListItem) {
			super(context);
			this.setOrientation(HORIZONTAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					200, LayoutParams.WRAP_CONTENT);
			params.setMargins(1, 1, 1, 1);

			TextView name = new TextView(context);
			name.setText(myListItem.getName());
			addView(name, params);

			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
					200, LayoutParams.WRAP_CONTENT);
			params2.setMargins(1, 1, 1, 1);

			TextView pcode = new TextView(context);
			pcode.setText(myListItem.getPcode());
			addView(pcode, params2);
			pcode.setVisibility(GONE);

		}

	}

}