package tk.sweetvvck.shortrendhouse.fragment;

import java.util.HashMap;
import java.util.List;

import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.VVHouseDetailActivity;
import tk.sweetvvck.swipeview.BaseSwipeListViewListener;
import tk.sweetvvck.swipeview.SwipeListView;
import tk.sweetvvck.swipeview.adapter.HouseAdapter;
import tk.sweetvvck.swipeview.util.SettingsManager;
import tk.sweetvvck.utils.HttpUtils;
import tk.sweetvvck.utils.MySharedPreferences;
import tk.sweetvvck.utils.Utils;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class VVHouseListFragment extends HouseListBaseFragment {

	private HouseAdapter adapter;
	private List<HouseInfo> data;
	private SwipeListView swipeListView;

	/** 用于标记是否要拦截全局右滑操作 ， 当mPosition == position和初始化时，设为不拦截，其他情况为拦截 */
	@SuppressWarnings("unused")
	private boolean notInterceptFlag = true;
	/** 用于记录当前触摸的列表的Item的位置 */
	private int mPosition;

	boolean isFirstShow = true;
	// 当前最上方滑动的item序号
	public static int cur_item_index = 1;
	private static Fragment instance;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (swipeListView != null && adapter != null) {
					swipeListView.setAdapter(adapter);
					cur_item_index = MySharedPreferences.get_Int(
							"cur_item_index", 1);
					swipeListView.setSelection(cur_item_index);
				}
				Utils.dismissProgressDialog(progressDialog);
				break;
			case 1:
				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_LONG).show();
				Utils.dismissProgressDialog(progressDialog);
				break;
			case 3:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> args = (HashMap<String, Object>) msg.obj;
				ImageView iv = (ImageView) args.get("imageView");
				Bitmap bmp = (Bitmap) args.get("bitmap");
				iv.setImageBitmap(bmp);
				break;
			}
		}
	};

	@Override
	public void initViews(LayoutInflater inflater) {
		swipeListView = (SwipeListView) rootView.findViewById(R.id.list);
		// 搜索条
		MySharedPreferences.init_SP_Instance(getActivity(), "MailListState");
		cur_item_index = MySharedPreferences.get_Int("cur_item_index", 1);
		reload();
	}

	@Override
	public void initListener() {
		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

			@Override
			public void onChoiceChanged(int position, boolean selected) {
				// TODO 标记为已读的逻辑处理，现在暂时不做
				// swipeListView.unselectedChoiceStates();
			}

			/**
			 * 当按下Item时，记录下当前Item的Position，并且将是否拦截全局左右滑动设置为不拦截
			 */
			@Override
			public void onDown(int downPosition) {
				if (mPosition == downPosition) {
					notInterceptFlag = true;
				}
				mPosition = downPosition;
			}

			/**
			 * 当某一个Item被滑开时，将邮件Item的OpenedFlag设为true，并且将全局拦截设为，拦截
			 * 意图在于滑动其他item时拦截Item的向右事件 打开一个Item时，调用
			 * swipeListView.closeOtherItems(); 关掉其他Item
			 */
			@Override
			public void onOpened(int position, boolean toRight) {
				swipeListView.closeOtherItems();
				data.get(position).setOpenedFlag(true);
				notInterceptFlag = false;
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
				if (position >= data.size()) {
					position -= 1;
				}
				if (position > 0)
					data.get(position).setOpenedFlag(false);
				notInterceptFlag = true;
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public boolean onMove(int position, float x) {
				return false;
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				swipeListView.closeOtherItems();
				Log.d("swipe", String.format("onStartOpen %d - action %d",
						position, action));
				// 这里是滑动事件监听 打开后面板2130837652
			}

			@Override
			public void onStartClose(int position, boolean right) {
				Log.d("swipe", String.format("onStartClose %d", position));
				// 这里是滑动事件监听 关闭后面板
			}

			@Override
			public void onClickFrontView(int position) {
				swipeListView.closeOpenedItems();
				Log.d("swipe", String.format("onClickFrontView %d", position));
				// 列表item点击事件
				Intent intent = new Intent(getActivity(),
						VVHouseDetailActivity.class);
				HouseInfo houseInfo = data.get(position);
				intent.putExtra("itemdata", houseInfo);
				startActivity(intent);
			}

			/**
			 * 当点击BackView时将是否拦截全局左右滑动设置为拦截
			 */
			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));
				// 后面板整体点击
				if (mPosition == position) {
					notInterceptFlag = false;
				}
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {
				for (int position : reverseSortedPositions) {
					data.remove(position - 1);
					mPosition -= 1;
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onScroll(int firstVisibleItem) {
				cur_item_index = firstVisibleItem;
			}

		});
	}

	public void initActionBarListener() {
		// ((MenuActivity) getActivity())
		// .setActionBarListener(new MenuActivity.ActionBarListener() {
		// @Override
		// public void onItemClick(int MenuItemID) {
		// switch (MenuItemID) {
		// // 刷新按钮点击事件
		// case R.id.action_refresh:
		// Toast.makeText(getActivity(), "main refresh",
		// Toast.LENGTH_SHORT).show();
		// MyDBHelper mMyDBHelper = MyDBHelper
		// .getInstant(getActivity());
		// // for (int i = 0; i < 1; i++) {
		// mMyDBHelper.insertDatabase();
		// // }
		// cur_item_index = 1;
		// MySharedPreferences.init_SP_Instance(getActivity(),
		// "MailListState");
		// MySharedPreferences.put_Int("cur_item_index",
		// cur_item_index);
		// bandData4View();
		// break;
		// case R.id.action_write:
		// Intent intent = new Intent(getActivity(),
		// EditMailActivity.class);
		// getActivity().startActivity(intent);
		// break;
		// default:
		// break;
		// }
		// }
		// });

		/**
		 * 注册这个监听器用于通知slidingmenu的CustomViewAbove是否拦截当前触摸事件，不向子View（
		 * SwipeListView）传递
		 */
//		_sm.setCustomInterceptTouchEventListener(new CustomInterceptTouchEventListener() {
//
//			@Override
//			public boolean interceptOrNot(CustomViewAbove customViewAbove,
//					MotionEvent ev) {
//				Log.d("swipe", String.format("onTouch %d", mPosition));
//				if (mPosition < 0)
//					return true;
//				if (notInterceptFlag && data != null && !data.isEmpty()
//						&& data.get(mPosition).isOpenedFlag()) {
//					return false;
//				} else {
//					return true;
//				}
//			}
//
//			@Override
//			public void onDrag() {
//				swipeListView.closeOpenedItems();
//			}
//		});
		if(_sm != null)
			_sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		initListener();
	}

	@Override
	public void bandData4View() {
		super.bandData4View();
		new Thread(new Runnable() {
			@Override
			public void run() {
				data = getData();
				if (data != null && !data.isEmpty()) {
					adapter = new HouseAdapter(getActivity(), data,
							swipeListView, handler);
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			}

		}).start();
	}

	private List<HouseInfo> getData() {
		String result = HttpUtils.getData(HttpUtils.GET_HOUSES_URL, null);
		Gson gson = new Gson();
		List<HouseInfo> houses = gson.fromJson(result,
				new TypeToken<List<HouseInfo>>() {
				}.getType());
		if (houses != null && !houses.isEmpty()) {
			return houses;
		}
		return null;
	}

	@Override
	public void onPause() {
		MySharedPreferences.init_SP_Instance(getActivity(), "MailListState");
		MySharedPreferences.put_Int("cur_item_index", cur_item_index);
		super.onPause();
	}

	private void reload() {
		SettingsManager settings = SettingsManager.getInstance();
		swipeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// 设置滑动方向： 只能左滑（left）；只能右滑（right）；两边都可以（both）。
		swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		// 设置左滑动作：滑动展示（REVEAL）；删掉该项（DISMISS）；选择该项，自动回弹功能（CHOICE）；未知（NONE）
		swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
		// 设置右滑动作：滑动展示（REVEAL）；删掉该项（DISMISS）；选择该项，自动回弹功能（CHOICE）；未知（NONE）
		swipeListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		// 左滑动停止预留宽度
		swipeListView.setOffsetLeft(Utils.dip2px(getActivity(),
				settings.getSwipeOffsetLeft()));
		// 右滑动停止预留宽度
		swipeListView.setOffsetRight(convertDpToPixel(settings
				.getSwipeOffsetRight()));
		// 滑动动画时间： 默认 0（系统默认）；数值越大，动画时间越长。
		swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
		// 长按列表项是否展开
		swipeListView.setSwipeOpenOnLongPress(false);
	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	public static Fragment getInstance(SlidingMenu sm) {
		if (instance == null) {
			instance = new VVHouseListFragment();
		}
		_sm = sm;
		return instance;
	}
}
