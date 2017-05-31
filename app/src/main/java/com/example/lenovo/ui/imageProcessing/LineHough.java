package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

/**
 * Created by lenovo on 2016/6/21.
 */
public class LineHough {
    private int[] input;
    private int[] output;
    private int width;
    private int height;
    private int w;
    private int h;
    private int[] acc;
    private int[] bcc;
    private int accSize = 4;
    private int[] results;
    private double[] sinValue;
    private double[] cosValue;

    public LineHough() {
        System.out.println("Hough LineHough Detection...");
    }

    public void init(int widthIn, int heightIn,int[] inputIn) {
        width = widthIn;
        height = heightIn;
        input = new int[width * height];
        output = new int[width * height];
        input = inputIn;
        //这个地方必须先给output数组所有的元素设置为白色，之后检测为直线的部分被设置为黑色，这样最终output显示的就是一副黑白图。
        //如果没有下面这步对output数组的初始化，则output数组元素最终为0值或者黑色值，为0值的像素点是透明的，这个会导致后面的角点检测检测不到角点
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                output[j * width + i] = Color.WHITE;
            }
        }
        sinValue = new double[360];
        cosValue = new double[360];
        for (int theta = 0; theta < 360; theta++) {
            sinValue[theta] = Math.sin((theta * Math.PI) / 180);
            cosValue[theta] = Math.cos((theta * Math.PI) / 180);
        }
    }

    public int[] process(int i, int j, int n) {
        w = width / n;//如果width，height未先在init（）中另赋初值，则此处w=0，h=0，需注意赋值先后顺序
        h = height / n;
        int rmax = (int) Math.sqrt(w * w + h * h) + 1;//在w*h方块内r的最大值
        acc = new int[(rmax + 1) * 180 + 181];
        bcc = new int[(rmax + 1) * 180 + 181];
        int r;
        for (int x = i; x < i + w && x < width; x++) {
            for (int y = j; y < j + h && y < height; y++) {
                if (input[y * width + x] == Color.BLACK) {
                    for (int theta = 0; theta < 181; theta = theta + 1) { //r=((int)(x * Math.cos(((theta) * Math.PI) / 180) + y * Math.sin(((theta) * Math.PI) / 180))/2)*2;
                        r = (int) ((x - i) * cosValue[theta] + (y - j) * sinValue[theta]);
                        if ((r >= 0) && (r <= rmax))
                            acc[r * 180 + theta] = acc[r * 180 + theta] + 1;
                        if ((r < 0) && (r > -rmax))
                            bcc[Math.abs(r) * 180 + theta] = bcc[Math.abs(r) * 180 + theta] + 1;
                    }
                }
            }
        }
        findMaxima(i, j);
        return output;
    }

    private int[] findMaxima(int i, int j) {
        int rmax = (int) Math.sqrt(w * w + h * h) + 1;
        results = new int[accSize * 3];
        int value = 0;
        int value1 = 0;
        int value2 = 0;
        for (int r = 0; r <= rmax; r++) {
            for (int theta = 0; theta < 181; theta++) {
                value1 = acc[r * 180 + theta];
                value2 = bcc[r * 180 + theta];
                if (value1 >= value2) {
                    value = value1;
                    getResults(value, r, theta);
                } else {
                    value = value2;
                    getResults(value, -r, theta);
                }
            }
        }
        for (int n = 0; n < accSize; n++) {
            if (results[n * 3] > 0)
                drawPolarLine(results[n * 3], results[n * 3 + 1], results[n * 3 + 2], i, j);
        }
        return output;
    }

    private void getResults(int value, int r, int theta) {
        if (value > results[(accSize - 1) * 3]) {
            results[(accSize - 1) * 3] = value;
            results[(accSize - 1) * 3 + 1] = r;
            results[(accSize - 1) * 3 + 2] = theta;

            int n = (accSize - 2) * 3;
            while ((n >= 0) && (results[n + 3] > results[n])) {
                for (int m = 0; m < 3; m++) {
                    int temp = results[n + m];
                    results[n + m] = results[n + 3 + m];
                    results[n + 3 + m] = temp;
                }
                n = n - 3;
                if (n < 0)
                    break;
            }
        }
    }

    private void drawPolarLine(int value, int r, int theta, int i, int j) {
        for (int x = i; x < i + w && x < width; x++) {
            for (int y = j; y < j + h && y < height; y++) {
                // int temp = ((int) (x * Math.cos(((theta) * Math.PI) / 180) + y * Math.sin(((theta) * Math.PI) / 180))/2)*2;
                // if (input[y * width + x] == Color.BLACK) {
                int temp = (int) ((x - i) * cosValue[theta] + (y - j) * sinValue[theta]);
                if (temp == r) {
                    output[y * width + x] = Color.BLACK;
                }
            }
        }
    }
    //  }
}
