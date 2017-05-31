package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by lenovo on 2016/8/26.
 */
public class DilationFilter {

    public final static int[][] DEFAULT_STRUCTURE_ELEMENT = new int[][]{
            {1, 1, 1}, {1, 1, 1}, {1, 1, 1}
    };
    private static int[][] structureElements;

    public DilationFilter() {
        structureElements = DEFAULT_STRUCTURE_ELEMENT;
        System.out.println("Dilation And Erosion Filter...");
    }

    public static int[][] getStructureElements() {
        return structureElements;
    }

    public static void setStructureElements(int[][] structureElements) {
        DilationFilter.structureElements = structureElements;
    }


    public static int[] filter(int width, int height, int[] input) {
        int[] setA;
        int[] output = new int[width * height];
        setA = input;
        int seh = DilationFilter.getStructureElements()[0].length / 2;
        int sew = DilationFilter.getStructureElements()[0].length / 2;
        Arrays.fill(output, Color.WHITE);// 背景设为白色
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (input[y * width + x] == Color.BLACK) {
                    for (int j = -seh; j <= seh; j++) {
                        int newy = y + j;
                        if (newy < 0 || newy >= height)
                            newy = 0;
                        for (int i = -sew; i <= sew; i++) {
                            int newx = x + i;
                            if (newx < 0 || newx >= height)
                                newx = 0;
                            //把图像中像素值为黑色的像素扩张为结构元素的副本，
                            // 即把3*3矩形结构元素中对应位置的元素值幅值给以黑色像素点为中心的3*3矩形区域
                            output[newy * width + newx] = structureElements[i + sew][j + seh];
                        }
                    }
                }
            }
        }
        //值为1的像素点设置为黑色，否则为白色
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (output[y * width + x] == 1)
                    output[y * width + x] = Color.BLACK;
            }
        }
        return output;
    }

    protected static int getPixel(int[] input, int width, int height, int col, int row) {
        if (col < 0 || col >= width)
            col = 0;
        if (row < 0 || row >= height)
            row = 0;
        int index = row * width + col;
        int tr = (input[index] >> 16) & 0x00ff;
        return tr;
    }

}
