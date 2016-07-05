package xyz.gatling.ora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by gimmi on 7/2/2016.
 */

public class SettingsItemView extends View {

    public SettingsItemView(Context context) {
        super(context);
        init();
    }

    public SettingsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = new Paint();
    private int titleSize;
    private int subtitleSize;
    private int xOrigin;
    private int yOrigin;
    private Bitmap rightPreview;
    int padding;
    int previewWidth = 100, previewHeight = 100;

    private String title;
    private String subtitle;

    private void init(){
        setBackgroundColor(Color.WHITE);
        titleSize = (int)Util.getDp(getContext(),18);
        subtitleSize = (int)Util.getDp(getContext(),12);
        xOrigin = (int)Util.getDp(getContext(),18);
        padding = (int) Util.getDp(getContext(), 8);

        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        setRightPreview("100%");
    }

    @Override
    public boolean isDrawingCacheEnabled() {
        return false;
    }

    public void setTitleAndSubtitle(String title, String subtitle){
        this.title = title;
        this.subtitle = subtitle;
    }

    public void setRightPreview(String string){
        int textSize = (int) Util.getDp(getContext(), previewWidth/7);
        Bitmap bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        if(!TextUtils.isEmpty(string)) {
            paint.setTextSize(textSize);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.CENTER);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawText(string, previewWidth / 2, previewHeight / 2 + textSize / 3, paint);
        }
        setRightPreview(bitmap);
    }

    public void setRightPreview(Bitmap bitmap){
        this.rightPreview = bitmap;
        resizePreview();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                (int)Util.getDp(getContext(), 62)
        );
    }

    private void resizePreview(){
        if(rightPreview != null) {
            rightPreview = Bitmap.createScaledBitmap(rightPreview, previewWidth, previewHeight, false);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        yOrigin = h/2;
        previewWidth = h - padding * 2;
        previewHeight = h - padding * 2;
        resizePreview();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(titleSize);
        canvas.drawText(title, xOrigin, yOrigin, paint);
        paint.setTextSize(subtitleSize);
        canvas.drawText(subtitle, xOrigin, yOrigin + titleSize, paint);

        if(rightPreview != null) {
            canvas.drawBitmap(rightPreview, getWidth() - getWidth()/5, padding, null);
        }

        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        canvas.drawLine(0, getHeight()-1, getWidth(), getHeight()-1, paint);
    }


}
