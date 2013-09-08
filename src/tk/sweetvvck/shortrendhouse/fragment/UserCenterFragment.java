package tk.sweetvvck.shortrendhouse.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import tk.sweetvvck.customview.ListViewPro;
import tk.sweetvvck.customview.LoadingCircleView;
import tk.sweetvvck.entity.HouseInfo;
import tk.sweetvvck.entity.SinaResp;
import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.activity.LoginActivity;
import tk.sweetvvck.shortrendhouse.activity.MenuActivity;
import tk.sweetvvck.shortrendhouse.activity.MenuActivity.ActionBarListener;
import tk.sweetvvck.shortrendhouse.activity.VVHouseDetailActivity;
import tk.sweetvvck.shortrendhouse.adapter.UserCenterAdapter;
import tk.sweetvvck.shortrendhouse.adapter.UserCenterAdapter.OnTabSelectListener;
import tk.sweetvvck.shortrendhouse.adapter.UserCenterAdapter.ViewHolder;
import tk.sweetvvck.swipeview.BaseSwipeListViewListener;
import tk.sweetvvck.swipeview.SwipeListView;
import tk.sweetvvck.swipeview.adapter.HouseAdapter;
import tk.sweetvvck.swipeview.util.SettingsManager;
import tk.sweetvvck.utils.AccessTokenKeeper;
import tk.sweetvvck.utils.FileOptService;
import tk.sweetvvck.utils.Utils;
import tk.sweetvvck.utils.WebTool;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.weibo.sdk.android.Oauth2AccessToken;

public class UserCenterFragment extends Fragment {
	public static Activity activity;

	public static Fragment instance;

	private ImageView imgAvatar;
	private TextView tvUsername;

	private static SlidingMenu _sm;

	private Dialog progressDialog;

	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public LoadingCircleView getProgressbar() {
		return progressbar;
	}

	private LoadingCircleView progressbar;

	private View rootView;

	private ListViewPro lvBackground;
	public static View itemHead1;

	// Tab相关
	private LinearLayout tabLayout;
	ImageButton stickytab1;
	ImageButton stickytab2;

	ViewHolder holder;
	ImageView tab_button_bg_1;
	ImageView tab_button_bg_2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		initViews(inflater, container);
		initTabViews();
		setUserInfo();
		imgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Oauth2AccessToken accessToken = AccessTokenKeeper
						.readAccessToken(activity);
				if (!accessToken.isSessionValid()) {
					Intent intent = LoginActivity.createLoginIntent(_rentMenu);
					activity.startActivity(intent);
				}
			}
		});
		initActionBarListener();
		return rootView;
	}

	private void initListener(final SwipeListView swipeListView) {
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
			}

			/**
			 * 当某一个Item被滑开时，将邮件Item的OpenedFlag设为true，并且将全局拦截设为，拦截
			 * 意图在于滑动其他item时拦截Item的向右事件 打开一个Item时，调用
			 * swipeListView.closeOtherItems(); 关掉其他Item
			 */
			@Override
			public void onOpened(int position, boolean toRight) {
				swipeListView.closeOtherItems();
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
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
				Intent intent = new Intent(getActivity(),
						VVHouseDetailActivity.class);
				HouseInfo houseInfo = null;
				if (swipeListView != null && swipeListView.getAdapter() != null
						&& swipeListView.getAdapter().getItem(position) != null) {
					houseInfo = (HouseInfo) swipeListView
							.getAdapter().getItem(position);
				}
				intent.putExtra("houseInfo", houseInfo);
				startActivity(intent);
			}

			/**
			 * 当点击BackView时将是否拦截全局左右滑动设置为拦截
			 */
			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {
				for (int i = 0; i < reverseSortedPositions.length; i++) {
					((HouseAdapter) swipeListView.getAdapter()).getData()
							.remove(i);
					((HouseAdapter) swipeListView.getAdapter())
							.notifyDataSetChanged();
				}
			}

			@Override
			public void onScroll(int firstVisibleItem) {
			}

		});
	}

	private void initTabViews() {
		tabLayout = (LinearLayout) rootView.findViewById(R.id.header_tab);

		stickytab1 = (ImageButton) rootView.findViewById(R.id.tab_button_1);
		stickytab2 = (ImageButton) rootView.findViewById(R.id.tab_button_2);
		// 蓝色条
		tab_button_bg_1 = (ImageView) rootView
				.findViewById(R.id.tab_button_bg_1);
		tab_button_bg_2 = (ImageView) rootView
				.findViewById(R.id.tab_button_bg_2);
		final UserCenterAdapter adapter = new UserCenterAdapter(activity,
				handler);
		adapter.setOnTabSelectListener(new OnTabSelectListener() {

			@Override
			public void onSelected(int position) {
				switch (position) {
				case 1:
					tab_button_bg_1
							.setBackgroundResource(R.drawable.tab_bg_select);
					tab_button_bg_2
							.setBackgroundResource(R.drawable.tab_bg_unselect);
					break;
				case 2:
					tab_button_bg_1
							.setBackgroundResource(R.drawable.tab_bg_unselect);
					tab_button_bg_2
							.setBackgroundResource(R.drawable.tab_bg_select);
					break;
				case 3:
					tab_button_bg_1
							.setBackgroundResource(R.drawable.tab_bg_unselect);
					tab_button_bg_2
							.setBackgroundResource(R.drawable.tab_bg_unselect);
					break;
				}
			}

			@Override
			public void getHolder(ViewHolder holder) {
				if (holder != null) {
					UserCenterFragment.this.holder = holder;
				}
			}
		});
		stickytab1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tab_button_bg_1.setBackgroundResource(R.drawable.tab_bg_select);
				tab_button_bg_2
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				holder.tab_button_bg_1
						.setBackgroundResource(R.drawable.tab_bg_select);
				holder.tab_button_bg_2
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				holder.content1.setVisibility(View.VISIBLE);
				holder.content2.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
		});
		stickytab2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tab_button_bg_1
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				tab_button_bg_2.setBackgroundResource(R.drawable.tab_bg_select);
				holder.tab_button_bg_1
						.setBackgroundResource(R.drawable.tab_bg_unselect);
				holder.tab_button_bg_2
						.setBackgroundResource(R.drawable.tab_bg_select);
				holder.content1.setVisibility(View.GONE);
				holder.content2.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}
		});
		lvBackground.setAdapter(adapter);
		lvBackground.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 0) {
					tabLayout.setVisibility(View.VISIBLE);
				} else {
					tabLayout.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/**
	 * @param inflater
	 * @param container
	 */
	private void initViews(LayoutInflater inflater, ViewGroup container) {
		if (_sm != null) {
			_sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		}
		rootView = inflater.inflate(R.layout.user_center, container, false);
		lvBackground = (ListViewPro) rootView.findViewById(R.id.lv_background);
		itemHead1 = inflater.inflate(R.layout.user_center_header, null);
		lvBackground.addHeaderView(itemHead1);
		imgAvatar = (ImageView) itemHead1.findViewById(R.id.avatar);
		tvUsername = (TextView) itemHead1.findViewById(R.id.tv_username);
		progressDialog = new Dialog(activity, R.style.myDialogTheme);
		View progressView = inflater.inflate(R.layout.progressbar, null);
		progressbar = (LoadingCircleView) progressView
				.findViewById(R.id.progress_bar);
		progressDialog.setContentView(progressView);
	}

	class BackgroundAdapter extends BaseAdapter {

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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img = new ImageView(activity);
			img.setBackground(getResources()
					.getDrawable(R.drawable.ic_launcher));
			return img;
		}
	}

	private Drawable avatarDrawable;

	final Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap bmp = Utils
						.getRoundedCornerBitmap(((BitmapDrawable) avatarDrawable)
								.getBitmap());
				tvUsername.setText(_resp.getName());
				imgAvatar.setBackground(null);
				imgAvatar.setImageBitmap(bmp);
				_rentMenu.getImgAvatar().setImageBitmap(bmp);
				_rentMenu.getTvUsername().setText(_resp.getName());
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;

			case 1:
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				List<HouseInfo> publishData = (List<HouseInfo>) map
						.get("publishData");
				List<HouseInfo> favoriteData = (List<HouseInfo>) map
						.get("favoriteData");
				SwipeListView publishListView = (SwipeListView) map
						.get("publishListView");
				SwipeListView favoriteListView = (SwipeListView) map
						.get("favoriteListView");

				HouseAdapter publishAdapter = new HouseAdapter(activity,
						publishData, holder.lvPublishHouseList, handler);
				HouseAdapter favoriteAdapter = new HouseAdapter(activity,
						favoriteData, holder.lvFavoriteHouseList, handler);
				publishListView.setAdapter(publishAdapter);
				Utils.setListViewHeightBasedOnChildren(publishListView);
				favoriteListView.setAdapter(favoriteAdapter);
				Utils.setListViewHeightBasedOnChildren(favoriteListView);
				reload(favoriteListView);
				reload(publishListView);
				initListener(publishListView);
				initListener(favoriteListView);
				break;
			case 3:
				HashMap<String, Object> args = (HashMap<String, Object>) msg.obj;
				ImageView iv = (ImageView) args.get("imageView");
				Bitmap bm = (Bitmap) args.get("bitmap");
				iv.setImageBitmap(bm);
				break;
			case 4:
				String result = (String) msg.obj;
				if (result != null) {
					Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG).show();
				}
				break;
			case HouseAdapter.HANDLE_FAVORITE:
				String message = (String) msg.obj;
				if (message != null) {
					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity, "收藏失败", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

	private void reload(SwipeListView swipeListView) {
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

	public void setUserInfo() {
		if (progressDialog != null)
			progressDialog.show();
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (_resp != null) {
					String avatarUrl = _resp.getAvatar_large();

					avatarDrawable = _rentMenu.getAvatarDrawable();

					if (avatarDrawable == null
							|| avatarDrawable == activity.getResources()
									.getDrawable(R.drawable.people_icon0)) {
						if (avatarUrl != null) {
							if (Utils.isNetworkConnected(activity)) {
								avatarDrawable = WebTool
										.loadImageFromNetwork(avatarUrl);
								if (avatarDrawable != null) {
									try {
										FileOptService.saveImage("avatar.jpg",
												avatarDrawable);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									avatarDrawable = new BitmapDrawable(
											FileOptService
													.readImage("avatar.jpg"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} else {
							avatarDrawable = getResources().getDrawable(
									R.drawable.people_icon0);
						}
						if (avatarDrawable == null) {
							avatarDrawable = getResources().getDrawable(
									R.drawable.people_icon0);
						}
					}
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	private void initActionBarListener() {
		((MenuActivity) getActivity())
				.setActionBarListener(new ActionBarListener() {

					@Override
					public void onItemClick(int MenuItemID) {
						switch (MenuItemID) {
						case R.id.actionbar_function:
							AccessTokenKeeper.clear(activity);
							_sm.toggle();
							Bitmap bmp = Utils
									.getRoundedCornerBitmap(((BitmapDrawable) getResources()
											.getDrawable(
													R.drawable.people_icon0))
											.getBitmap());
							imgAvatar.setImageBitmap(bmp);
							tvUsername.setText("点击登录");
							_rentMenu.getImgAvatar().setImageBitmap(bmp);
							_rentMenu.getTvUsername().setText("点击登录");
							break;

						default:
							break;
						}
					}
				});
	}

	private static SinaResp _resp;
	private static RentHouseSideMenuFragment _rentMenu;

	public static Fragment getInstance(SlidingMenu sm, SinaResp resp,
			RentHouseSideMenuFragment rentMenu) {
		instance = new UserCenterFragment();
		_sm = sm;
		_resp = resp;
		_rentMenu = rentMenu;
		return instance;
	}
}
