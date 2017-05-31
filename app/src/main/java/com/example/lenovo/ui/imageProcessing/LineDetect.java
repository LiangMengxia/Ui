package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/6/21.
 */
public class LineDetect {
    private int w;
    private int h;
    private int rmax;
    private int lineNum;
    private int[] output;
    private double[] sinValue;
    private double[] cosValue;
    private Acc[] accSum;//r>0累加器数组
    private Acc[] bccSum;//r<0累加器数组
    private Acc[] abccSum;//总的累加器

    //Acc累加器，同一个r，theta的点进行累加，累加个数为val
    public class Acc {
        private int r = 0;
        private int theta = 0;
        private int val = 0;

        public Acc() {
        }
    }

    public int[] lineDetect(int width, int height, int lineNumber, int[] input) {
        init(width, height, lineNumber);
        try {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    if (input[y * w + x] == Color.BLACK) {
                        for (int theta = 0; theta < 181; theta++) { //r=((int)(x * Math.cos(((theta) * Math.PI) / 180) + y * Math.sin(((theta) * Math.PI) / 180))/2)*2;
                            int r = (int) (x * cosValue[theta] + y * sinValue[theta]);
                            if ((r >= 0) && (r < rmax)) {
                                int index = r * 180 + theta;
                                accSum[index].r = r;
                                accSum[index].theta = theta;
                                accSum[index].val++;
                            }
                            if ((r < 0) && (r > -rmax)) {
                                int index = Math.abs(r) * 180 + theta;
                                bccSum[index].r = r;
                                bccSum[index].theta = theta;
                                bccSum[index].val++;
                            }
                        }
                    }
                }
            }
            rankAccSum(accSum);
            rankAccSum(bccSum);
            //合并acc和bcc
            for (int i = 0; i < lineNum; i++) {
                abccSum[i] = accSum[i];
                abccSum[i + lineNum] = bccSum[i];
            }
            rankAccSum(abccSum);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        for (int i = 0; i < lineNum; i++) {
            drawLine(abccSum[i].r, abccSum[i].theta);
        }
        return output;
    }

    private void init(int width, int height, int lineNumber) {
        w = width;
        h = height;
        lineNum = lineNumber;
        output = new int[w * h];
        rmax = (int) Math.sqrt(w * w + h * h) + 1;//在w*h方块内r的最大值
        accSum = new Acc[rmax * 180 + 181];
        bccSum = new Acc[rmax * 180 + 181];
        abccSum = new Acc[2 * lineNum];
        sinValue = new double[181];
        cosValue = new double[181];
        Arrays.fill(output,Color.WHITE);
        for (int theta = 0; theta < 181; theta++) {
            sinValue[theta] = Math.sin((theta * Math.PI) / 180);
            cosValue[theta] = Math.cos((theta * Math.PI) / 180);
        }
        //初始化累加器数组
        for (int i = 0; i < accSum.length; i++) {
            accSum[i] = new Acc();
            bccSum[i] = new Acc();
        }
        for (int i = 0; i < abccSum.length; i++) {
            abccSum[i] = new Acc();
        }
    }

    /*
    * 排序Acc数组，只对前面几个Acc进行排序，找出lineSize个较大的Acc
    * */
    private void rankAccSum(Acc[] accs) {
        for (int i = 0; i < lineNum; i++) {
            int max = i;
            for (int j = i + 1; j < accs.length; j++) {
                if (accs[j].val > accs[max].val) {
                    max = j;
                }
            }
            if (max != i) {
                Acc acc = accs[max];
                accs[max] = accs[i];
                accs[i] = acc;
            }
        }
    }

    //画出lineNum条直线
    private void drawLine(int r, int theta) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int temp = (int) (x * cosValue[theta] + y * sinValue[theta]);
                if (temp==r) {
                    output[y * w + x] = Color.BLACK;
                } else output[y * w + x] = Color.WHITE;
            }
        }
    }

    //标记累加器存储直线所在点的坐标（xi,yi），存储形式为x序列和y序列
    private void drawLine(List<Integer> listx, List<Integer> listy) {
        for (int i = 0; i < listx.size(); i++) {
            int x = listx.get(i);
            int y = listy.get(i);
            output[y * w + x] = Color.BLACK;
        }
    }

}
