package com.example.lenovo.ui.activity.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.LocationClient;
import com.example.lenovo.ui.R;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    ImageView imageView;
    Button btn_camera;
    Button btn_ablum;
    Uri imageUri;
    File newfile = null;
    String name;
    String abFile=null;
    // 存储路径
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/LeafEye图片";
    public static LocationClient locationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        locationClient=new LocationClient(this.getApplicationContext());
        imageView=(ImageView)findViewById(R.id.imageView);
        btn_camera = (Button) findViewById(R.id.btn_take);
        btn_ablum = (Button) findViewById(R.id.btn_ablum);
        final File parentFile=new File(PATH);
        if (!parentFile.exists())parentFile.mkdirs();
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                name = DateFormat.format("'IMG'_yyyyMMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA))
                        + ".jpg";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    newfile = new File(PATH, name);
                }
                abFile=newfile.getAbsolutePath();
                imageUri = Uri.fromFile(newfile);//uri传递数据
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将相机拍照结果，即原图，存储到imageUri路径
                startActivityForResult(intent, 1);
            }
        });
        btn_ablum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CameraActivity.this,AblumActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //照相结果
                   // bitmapOrg = BitmapFactory.decodeFile(newfile.getPath());
                    ContentResolver contentResolver = getContentResolver();
/*                    try {
                        //单独建立一个名为“图片”的文件夹，存储整个相册新增加的照片。更新“图片”相册
                        MediaStore.Images.Media.insertImage(contentResolver,abFile,newfile.getName(),null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    //通知媒体扫描文件，指定扫描文件为imageUri路径文件,只更新“LeafEye图片”相册
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(imageUri.toString())));
                    break;
            }
        }
    }
}
