package tk.sweetvvck.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义圆形带进度的进度条
 * @author 程科
 */
public class LoadingCircleView extends View {  
  
    private final  Paint paint;    
    private final Context context;    
    private Resources res;  
    private int progress;  
    private int ringWidth;  
    //圆环的颜色  
    private int ringColor;  
    //进度条颜色  
    private int progressColor;  
    //字体颜色  
    private int textColor;  
    //字的大小  
    private int textSize;  
      
    private String textProgress;  
      
    public LoadingCircleView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;    
        this.paint = new Paint();    
        this.res = context.getResources();  
        this.paint.setAntiAlias(true); //消除锯齿    
        this.ringWidth = dip2px(context, 10); //设置圆环宽度    
        this.ringColor = Color.rgb(233, 233, 233);  
        this.progressColor = Color.rgb(146, 206, 108);  
        this.textColor = Color.rgb(203, 203, 203);  
        this.textSize =  30;  
    }  
  
    public LoadingCircleView(Context context, AttributeSet attrs) {  
        this(context, attrs,0);  
    }  
  
    public LoadingCircleView(Context context) {  
        this(context,null);  
    }  
    /** 
     * 设置加载进度，取值范围在0~100之间 
     * @param progress 
     */  
    public void setProgress(int progress) {  
        if(progress>=0&&progress<=100){  
        this.progress = progress;  
        invalidate();  
        }  
    }  
    /** 
     * 设置圆环背景色 
     * @param ringColor 
     */  
    public void setRingColor(int ringColor) {  
        this.ringColor = res.getColor(ringColor);  
    }  
    /** 
     * 设置进度条颜色 
     * @param progressColor 
     */  
    public void setProgressColor(int progressColor) {  
        this.progressColor = res.getColor(progressColor);  
    }  
    /** 
     * 设置字体颜色 
     * @param textColor 
     */  
    public void setTextColor(int textColor) {  
        this.textColor = res.getColor(textColor);  
    }  
    /** 
     * 设置字体大小 
     * @param textSize 
     */  
    public void setTextSize(int textSize) {  
        this.textSize = textSize;  
    }  
    /** 
     * 设置圆环半径 
     * @param ringWidth 
     */  
    public void setRingWidthDip(int ringWidth) {  
        this.ringWidth = dip2px(context, ringWidth);  
    }  
    /** 
     * 通过不断画弧的方式更新界面，实现进度增加 
     */  
    @Override  
    protected void onDraw(Canvas canvas) {  
        int center = getWidth()/2;    
        int radios = center-ringWidth/2;  
            
            
        //绘制圆环    
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆     
        this.paint.setColor(ringColor);  
        this.paint.setStrokeWidth(ringWidth);    
        canvas.drawCircle(center,center, radios, this.paint);    
        RectF oval = new RectF(center-radios, center-radios, center+radios, center+radios);  
        this.paint.setColor(progressColor);  
        canvas.drawArc(oval, 90, 360*progress/100, false, paint);  
        this.paint.setStyle(Paint.Style.FILL);  
        this.paint.setColor(textColor);  
        this.paint.setStrokeWidth(0);  
        this.paint.setTextSize(textSize);  
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);  
        textProgress = progress+"%";  
        float textWidth = paint.measureText(textProgress);  
        canvas.drawText(textProgress, center-textWidth/2, center+textSize/2, paint);  
            
            
        super.onDraw(canvas);    
    }  
      
      /**  
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  
     */    
    public static int dip2px(Context context, float dpValue) {    
        final float scale = context.getResources().getDisplayMetrics().density;    
        return (int) (dpValue * scale + 0.5f);    
    }  }  