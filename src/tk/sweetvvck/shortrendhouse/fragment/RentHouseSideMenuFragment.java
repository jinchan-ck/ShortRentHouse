package tk.sweetvvck.shortrendhouse.fragment;

import java.util.ArrayList;
import java.util.List;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.MainActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class RentHouseSideMenuFragment extends Fragment {

	Context context;
	LinearLayout layout = null;
	private ListView menu_function;

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
		initViews(inflater, container);
		return layout;
	}

	/**
	 * @param inflater
	 * @param container
	 */
	private void initViews(LayoutInflater inflater, ViewGroup container) {
		context = container.getContext();
		layout = (LinearLayout) inflater.inflate(R.layout.mail_menu, null);
		menu_function = (ListView) layout.findViewById(R.id.menu_function);
		menu_function.setAdapter(new FunctionAdapter(context));
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

		String[] txts = { "五八同城", "赶集生活"};

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
