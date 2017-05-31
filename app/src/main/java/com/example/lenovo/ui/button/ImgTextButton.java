package com.example.lenovo.ui.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.ui.R;

/**
 * Created by lenovo on 2017/5/3.
 */
public class ImgTextButton extends RelativeLayout {
    private ImageView image;
    private TextView text;

    public ImgTextButton(Context context) {
        super(context, null);
    }

    public ImgTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.img_text_btn, this, true);
        this.image = (ImageView) findViewById(R.id.imageview);
        this.text = (TextView) findViewById(R.id.textview);
        setFocusable(true);
        setClickable(true);
    }

    public void setImageResource(int imageviewId) {
        this.image.setImageResource(imageviewId);
    }


    public void setImageBackgound(Drawable drawable) {
        this.image.setBackground(drawable);
    }

    public void setText(int textId) {
        this.text.setText(textId);
    }

    public void setText(CharSequence text) {
        this.text.setText(text);
    }

    public void setTextColor(int color) {
        this.text.setTextColor(color);
    }

    public void setTextSize(int size) {
        this.text.setTextSize(size);
    }

    public void setBackgound(int color) {
        this.setBackgroundColor(color);
    }
}
