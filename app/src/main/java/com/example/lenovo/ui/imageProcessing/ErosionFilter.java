package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by lenovo on 2016/8/26.
 */
public class ErosionFilter {
    public static int[][] DEFAULT_STRUCTURE_ELEMENT = new int[][]{
            {1, 1, 1}, {1, 1, 1}, {1, 1, 1}
    };
    private static int[][] structureElements;
    private static int Number;

    public ErosionFilter() {
        structureElements = DEFAULT_STRUCTURE_ELEMENT;
        System.out.println("Erosion Filter...");
    }

    public static int[][] getStructureElements() {
        return structureElements;
    }

    public static void setStructureElements(int[][] structureElements) {
        ErosionFilter.structureElements = structureElements;
    }


    public static int[] filter(int width, int height, int[] input) {
        int[] setA;
        int[] output = new int[width * height];
        setA = input;
        int seh = ErosionFilter.getStructureElements()[0].length / 2;
        int sew = ErosionFilter.getStructureElements()[0].length / 2;
        Arrays.fill(output, Color.WHITE);//背景设为白色
        int number = 0;
        //统计3*3结构元素中有多少个1
        for (int j = 0; j <= 2 * seh; j++) {
            for (int i = 0; i <= 2 * sew; i++) {
                if (structureElements[i][j] == 1) number++;
            }
        }
        Number = number;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int n = 0;
                for (int j = -seh; j <= seh; j++) {
                    int newy = y + j;
                    for (int i = -sew; i <= sew; i++) {
                        int newx = x + i;
                        int g1 = getPixel(setA, width, height, newx, newy);
                        if (g1 < 127) {
                            g1 = 1;
                        }
                        //统计以（x，y）为中心的3*3矩形区域内，结构元素为1的所有位置处，矩形区域对应位置是否也都为1
                        if (g1 == 1 && structureElements[i + sew][j + seh] == 1) n++;
                    }
                }
                //满足上述条件的中心点保留，设为黑色，否则为白色
                if (n >= Number) output[y * width + x] = Color.BLACK;
            }
        }
        return output;
    }

    protected static int getPixel(int[] input, int width, int height, int newx, int newy) {
        if (newx < 0 || newx >= width)
            newx = 0;
        if (newy < 0 || newy >= height)
            newy = 0;
        int index = newy * width + newx;
        int tr = (input[index] & 0x00ff0000 >> 16);
        return tr;
    }

}

