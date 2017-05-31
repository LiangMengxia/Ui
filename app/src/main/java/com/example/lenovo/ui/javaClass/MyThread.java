package com.example.lenovo.ui.javaClass;

import com.example.lenovo.ui.imageProcessing.Binary;

/**
 * Created by lenovo on 2017/5/30.
 */
public class MyThread extends Thread {
    int w;
    int h;
    public int[] input;
    public MyThread(int width,int height,int[] inputPixel){
        w=width;
        h=height;
        input=new int[w*h];
        System.arraycopy(inputPixel,0,input,0,inputPixel.length);
    }

    @Override
    public void run() {
        input=Binary.getBinaryImg(w, h, input);
        System.out.println(getName()+"完成");
    }

    public int[] getInput(){
        return input;
    }
}
