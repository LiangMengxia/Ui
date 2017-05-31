package com.example.lenovo.ui.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.ui.R;
import com.example.lenovo.ui.button.AnoBackImgTextBtn;
import com.example.lenovo.ui.javaClass.CircleImageView;

public class ShareActivity extends Activity implements View.OnClickListener {
    CircleImageView circleImageView1;
    CircleImageView circleImageView2;
    AnoBackImgTextBtn backspaceBtn;
    Button firstpageBtn;
    Button chooseNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initViews();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        Intent intent = getIntent();
        byte[] bitmapBytes = intent.getByteArrayExtra("bitmapbytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        circleImageView1 = (CircleImageView) findViewById(R.id.circleimage1);
        circleImageView1.setImageBitmap(bitmap);

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.checked_launcher);
        circleImageView2 = (CircleImageView) findViewById(R.id.circleimage2);
        circleImageView2.setImageBitmap(bitmap1);

        backspaceBtn = (AnoBackImgTextBtn) findViewById(R.id.backspaceBtn);
        backspaceBtn.setImageResource(R.drawable.back_gray_selector);
        backspaceBtn.setOnClickListener(this);

        firstpageBtn = (Button) findViewById(R.id.firstpageBtn);
        firstpageBtn.setOnClickListener(this);

        chooseNextBtn = (Button) findViewById(R.id.chooseNextBtn);
        chooseNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backspaceBtn:
                onBackPressed();//返回到前一个界面
                break;
            case R.id.firstpageBtn:
                Intent intent2 = new Intent(ShareActivity.this, CameraActivity.class);
                startActivity(intent2);
                break;
            case R.id.chooseNextBtn:
                //返回前一个activity，重新选择处理图片
                Intent intent1 = new Intent(ShareActivity.this, AblumActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
