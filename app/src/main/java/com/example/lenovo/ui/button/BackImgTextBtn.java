package com.example.lenovo.ui.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.ui.R;

/**
 * Created by lenovo on 2017/5/15.
 */
public class BackImgTextBtn extends RelativeLayout {
    ImageView imageview;
    TextView textview;
    public BackImgTextBtn(Context context){
        super(context);
    }
    public BackImgTextBtn(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.back_img_text_btn,this,true);
        this.imageview = (ImageView) findViewById(R.id.backimageview);
        this.textview = (TextView) findViewById(R.id.backtextview);
        setFocusable(true);
        setClickable(true);
    }
    public void setImageResource(int imageviewId) {
        this.imageview.setImageResource(imageviewId);
    }

    public void setText(int textId) {
        this.textview.setText(textId);
    }

    public void setText(CharSequence text) {
        this.textview.setText(text);
    }
    public void setTextColor(ColorStateList colorStateList) {
        this.textview.setTextColor(colorStateList);
    }
    public void setTextColor(int color) {
     this.textview.setTextColor(color);
    }
    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }
}
