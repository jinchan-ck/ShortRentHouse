package tk.sweetvvck.utils;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Utils {

	/**
	 * 设置listview的高度。解决listview嵌套listview的时候，无法完整显示里边listview的高度问题。
	 * 使用条件：item必须为LinearLayout
	 * 用法：在setAdapter之后调用。
	 * @param listView 需要被计算高度的listview（子listview）
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {    
        ListAdapter listAdapter = listView.getAdapter();     
        if (listAdapter == null) {    
            return;    
        }    
    
        int totalHeight = 0;    
        for (int i = 0; i < listAdapter.getCount(); i++) {    
            View listItem = listAdapter.getView(i, null, listView);  
            if(listItem != null)
            	listItem.measure(0, 0);    
            totalHeight += listItem.getMeasuredHeight();    
        }    
    
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    }
	/**
	 * 把resID转换成drawable
	 * @param context
	 * @param resID (R.drawable.xxx)
	 * @return drawable
	 */
	public static Drawable ResIDToDrawable(Context context, int resID){
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				resID);
		BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
	return bd;
	}
	/**
	 * 设置文字字体
	 * @param view 被设置TextView
	 * @param typeface 指定字库名称（如果为空，默认字体为Roboto.ttf）
	 */
	public static void setFontType(Context context, TextView view, String typeface){
		Typeface face;
		if(typeface == null || "".equals(typeface)){
			face = Typeface.createFromAsset (context.getAssets(), "fonts/Roboto.ttf");
		}else{
			face = Typeface.createFromAsset (context.getAssets(), "fonts/"+typeface);
		}
		view.setTypeface(face);
//		view.setTextColor(Color.rgb(255, 0, 0));
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    /** 
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素) 
     */
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp 
     */
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    } 
    
    /**
     * 图片切圆角
     * @param drawable
     * @param roundPx
     * @return
     */
	public static Bitmap getRoundRectBitmap(Drawable drawable, float roundPx) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		bitmap = getRoundRectBitmap(bitmap, roundPx);
		return bitmap;
	}
	/**
     * 图片切圆角
     * @param bitmap
     * @param roundPx
     * @return
     */
	public static Bitmap getRoundRectBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	/**
	 * 拼接多张图片成一张图片 (超过4张图，显示4张图)
	 * @param bitmaps 存储bitmap的list
	 * @param hasFrame 是否需要画边框
	 * @return 拼装好的bitmap
	 */
	public static Bitmap PackBitmaps (List<Bitmap> bitmaps, boolean hasFrame) {
		if(bitmaps == null)
			return null;
		Paint paint = null;
		int list_size = bitmaps.size();
		int width = bitmaps.get(0).getHeight();
		int height = bitmaps.get(0).getHeight();
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		if(hasFrame){
			paint = new Paint();
			paint.setStrokeWidth(2);
			paint.setDither(true);
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setColor(0xff424242);
		}
		if(bitmaps.size()>3){
			Rect r1_1 = new Rect(0, 0, bitmaps.get(list_size-4).getWidth()/2, bitmaps.get(list_size-4).getHeight()/2);
			canvas.drawBitmap(bitmaps.get(list_size-4), null, r1_1, null);
			
			Rect r2_1 = new Rect(bitmaps.get(list_size-3).getWidth()/2, 0, bitmaps.get(list_size-3).getWidth(), bitmaps.get(list_size-3).getHeight()/2);
			canvas.drawBitmap(bitmaps.get(list_size-3), null, r2_1, null);
			
			Rect r3_1 = new Rect(0, bitmaps.get(list_size-2).getHeight()/2, bitmaps.get(list_size-2).getWidth()/2, bitmaps.get(list_size-2).getHeight());
			canvas.drawBitmap(bitmaps.get(list_size-2), null, r3_1, null);
			
			Rect r4_1 = new Rect(bitmaps.get(list_size-1).getWidth()/2, bitmaps.get(list_size-1).getHeight()/2, bitmaps.get(list_size-1).getWidth(), bitmaps.get(list_size-1).getHeight());
			canvas.drawBitmap(bitmaps.get(list_size-1), null, r4_1, null);
			
			//画边框
			if(hasFrame){
//				canvas.drawRoundRect(new RectF(1, 1, width-1, height-1), 5, 5, paint);//画出圆角边框
				canvas.drawRect(1, 1, width-1, height-1, paint);
				canvas.drawLine(width/2, 0, width/2, height, paint);
				canvas.drawLine(0, height/2, width, height/2, paint);
			}
		}else if(bitmaps.size()>2){
			Rect r1_1 = new Rect(0, 0, bitmaps.get(0).getWidth()/2, bitmaps.get(0).getHeight()/2);
			canvas.drawBitmap(bitmaps.get(0), null, r1_1, null);
			
			Rect r2_1 = new Rect(bitmaps.get(1).getWidth()/2, 0, bitmaps.get(1).getWidth(), bitmaps.get(1).getHeight()/2);
			canvas.drawBitmap(bitmaps.get(1), null, r2_1, null);
			
			Rect r3_1 = new Rect(0, bitmaps.get(2).getHeight()/2, bitmaps.get(2).getWidth()/2, bitmaps.get(2).getHeight());
			canvas.drawBitmap(bitmaps.get(2), null, r3_1, null);
			if(bitmaps.size()>3){
				Rect r4_1 = new Rect(bitmaps.get(3).getWidth()/2, bitmaps.get(3).getHeight()/2, bitmaps.get(3).getWidth(), bitmaps.get(3).getHeight());
				canvas.drawBitmap(bitmaps.get(3), null, r4_1, null);
			}
			//画边框
			if(hasFrame){
				canvas.drawRect(1, 1, width-1, height-1, paint);
				canvas.drawLine(width/2, 0, width/2, height, paint);
				canvas.drawLine(0, height/2, width, height/2, paint);
			}
		}else if(bitmaps.size()>1){
			//定义图片显示区域
			Rect r1_0 = new Rect(bitmaps.get(0).getWidth()/4, 0, bitmaps.get(0).getWidth()-bitmaps.get(0).getWidth()/4, bitmaps.get(0).getHeight());
			//图片要展示的位置大小
			Rect r1_1 = new Rect(0, 0, bitmaps.get(0).getWidth()/2, bitmaps.get(0).getHeight());
			canvas.drawBitmap(bitmaps.get(0), r1_0, r1_1, null);
			
			Rect r2_0 = new Rect(bitmaps.get(1).getWidth()/4, 0, bitmaps.get(1).getWidth()-bitmaps.get(1).getWidth()/4, bitmaps.get(1).getHeight());
			Rect r2_1 = new Rect(bitmaps.get(1).getWidth()/2, 0, bitmaps.get(1).getWidth(), bitmaps.get(1).getHeight());
			canvas.drawBitmap(bitmaps.get(1), r2_0, r2_1, null);
			//画边框
			if(hasFrame){
				canvas.drawRect(1, 1, width-1, height-1, paint);
				canvas.drawLine(width/2, 0, width/2, height, paint);
			}
		}else{
			return bitmaps.get(0);
		}
		return result;
	}
	
	/**
	 * 创建一个简单的进度框
	 * @param progressDialog 要显示进度框的界面的进度框引用
	 * @param context 上下文对象
	 * @param message 进度框显示的内容
	 * @return 创建的进度框对象
	 */
	public static ProgressDialog createSimpleProgressDialog(ProgressDialog progressDialog, Context context, String message) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.show();
		return progressDialog;
	}
	
	/**
	 * 让进度框消失
	 * @param progressDialog 要dismiss的进度框
	 */
	public static void dismissProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
