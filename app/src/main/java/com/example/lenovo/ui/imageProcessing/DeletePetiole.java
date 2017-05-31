package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

/**
 * Created by lenovo on 2016/12/14.
 */
public class DeletePetiole {
    private static int w, h;
    private static int[][] STRUCTURE_ELEMENT0 = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private static int[][] STRUCTURE_ELEMENT1 = new int[][]{
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}
    };
    private static int[][] STRUCTURE_ELEMENT2 = new int[][]{
            {1, 1, 1}, {1, 1, 1}, {1, 1, 1}
    };

    public DeletePetiole() {
    }

    //去叶柄得到叶面部分
    public static int[] deletePetiole(int width, int height, int[] bmpInputs) {
        w = width;
        h = height;
        int[] T0 = new int[w * h];//T0原图,T1中间量,T2叶子二值
        int[] T1 = new int[w * h];
        int[] T2 = new int[w * h];
        try {
            System.arraycopy(bmpInputs, 0, T0, 0, bmpInputs.length);
            ErosionFilter.setStructureElements(STRUCTURE_ELEMENT1);
            T1 = ErosionFilter.filter(w, h, bmpInputs);//先腐蚀
            DilationFilter.setStructureElements(STRUCTURE_ELEMENT1);
            T1 = DilationFilter.filter(w, h, T1);//再膨胀
            T2 = minus(T0, T1);//原图减去原图开操作后的图像
            ConnectedClass connectedClass = new ConnectedClass();
            T2 = connectedClass.FindMaxConnectedArea(w, h, T2);//此处是得到叶柄二值部分
            T2 = minus(T0, T2);//此处是得到叶子二值部分
        } catch (Exception e) {
            e.printStackTrace();
        }
        return T2;
    }

    //提取中轴起点，就是叶柄与叶面骨架的交点
    public static int getStartPoint(int width, int height, int[] orgPixels, int[] leafPixels, int[] framePixels) {
        w = width;
        h = height;
        int c = 0;
        int[] petiolePixels = minus(orgPixels, leafPixels);//提取叶柄部分
        DilationFilter.setStructureElements(STRUCTURE_ELEMENT2);
        petiolePixels = DilationFilter.filter(w, h, petiolePixels);//叶柄膨胀
        petiolePixels = and(petiolePixels, framePixels);//提取起点
        for (int index = 0; index < w * h; index++) {
            if (petiolePixels[index] == Color.BLACK) c = index;
        }
        return c;
    }

    private static int[] minus(int[] a, int[] b) {//a减b,同白同黑则为白，异为黑
        int[] c = new int[w * h];
        //前提条件：a包括b
        for (int index = 0; index < w * h; index++) {
            if (a[index] != b[index]) c[index] = Color.BLACK;
            else c[index] = Color.WHITE;
        }
        return c;
    }

    private static int[] and(int[] a, int[] b) {//a与b交集
        int[] c = new int[w * h];
        for (int index = 0; index < w * h; index++) {
            if (a[index] == Color.BLACK && b[index] == Color.BLACK) c[index] = Color.BLACK;
            else c[index] = Color.WHITE;
        }
        return c;
    }

}
