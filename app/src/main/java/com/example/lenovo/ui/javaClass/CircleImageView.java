package com.example.lenovo.ui.javaClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/5/15.
 * 实现圆形view视图
 */
public class CircleImageView extends View {
    private Paint mPaint;
    private Matrix matrix;
    int circleWidth;
    Bitmap bitmap;

    public CircleImageView(Context context) {
        super(context);
        init();
    }
    public CircleImageView(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }
    public CircleImageView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        //去锯齿效果
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(circleWidth, circleWidth);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(circleWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, circleWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int radius = Math.min(width, height) / 2;
        setBitmapShader();
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }

    public void setImageBitmap(Bitmap srcbitmap) {
        bitmap = srcbitmap;
    }

    private void setBitmapShader() {
        double scale = 1;
        float dx = 0, dy = 0;
        BitmapShader bitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //图片宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        //视图宽高
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        //计算缩放比例
        int bSize = Math.min(bitmapWidth, bitmapHeight);
        scale = circleWidth * 1.0 / bSize;
        if (bitmapWidth * viewHeight > bitmapHeight * viewWidth) {
            dx = (float) ((viewWidth - bitmapWidth * scale) * 0.5f);
        } else {
            dy = (float) ((viewHeight - bitmapHeight * scale) * 0.5f);
        }
        matrix.setScale((float) scale, (float) scale);
        matrix.postTranslate(dx, dy);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);
    }

}

