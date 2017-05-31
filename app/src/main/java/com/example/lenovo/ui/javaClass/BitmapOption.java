package com.example.lenovo.ui.javaClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by lenovo on 2017/5/24.
 * 将大图片bitmap缩小到1M以下
 */
public class BitmapOption {
    public BitmapOption(){}
    public static byte[] getSmallerBitmapBytes(Bitmap bitmap){
        ByteArrayOutputStream ops=new ByteArrayOutputStream();
        //将压缩的bitmap写入字节数组输出流ops，第二个参数设置为100的话就是没压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ops);
        byte[] bytes=ops.toByteArray();
        //通过BitmapFactory.Options来缩小图片，一定程度的避免了传递图片数据出现内存溢出
        BitmapFactory.Options options = new BitmapFactory.Options();
        //inJustDecodeBounds设置为true，BitmapFactory.decodeByteArray不返回bitmap
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeByteArray加载原图
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        /*
        * 获取采样率inSampleSize
        *inSampleSize大于1时，图片高、宽分别以2的inSampleSize次方分之一缩小
        *inSampleSize小于等于1时，图片高、宽不变
        * */
        options.inSampleSize = getinSampleSize(options);
        if (options.inSampleSize==1)return bytes;

        //inJustDecodeBounds设置为false，BitmapFactory.decodeByteArray返回bitmap
        options.inJustDecodeBounds = false;
        //BitmapFactory.decodeByteArray重新加载图片：通过option得到缩小的bitmap，并将缩小的bitmap字节序列化后返回
        Bitmap smallerBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        smallerBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] smallerbytes = byteArrayOutputStream.toByteArray();
        return smallerbytes;
    }

    //获取option采样率inSampleSize
    private static int getinSampleSize(BitmapFactory.Options options) {
        int inSampleSize = 1;
        int imageWidth = options.outWidth;//取出bitmap的原始高宽
        int imageHeight = options.outHeight;
        //个人认为intent，bundle传递图片的时候，当图片内存大于1024KB的时候，会发生内存溢出，
        // 所以为解决内存溢出问题，此处选择通过计算图片大小来查找缩放比例系数小于1024KB时，找到inSampleSize
        while (getImageMemory(imageWidth, imageHeight, inSampleSize) > 1024) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    //24位位图内存大小计算
    private static int getImageMemory(int imagewidth, int imageheight, int inSampleSize) {
        return (imagewidth / inSampleSize) * (imageheight / inSampleSize) * 3 / 1024;
    }
}
