package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/10.
 */
public class DminClass {
    int w, h;
    int x0 = 0;
    int y0 = 0;
    int dir;
    int I;
    int[] imgIndex;
    double[] disMatrix;
    int[] img;
    int[] DminImg;
    int[][] boundaryXY;

    public DminClass() {
    }

    public class dInfoClass {

        private pixels8Class[] pixels8s;

        public dInfoClass() {
            try {
                pixels8s = new pixels8Class[8];
                for (int i = 0; i < 8; i++) {
                    pixels8s[i] = new pixels8Class();
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        public void setPixels8s(int x, int y) {
            pixels8s[0].x = x + 1;
            pixels8s[0].y = y;
            int index0 = y * w + (x + 1);
            pixels8s[0].inputValue = img[index0];

            pixels8s[1].x = x + 1;
            pixels8s[1].y = y - 1;
            int index1 = (y - 1) * w + (x + 1);
            pixels8s[1].inputValue = img[index1];

            pixels8s[2].x = x;
            pixels8s[2].y = y - 1;
            int index2 = (y - 1) * w + x;
            pixels8s[2].inputValue = img[index2];

            pixels8s[3].x = x - 1;
            pixels8s[3].y = y - 1;
            int index3 = (y - 1) * w + x - 1;
            pixels8s[3].inputValue = img[index3];

            pixels8s[4].x = x - 1;
            pixels8s[4].y = y;
            int index4 = y * w + x - 1;
            pixels8s[4].inputValue = img[index4];

            pixels8s[5].x = x - 1;
            pixels8s[5].y = y + 1;
            int index5 = (y + 1) * w + x - 1;
            pixels8s[5].inputValue = img[index5];

            pixels8s[6].x = x;
            pixels8s[6].y = y + 1;
            int index6 = (y + 1) * w + x;
            pixels8s[6].inputValue = img[index6];

            pixels8s[7].x = x + 1;
            pixels8s[7].y = y + 1;
            int index7 = (y + 1) * w + x + 1;
            pixels8s[7].inputValue = img[index7];
        }

        private pixels8Class[] getPixels8s() {
            return pixels8s;
        }
    }

    public class pixels8Class {
        private int inputValue;
        private int x;
        private int y;

        public pixels8Class() {
            inputValue = 2;
            x = 0;
            y = 0;
        }
    }

    //提取二值图像inputs的内边界
    public List<Integer> getEdge(int width, int height, int[] inputs) {
        w = width;
        h = height;
        List<Integer> edgeList=new LinkedList<>();
        img = new int[w * h];
        int[] BWimg = new int[w * h];
        Arrays.fill(BWimg, Color.WHITE);
        for (int index = 0; index < w * h; index++) {
            if (inputs[index] == Color.BLACK) img[index] = 1;
            else img[index] = 0;
        }

        boolean bl = FindNewx0y0();
        if (bl) {
            FindBoundary();
            //将该层边界点删除，设为背景点，即边界点img置0
        }
        for (int index : imgIndex) {
            if (index != 0) {
                edgeList.add(index);
            }
        }
        return edgeList;
    }

    public double[] getDmin(int width, int height, int[] inputs, int[] framePixels) {
        w = width;
        h = height;
        int Max=w*w+h*h;
        disMatrix = new double[w * h];
        img = new int[w * h];
        try {
            for (int index = 0; index < w * h; index++) {
                if (inputs[index] == Color.BLACK) img[index] = 1;
                else img[index] = 0;
            }

            boolean bl = FindNewx0y0();
            if (bl) {
                FindBoundary();
            }
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int index = y * w + x;
                    if (framePixels[index] == Color.BLACK) {
                        double dmin = Max;
                        for (int i = 0; i < I; i++) {
                            double dis = Math.pow((x - boundaryXY[0][i]), 2.0) + Math.pow((y - boundaryXY[1][i]), 2.0);
                            if (dis < dmin) dmin = dis;
                        }
                        disMatrix[index] = Math.sqrt(dmin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return disMatrix;
    }

    public int[] getDminImg(int width, int height, int[] inputs, int[] framePixels) {
        DminImg = new int[width * height];
        disMatrix = this.getDmin(width, height, inputs, framePixels);
        //归一化所有点的距离变换值，作为其灰度值，由此获得距离变换图像
        double DMax = 0;
        for (int i = 0; i < w * h; i++) {
            if (disMatrix[i] > DMax) DMax = disMatrix[i];
        }
        for (int i = 0; i < w * h; i++) {
            int grayRelative = (int) (255 * disMatrix[i] / DMax);//归一化
            DminImg[i] = 255 << 24 | grayRelative << 16 | grayRelative << 8 | grayRelative;
        }
        return DminImg;
    }

    private boolean FindNewx0y0() {
        dir = 7;
        int round = 0;
        boolean bool = false;
        for (int y = y0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (img[y * w + x] == 1) {
                    x0 = x;
                    y0 = y;
                    bool = true;
                    round++;
                    break;
                }
            }
            if (round == 1) break;
        }
        return bool;
    }

    private void FindBoundary() {
        try {
            //the first point index=y0*w+x0;
            int currentIndex = -1;
            int lastSecondIndex = -1;
            int newX = x0, newY = y0;
            imgIndex = new int[(w + h) * 2];
            boundaryXY = new int[2][(w + h) * 2];
            I = 0;
            //find boundary points,当回到起点的时候while结束
            while (currentIndex != imgIndex[1] || lastSecondIndex != imgIndex[0]) {
                //更新dir,该段程序位置不能变
                if (dir % 2 == 0) dir = (dir + 7) % 8;
                else dir = (dir + 6) % 8;
                int roundNum = 0;
                int newindex = newY * w + newX;
                //set and get newindex's 8 neighbor structure Pixels8s
                dInfoClass dInfo=new dInfoClass();
                dInfo.setPixels8s(newX, newY);
                pixels8Class[] Pixels8s = dInfo.getPixels8s();
                //round to find the next boundary point
                while (Pixels8s[dir].inputValue != 1 && roundNum <= 7) {
                    dir++;
                    if (dir == 8) dir = 0;
                    roundNum++;
                }
                //not find the next boundary point,exit while to FindNewx0y0
                if (Pixels8s[dir].inputValue == 0 && roundNum == 8) {
                    imgIndex[I] = newindex;
                    break;
                }
                //find the next boundary point,get newindex's distance information
                imgIndex[I] = newindex;
                lastSecondIndex = newindex;
                //update newindex，set as the next boundary point
                newX = Pixels8s[dir].x;
                newY = Pixels8s[dir].y;
                boundaryXY[0][I] = newX;
                boundaryXY[1][I] = newY;
                currentIndex = newY * w + newX;
                I++;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

public double getEdgelength(int width, int height, int[] inputs){
    boolean[] visited = new boolean[width * height];
    int[] edgeInputs=new int[width*height];
    double edgeLenth=0;
    double sqrt2 = Math.sqrt(2);
    Arrays.fill(edgeInputs, Color.WHITE);
    List<Integer> list=getEdge(width, height, inputs);
    for (int index:list){
        edgeInputs[index]=Color.BLACK;
    }
    for (int y = 1; y < height - 1; y++) {
        for (int x = 1; x < width - 1; x++) {
            int index = y * width + x;
            if (!visited[index] && edgeInputs[index] == Color.BLACK) {
                int[] a = new int[9];
                System.arraycopy(edgeInputs, index - width - 1, a, 0, 3);
                System.arraycopy(edgeInputs, index - 1, a, 3, 3);
                System.arraycopy(edgeInputs, index + width - 1, a, 6, 3);
                for (int i = 0; i < 9; i++) {
                    if (i == 4) continue;
                    if (a[i] == Color.BLACK && i % 2 == 1) edgeLenth+= 1;
                    if (a[i] == Color.BLACK && i % 2 == 0) edgeLenth += sqrt2;
                }
                visited[index] = true;
            }
        }
    }
    return edgeLenth;
}

}
