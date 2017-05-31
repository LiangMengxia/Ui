package com.example.lenovo.ui.imageProcessing;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.lenovo.ui.javaClass.Gauss;
import com.example.lenovo.ui.javaClass.MyThread;

/**
 * Created by lenovo on 2017/5/25.
 */
public class Correct {
    private int w;
    private int h;
    private int rectangleA;
    private int rectangleL;
    private int[] inputs;
    private int[] orgInput;
    private int[] leafPixels;
    private int[] rectanglePixels;
    private int[] frameworkPixels;
    private int[] refactPixels;
    private double[] disMatrix;
    private double CentralShaftLength;
    private double perimeter;
    private Bitmap bim2;
    private Bitmap bim3;

    public Correct() {

    }

    public Bitmap correctFun(Bitmap bitmap) {
        //初始化bitmap
        init(bitmap);
        //第一步：二值化
/*        long t0=System.currentTimeMillis();
        inputs = Binary.getBinaryImg(w, h, inputs);
        long t1=System.currentTimeMillis();
        System.out.println((t1-t0)+"ms");*/
        long t0=System.currentTimeMillis();
        int length=w*h/4;
        int[] input1=new int[length];
        int[] input2=new int[length];
        int[] input3=new int[length];
        int[] input4=new int[length];
        int i1=0,i2=0,i3=0,i4=0;
        for (int x=0;x<w/2;x++){
            for (int y=0;y<h/2;y++){
                input1[i1++]=inputs[y*w+x];
            }
        }
        for (int x=w/2;x<w;x++){
            for (int y=0;y<h/2;y++){
                input2[i2++]=inputs[y*w+x];
            }
        }
        for (int x=0;x<w/2;x++){
            for (int y=h/2;y<h;y++){
                input3[i3++]=inputs[y*w+x];
            }
        }
        for (int x=w/2;x<w;x++){
            for (int y=h/2;y<h;y++){
                input4[i4++]=inputs[y*w+x];
            }
        }
        MyThread thread1=new MyThread(w/2,h/2,input1);
        MyThread thread2=new MyThread(w/2,h/2,input2);
        MyThread thread3=new MyThread(w/2,h/2,input3);
        MyThread thread4=new MyThread(w/2,h/2,input4);
        thread1.setName("二值化1");
        thread2.setName("二值化2");
        thread3.setName("二值化3");
        thread4.setName("二值化4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        long t1 = System.currentTimeMillis();
        System.out.println(t1 - t0 + "ms");


/*        ConnectedClass connectedClass = new ConnectedClass();
        leafPixels = connectedClass.FindMaxConnectedArea(w, h, inputs);
        //四边形方框二值图像rectanglePixels
        rectanglePixels = minus(inputs, leafPixels);

        Framework rectangleFrame = new Framework();
        rectanglePixels = rectangleFrame.getFramework(w, h, rectanglePixels);

        LineHough lineHough=new LineHough();
        lineHough.init(w,h,rectanglePixels);
        rectanglePixels=lineHough.process(0,0,1);*/
/*        //第三步：检测直线
        LineDetect lines = new LineDetect();
        rectanglePixels = lines.lineDetect(w, h, 4, rectanglePixels);*/
        //第四步：Harris角点检测角点
/*        HarrisCorner harrisCorner = new HarrisCorner();
        rectanglePixels = harrisCorner.filter(w, h, rectanglePixels);//数组inputs用于后面的校正过程
        //一个角点位置有四个角就检测出了4个角点，故角点归一，把四个角点合成一个角点
        jiaoDianGuiYi();
        //第五步：校正,包括正变换和反变换
        Switch();*/
        for (int x=0;x<w;x++){
            for (int y=0;y<h;y++){
                bim3.setPixel(x,y,inputs[y*w+x]);
            }
        }
        return bim3;
    }

    private void init(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        w = width + 2;
        h = height + 2;
        int[] pixels = new int[width * height];
        inputs = new int[w * h];//扩展后的像素
        orgInput = new int[w * h];//原始图片像素
        leafPixels = new int[w * h];//叶片像素
        rectanglePixels = new int[w * h];//方框像素
        bim2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bim3 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[y * width + x] = bitmap.getPixel(x, y);
            }
        }
        //在原图像最外层，加一圈白点,得到新的像素点数组inputs
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = y * w + x;
                if (x == 0 || x == w - 1 || y == 0 || y == h - 1)
                    inputs[index] = Color.WHITE;
                else {
                    int oldindex = (y - 1) * width + x - 1;
                    inputs[index] = pixels[oldindex];
                }
            }
        }
        System.arraycopy(inputs, 0, orgInput, 0, inputs.length);
    }

    //角点归一
    private void jiaoDianGuiYi() {
        int r = 10;
        //因为在图像边框位置竟然检测出了角点，所以角点归一的时候不遍历边框位置,y的范围为1到h-2
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int sxx = 0;
                int syy = 0;
                int n = 0;
                if (rectanglePixels[y * w + x] == Color.GREEN) {
                    for (int ry = -r; ry <= r; ry++) {
                        int yy = y + ry;
                        if (yy < 0) yy = 0;
                        if (yy > h) yy = h - 1;
                        for (int rx = -r; rx <= r; rx++) {
                            int xx = x + rx;
                            if (xx < 0) xx = 0;
                            if (xx > w) xx = w - 1;
                            if (rectanglePixels[yy * w + xx] == Color.GREEN) {
                                sxx += xx;
                                syy += yy;
                                n++;
                            }
                        }
                    }
                }
                if (n != 0) {
                    sxx = sxx / n;
                    syy = syy / n;
                    rectanglePixels[syy * w + sxx] = Color.RED;//把归一后的角点像素值设为红色
                }
            }
        }
    }

    private void Switch() {
        //图形校正
        float[][] ax = new float[5][5];
        float[][] ay = new float[5][5];
        float[] bx = new float[5];
        float[] by = new float[5];
        float[] kx = new float[5];
        float[] ky = new float[5];
        float[][] fax = new float[5][5];
        float[][] fay = new float[5][5];
        float[] fbx = new float[5];
        float[] fby = new float[5];
        int[] pointxy = new int[2 * 100];
        int count = 0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (rectanglePixels[y * w + x] == Color.RED) {
                    pointxy[2 * count] = x;
                    pointxy[2 * count + 1] = y;
                    count++;
                    if (count <= 4) {
                        //ax,ay是进行正变换过程的传递参数
                        ax[count][1] = 1;
                        ax[count][2] = (float) x;
                        ax[count][3] = (float) y;
                        ax[count][4] = (float) (x * y);

                        ay[count][1] = 1;
                        ay[count][2] = (float) x;
                        ay[count][3] = (float) y;
                        ay[count][4] = (float) (x * y);
                        //fax,fay是进行反变换时的传递参数
                        fax[count][1] = 1;
                        fax[count][2] = (float) x;
                        fax[count][3] = (float) y;
                        fax[count][4] = (float) (x * y);

                        fay[count][1] = 1;
                        fay[count][2] = (float) x;
                        fay[count][3] = (float) y;
                        fay[count][4] = (float) (x * y);
                    }
                }
            }
        }
        double x1 = ax[1][2], y1 = ax[1][3];
        double x2 = ax[2][2], y2 = ax[2][3];
        double x3 = ax[3][2], y3 = ax[3][3];
        double x4 = ax[4][2], y4 = ax[4][3];
        float dis = (float) Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0));//第一个角点和第二个角点的距离
        rectangleA = ((int) dis + 1) * ((int) dis + 1);//标准正方形
        rectangleL = 4 * (int) dis;
        //第一个角点在第二个角点右侧
        if (x1 > x2) {
            //bx,by是进行正变换过程的传递参数
            bx[1] = (float) pointxy[0];
            bx[2] = (float) pointxy[0] - dis;
            by[1] = (float) pointxy[1];
            by[2] = (float) pointxy[1];
            //fbx,fby是进行反变换时的传递参数
            fbx[1] = (float) pointxy[0];
            fbx[2] = (float) pointxy[0] - dis;
            fby[1] = (float) pointxy[1];
            fby[2] = (float) pointxy[1];
            // 第三个角点在第四个角点的右侧
            if (x3 > x4) {
                bx[3] = (float) pointxy[0];
                bx[4] = (float) pointxy[0] - dis;
                by[3] = pointxy[1] + dis;
                by[4] = pointxy[1] + dis;

                fbx[3] = (float) pointxy[0];
                fbx[4] = (float) pointxy[0] - dis;
                fby[3] = pointxy[1] + dis;
                fby[4] = pointxy[1] + dis;
            }
            //第三个角点在第四个角点的左侧
            if (x3 < x4) {
                bx[3] = (float) pointxy[0] - dis;
                bx[4] = (float) pointxy[0];
                by[3] = pointxy[1] + dis;
                by[4] = pointxy[1] + dis;

                fbx[3] = (float) pointxy[0] - dis;
                fbx[4] = (float) pointxy[0];
                fby[3] = pointxy[1] + dis;
                fby[4] = pointxy[1] + dis;
            }
        }
        //第一个角点在第二个角点左侧
        if (x1 < x2) {
            bx[1] = (float) pointxy[0];
            bx[2] = (float) pointxy[0] + dis;
            by[1] = (float) pointxy[1];
            by[2] = (float) pointxy[1];

            fbx[1] = (float) pointxy[0];
            fbx[2] = (float) pointxy[0] + dis;
            fby[1] = (float) pointxy[1];
            fby[2] = (float) pointxy[1];
            //第三个角点在第四个角点的右侧
            if (x3 > x4) {
                bx[3] = (float) pointxy[0] + dis;
                bx[4] = (float) pointxy[0];
                by[3] = pointxy[1] + dis;
                by[4] = pointxy[1] + dis;

                fbx[3] = (float) pointxy[0] + dis;
                fbx[4] = (float) pointxy[0];
                fby[3] = pointxy[1] + dis;
                fby[4] = pointxy[1] + dis;
            }
            //第三个角点在第四个角点的左侧
            if (x3 < x4) {
                bx[3] = (float) pointxy[0];
                bx[4] = (float) pointxy[0] + dis;
                by[3] = pointxy[1] + dis;
                by[4] = pointxy[1] + dis;

                fbx[3] = (float) pointxy[0];
                fbx[4] = (float) pointxy[0] + dis;
                fby[3] = pointxy[1] + dis;
                fby[4] = pointxy[1] + dis;
            }
        }

        Gauss.setN(4);
        Gauss.elimination(ax, bx);
        kx = Gauss.back();

        Gauss.elimination(ay, by);
        ky = Gauss.back();
        //对原图进行校正
        double newx = 0, newy = 0;
        //实例化双线性插值对象，传入原图像素
        BilinearZoom bilinearZoom = new BilinearZoom(orgInput);

        //在原图中进行插值
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                newx = (kx[1] + kx[2] * x + kx[3] * y + kx[4] * x * y);
                newy = (ky[1] + ky[2] * x + ky[3] * y + ky[4] * x * y);
                newx = newx + 0.5;
                newy = newy + 0.5;
                if (newx < 0) {
                    newx = 0;
                }
                if (newx >= w) {
                    newx = w - 1;
                }

                if (newy < 0) {
                    newy = 0;
                }
                if (newy >= h) {
                    newy = h - 1;
                }
                int pointC = bilinearZoom.xyBlinear(newx, newy, w, h);
                bim3.setPixel((int) (newx), (int) (newy), pointC);
            }
        }
        //反变换
        oppsiteSwitch(fax, fbx, fby, orgInput);
    }

    //用于4个参考点的校正过程中，反变换
    private void oppsiteSwitch(float[][] fax, float[] fbx, float[] fby, int[] orginput) {
        float[][] a1 = new float[5][5];
        float[][] a2 = new float[5][5];
        float[] bx1 = new float[5];
        float[] by2 = new float[5];
        float[] kx = new float[5];
        float[] ky = new float[5];
        for (int i = 1; i <= 4; i++) {
            a1[i][1] = 1;
            a1[i][2] = fbx[i];
            a1[i][3] = fby[i];
            a1[i][4] = fbx[i] * fby[i];

            a2[i][1] = 1;
            a2[i][2] = fbx[i];
            a2[i][3] = fby[i];
            a2[i][4] = fbx[i] * fby[i];

            bx1[i] = fax[i][2];

            by2[i] = fax[i][3];
        }
        Gauss.setN(4);
        Gauss.elimination(a1, bx1);
        kx = Gauss.back();

        Gauss.elimination(a2, by2);
        ky = Gauss.back();
        double newx = 0, newy = 0;
        BilinearZoom bilinearZoom = new BilinearZoom(orginput);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                newx = (kx[1] + kx[2] * x + kx[3] * y + kx[4] * x * y);
                newy = (ky[1] + ky[2] * x + ky[3] * y + ky[4] * x * y);
                newx = newx + 0.5;
                newy = newy + 0.5;
                if (newx < 0) {
                    newx = 0;
                }
                if (newx >= w) {
                    newx = w - 1;
                }

                if (newy < 0) {
                    newy = 0;
                }
                if (newy >= h) {
                    newy = h - 1;
                }
                int pointC = bilinearZoom.xyBlinear(newx, newy, w, h);
                bim3.setPixel(x, y, pointC);
            }
        }
    }

    private int[] minus(int[] a, int[] b) {//a减b,同白同黑则为白，异为黑
        int[] c = new int[w * h];
        //前提条件：a包括b
        for (int index = 0; index < w * h; index++) {
            if (a[index] != b[index]) c[index] = Color.BLACK;
            else c[index] = Color.WHITE;
        }
        return c;
    }
}
