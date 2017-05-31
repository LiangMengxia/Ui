package com.example.lenovo.ui.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.ui.R;

/**
 * Created by lenovo on 2017/5/3.
 */
public class SaveImgTextBtn extends RelativeLayout {
    ImageView image;
    TextView text;
    public SaveImgTextBtn(Context context){
        super(context);
    }
    public SaveImgTextBtn(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.save_img_text_btn,this,true);
        this.image = (ImageView) findViewById(R.id.imageview);
        this.text = (TextView) findViewById(R.id.textview);
        setFocusable(true);
        setClickable(true);
    }
}
