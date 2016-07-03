package xyz.gatling.ora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by gimmi on 7/2/2016.
 */

public class SettingsItem extends View {

    public SettingsItem(Context context) {
        super(context);
        init();
    }

    public SettingsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = new Paint();
    private int titleSize;
    private int subtitleSize;
    private int xOrigin;
    private int yOrigin;
    private Bitmap rightPreview;
    int padding = (int)getDp(8);

    private String title;
    private String subtitle;

    private void init(){
        setBackgroundColor(Color.WHITE);
        titleSize = (int)getDp(18);
        subtitleSize = (int)getDp(12);
        xOrigin = (int)getDp(24);

        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        setRightPreview("100%");
    }

    public void setTitleAndSubtitle(String title, String subtitle){
        this.title = title;
        this.subtitle = subtitle;
    }

    public void setRightPreview(String string){
        int square = 300;
        int textSize = (int) getDp(square/7);
        Bitmap bitmap = Bitmap.createBitmap(square, square, Bitmap.Config.ARGB_8888);
        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(string, square/2, square/2+textSize/3, paint);
        setRightPreview(bitmap);
    }

    public void setRightPreview(Bitmap bitmap){
        this.rightPreview = bitmap;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                (int)getDp(62)
        );
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        yOrigin = h/2;
        rightPreview = Bitmap.createScaledBitmap(rightPreview, h-padding*2, h-padding*2, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(titleSize);
        canvas.drawText(title, xOrigin, yOrigin, paint);
        paint.setTextSize(subtitleSize);
        canvas.drawText(subtitle, xOrigin, yOrigin + titleSize, paint);

        if(rightPreview != null) {
            canvas.drawBitmap(rightPreview, getWidth() - xOrigin*2, padding, null);
        }

        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        canvas.drawLine(0, getHeight()-1, getWidth(), getHeight()-1, paint);
    }

    private float getDp(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
