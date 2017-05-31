package com.example.lenovo.ui.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.ui.R;
import com.example.lenovo.ui.button.BackImgTextBtn;
import com.example.lenovo.ui.button.ImgTextButton;
import com.example.lenovo.ui.button.SaveImgTextBtn;
import com.example.lenovo.ui.imageProcessing.Correct;
import com.example.lenovo.ui.javaClass.BitmapOption;
import com.example.lenovo.ui.javaClass.MyLocation;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class AblumActivity extends Activity implements View.OnClickListener {
    final int ablum_requestCode = 1;
    final int jianQie_requestCode = 2;
    ImageView imageView = null;
    ImgTextButton detailsBtn = null;
    ImgTextButton jianqieBtn = null;
    ImgTextButton jiaozhengBtn = null;
    ImgTextButton borderBtn = null;
    ImgTextButton zhongzhouBtn = null;
    ImgTextButton canshuBtn = null;
    SaveImgTextBtn saveBtn = null;
    BackImgTextBtn backImgTextBtn = null;
    Bitmap orgbitmap = null;
    Bitmap jianQiebitmap = null;
    Bitmap savingBitmap = null;
    Uri imageUri = null;
    Uri jianQieUri = null;
    File jianQieFile = null;
    String message = null;
    String imageName = null;
    String imagePath = null;
    private static final String PATH1 = Environment.getExternalStorageDirectory().toString() + "/DCIM/剪切后的图片";
    private static final String PATH2 = Environment.getExternalStorageDirectory().toString() + "/DCIM/校正后的图片";
    int imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablum);
        initViews();//初始化所有的View控件
        final File parentFile1 = new File(PATH1);
        if (!parentFile1.exists()) parentFile1.mkdirs();
        final File parentFile2 = new File(PATH2);
        if (!parentFile2.exists()) parentFile2.mkdirs();

        final Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent1, 1);
    }

    private void initViews() {
        imageView = (ImageView) findViewById(R.id.image);

        detailsBtn = (ImgTextButton) findViewById(R.id.details);
        detailsBtn.setImageResource(R.drawable.details_selector);
        detailsBtn.setText(R.string.details);
        detailsBtn.setOnClickListener(this);

        jianqieBtn = (ImgTextButton) findViewById(R.id.jianqie);
        jianqieBtn.setImageResource(R.drawable.jianqie_selector);
        jianqieBtn.setText(R.string.jianqie);
        jianqieBtn.setOnClickListener(this);

        jiaozhengBtn = (ImgTextButton) findViewById(R.id.jiaozheng);
        jiaozhengBtn.setImageResource(R.drawable.jiaozheng_selector);
        jiaozhengBtn.setText(R.string.jiaozheng);
        jiaozhengBtn.setOnClickListener(this);

        borderBtn = (ImgTextButton) findViewById(R.id.border);
        borderBtn.setImageResource(R.drawable.border_selector);
        borderBtn.setText(R.string.border);
        borderBtn.setOnClickListener(this);

        zhongzhouBtn = (ImgTextButton) findViewById(R.id.zhongzhou);
        zhongzhouBtn.setImageResource(R.drawable.zhongzhou_selector);
        zhongzhouBtn.setText(R.string.zhongzhou);
        zhongzhouBtn.setOnClickListener(this);

        canshuBtn = (ImgTextButton) findViewById(R.id.canshu);
        canshuBtn.setImageResource(R.drawable.canshu_selector);
        canshuBtn.setText(R.string.canshu);
        canshuBtn.setOnClickListener(this);

        saveBtn = (SaveImgTextBtn) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        backImgTextBtn = (BackImgTextBtn) findViewById(R.id.backBtn);
        backImgTextBtn.setImageResource(R.drawable.back_white_selector);
        backImgTextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details:
                //获取定位信息message
                MyLocation.getLocationDetails();
                message = MyLocation.message;
                //AlertDialog对话框显示定位信息和图片信息
                AlertDialog.Builder builder = new AlertDialog.Builder(AblumActivity.this);
                builder.setTitle("详细信息");
                builder.setMessage(message + "\n" + "名称: " + imageName + "\n" + "路径: " + imagePath + "\n" + "大小: " + imageSize + "KB");
                builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.jianqie:
                jianqieFunc();
                break;
            case R.id.jiaozheng:
                Correct correct = new Correct();
                Bitmap bitmap = correct.correctFun(savingBitmap);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.border:
                break;
            case R.id.zhongzhou:
                break;
            case R.id.canshu:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("面积：" + "\n" + "周长：" + "\n" + "中轴：" + "\n");
                builder1.setPositiveButton("保存参数", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case R.id.saveBtn:
                /*
                *依照使用者的操作来决定是保存原图还是处理后的图片
                *所以在得到原图或者处理后的图片的时候，我都把对应的bitmap传到了中间变量savingBitmap
                * */
                byte[] smallerbytes = BitmapOption.getSmallerBitmapBytes(savingBitmap);
                Intent intent1 = new Intent(AblumActivity.this, ShareActivity.class);
                intent1.putExtra("bitmapbytes", smallerbytes);
                startActivity(intent1);
                break;
            case R.id.backBtn:
                Intent intent2 = new Intent(AblumActivity.this, CameraActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /*
    * 调用系统相册，并返回选择的照片；
    * 剪切图片，并保存，且更新剪切图片相册
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ablum_requestCode: {
                ContentResolver contentResolver = getContentResolver();
                imageUri = data.getData();//uri传递数据
                //strings是需要查询的数据列,图片名称，图片绝对路径，图片大小（单位bytes）
                String[] strings = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
                //query查询数据库
                Cursor cursor = contentResolver.query(imageUri, strings, null, null, null);
                try {
                    //cursor.moveToFirst()，光标cursor必须移动到第一行的前面，从该列第一行查询到最后一行，查询完后要关闭cursor
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        imageName = cursor.getString(0);
                        imagePath = cursor.getString(1);
                        imageSize = cursor.getInt(2) / 1024;
                    }
                    cursor.close();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    orgbitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                    savingBitmap = orgbitmap;//把原始图片传给savingBitmap，savingBitmap用作保存与分享操作的传递图片
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(orgbitmap);
                break;
            }
            case jianQie_requestCode:
                ContentResolver contentResolver = getContentResolver();
                try {
                    jianQiebitmap = MediaStore.Images.Media.getBitmap(contentResolver, jianQieUri);
                    savingBitmap = jianQiebitmap;//把剪切得到的图片传给savingBitmap，savingBitmap用作保存与分享操作的传递图片
                    imageView.setImageBitmap(jianQiebitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //通知媒体扫描文件，指定扫描文件为jianQieUri路径文件,只更新"剪切后的图片"相册
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(jianQieUri.toString())));
                break;
        }
    }

    private void jianqieFunc() {
        String jianQieName = DateFormat.format("'IMG'_yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            jianQieFile = new File(PATH1, jianQieName);
        }
        jianQieUri = Uri.fromFile(jianQieFile);//将剪切后的图片存储到jianQieFile路径
        int dp = 300;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", dp);//输出X方向的像素
        intent.putExtra("outputY", dp);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, jianQieUri);//保存输出结果到jianQieUri
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, 2);
    }

}