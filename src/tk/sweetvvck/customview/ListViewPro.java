package tk.sweetvvck.customview;

import tk.sweetvvck.shortrendhouse.R;
import tk.sweetvvck.shortrendhouse.fragment.UserCenterFragment;
import tk.sweetvvck.utils.TouchTool;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Class: ListViewPro.java<br>
 * Date: 2013/04/04<br>
 * Author: TiejianSha <br>
 * Email: tntshaka@gmail.com<br>
 */

public class ListViewPro extends ListView {

	private Context mContext;
	private Scroller mScroller;
	TouchTool tool;
	int left, top;
	float startX, startY, currentX, currentY;
	int bgViewH, iv1W;
	int rootW, rootH;
	View headView;
	View bgView;
	boolean scrollerType;
	static final int len = 0xc8;

	public ListViewPro(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ListViewPro(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mScroller = new Scroller(mContext);
	}

	public ListViewPro(Context context) {
		super(context);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (!mScroller.isFinished()) {
			return super.onTouchEvent(event);
		}
		headView = UserCenterFragment.itemHead1;
		bgView = headView.findViewById(R.id.path_headimage);
		currentX = event.getX();
		currentY = event.getY();
		headView.getTop();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i("ListView2", "ACTION_DOWN!!!");
			left = bgView.getLeft();
			top = bgView.getBottom();
			rootW = getWidth();
			rootH = getHeight();
			bgViewH = bgView.getHeight();
			startX = currentX;
			startY = currentY;
			tool = new TouchTool(bgView.getLeft(), bgView.getBottom(),
					bgView.getLeft(), bgView.getBottom() + len);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("ListView2", "ACTION_MOVE!!!");
			Log.i("ListView2", "currentX" + currentX);
			Log.i("ListView2", "currentY" + currentY);
			Log.i("ListView2", "headView.getTop()=" + headView.getTop());
			Log.i("ListView2", "headView.isShown()=" + headView.isShown());

			if (headView.isShown() && headView.getTop() >= 0) {
				if (tool != null) {
					int t = tool.getScrollY(currentY - startY);
					if (t >= top && t <= headView.getBottom() + len) {
						bgView.setLayoutParams(new RelativeLayout.LayoutParams(
								bgView.getWidth(), t));
					}
				}
				scrollerType = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("ListView2", "ACTION_UP!!!");
			scrollerType = true;
			mScroller.startScroll(bgView.getLeft(), bgView.getBottom(),
					0 - bgView.getLeft(), bgViewH - bgView.getBottom(), 500);
			invalidate();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			System.out.println("x=" + x);
			System.out.println("y=" + y);
			bgView.layout(0, 0, x + bgView.getWidth(), y);
			invalidate();
			// �ص�
			if (!mScroller.isFinished() && scrollerType && y > 200) {// �ص��ж�
				bgView.setLayoutParams(new RelativeLayout.LayoutParams(bgView
						.getWidth(), y));
			}
		}
	}
}
