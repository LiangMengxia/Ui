package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2016/11/30.
 */
public class ConnectedClass {
    private final double final4=Math.pow(2, 4);
    private final double final3=Math.pow(2, 3);
    private final double final2=Math.pow(2, 2);
    private final double final1=Math.pow(2, 1);
    private final double final0=Math.pow(2, 0);
    private int w;
    private int h;
    private int newM = 1;
    private int m0, m1, m2, m3;
    private int index0, index1, index2, index3;
    private int[] pixels;
    private List<UnitClass> unit;
    private List<List<Integer>> ListM;
    private boolean[] visited;
    //查找表，将所有的点分为3类
    private int[] caseNum = {
            0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};

    public ConnectedClass() {

    }

    public static class UnitClass {
        int m;

        public UnitClass() {
            m = 0;
        }
    }

    //提取二值图像中所有连通域
    public List<List<Integer>> FindAllConnectedArea(int width, int height, int[] inputs) {
        try {
            w = width;
            h = height;
            pixels = new int[w * h];
            unit = new ArrayList<>(w * h);
            ListM = new LinkedList<>();
            visited = new boolean[w * h];
            List<Integer> list0 = new LinkedList<>();
            list0.add(0);
            ListM.add(list0);//为保证ListM里面的元素下标表示的就是该元素列表标号（即1到maxM），ListM必须从0开始add元素列表

            for (int index = 0; index < w * h; index++) {
                UnitClass unitClass = new UnitClass();
                unit.add(index, unitClass);
            }
            for (int index = 0; index < w * h; index++) {
                if (inputs[index] == Color.BLACK) pixels[index] = 1;
                else pixels[index] = 0;
            }
            //正向扫描给定所有点一个临时标号
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    if (pixels[y*w+x]==1)
                    visitNeibor(x, y);
                }
            }
            //移除ListM里面不是连通区域的list，仅留下连通域list
            List<List<Integer>> delist=new LinkedList<>();
            ListM.remove(0);
            for (List<Integer> list : ListM) {
                if (list.size() == 0)
                    delist.add(list);
            }
            ListM.removeAll(delist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListM;
    }

    //提取二值图像中最大连通域的二值图像
    public int[] FindMaxConnectedArea(int width, int height, int[] inputs) {
        try {
            w = width;
            h = height;
            pixels = new int[w * h];
            unit = new ArrayList<>(w * h);
            ListM = new LinkedList<>();
            visited = new boolean[w * h];
            List<Integer> list0 = new LinkedList<>();
            list0.add(0);
            ListM.add(list0);//为保证ListM里面的元素下标表示的就是该元素列表标号（即1到maxM），ListM必须从0开始add元素列表

            for (int index = 0; index < w * h; index++) {
                UnitClass unitClass = new UnitClass();
                unit.add(index, unitClass);
            }
            for (int index = 0; index < w * h; index++) {
                if (inputs[index] == Color.BLACK) pixels[index] = 1;
                else pixels[index] = 0;
            }
            //正向扫描给定所有点一个临时标号
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    visitNeibor(x, y);
                }
            }
            //将最大连通域对应的List里面的所有目标像素点用红色点标记
            List<Integer> LongestList = FindLongestList();

            Arrays.fill(pixels, Color.WHITE);//该语句不要挪动到前面，否则出错
            for (int index : LongestList) {
                pixels[index] = Color.BLACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pixels;//(width+2)*(height+2)
    }

    private void neibor(List<Integer> mValue) {
        m0 = unit.get(index0).m;
        mValue.add(0, m0);
        m1 = unit.get(index1).m;
        mValue.add(1, m1);
        m2 = unit.get(index2).m;
        mValue.add(2, m2);
        m3 = unit.get(index3).m;
        mValue.add(3, m3);
    }

    private void visitNeibor(int x, int y) {
        try {
            List<Integer> mValue = new LinkedList<>();
            int index = y * w + x;
            index0 = index - w - 1;//(y - 1) * w + x - 1;
            index1 = index - w;// (y - 1) * w + x;
            index2 = index - w + 1;//(y - 1) * w + x + 1;
            index3 = index - 1;//y * w + x - 1;
            int caseIndex = (int) (pixels[index] *final4  + pixels[index3] * final3 + pixels[index2] * final2
                    + pixels[index1] * final1 + pixels[index0] * final0);
            int caseNumber = caseNum[caseIndex];
            switch (caseNumber) {
                case 0:
                    break;
                //case1 current pixel 背景点，领域中有目标点
                case 1:
                    neibor(mValue);
                    int min3 = FindMinM(mValue);
                    if (pixels[index0] != 0 && m0 > min3 && !visited[m0]) {
                        UpDateList(m0, min3);
                        visited[m0] = true;
                    }
                    if (pixels[index1] != 0 && m1 > min3 && !visited[m1]) {
                        UpDateList(m1, min3);
                        visited[m1] = true;
                    }
                    if (pixels[index2] != 0 && m2 > min3 && !visited[m2]) {
                        UpDateList(m2, min3);
                        visited[m2] = true;
                    }
                    if (pixels[index3] != 0 && m3 > min3 && !visited[m3]) {
                        UpDateList(m3, min3);
                        visited[m3] = true;
                    }
                    visited[m0] = false;
                    visited[m1] = false;
                    visited[m2] = false;
                    visited[m3] = false;
                    break;
                //case2 current pixel 目标点，领域中无目标点
                case 2:
                    //给孤立点或者某个连通域的第一个点index一个新的标号
                    unit.get(index).m = newM;
                    //新建list，将index加入到这个list里面去
                    List<Integer> list = new LinkedList<>();
                    list.add(index);
                    //把这个list放到ListM对应标号的位置
                    ListM.add(newM, list);//在没add 0元素的时候，是不能add 其他指定位置的元素的
                    //更新标号值
                    newM++;
                    break;
                //case3 current pixel 目标点，领域中有目标点
                case 3:
                    neibor(mValue);
                    mValue.add(4, unit.get(index).m);
                    int min = FindMinM(mValue);
                    //将index的标号置为min，并把index加入到标号为min的list里面
                    unit.get(index).m = min;
                    ListM.get(min).add(index);
                    //更新邻域点标号，及其所在list
                    if (pixels[index0] != 0 && m0 > min && !visited[m0]) {
                        UpDateList(m0, min);
                        visited[m0] = true;
                    }
                    if (pixels[index1] != 0 && m1 > min && !visited[m1]) {
                        UpDateList(m1, min);
                        visited[m1] = true;
                    }
                    if (pixels[index2] != 0 && m2 > min && !visited[m2]) {
                        UpDateList(m2, min);
                        visited[m2] = true;
                    }
                    if (pixels[index3] != 0 && m3 > min && !visited[m3]) {
                        UpDateList(m3, min);
                        visited[m3] = true;
                    }
                    visited[m0] = false;
                    visited[m1] = false;
                    visited[m2] = false;
                    visited[m3] = false;
                    break;
            }
            mValue.clear();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }

    private void UpDateList(int m, int min) {
        try {
            List<Integer> listm = ListM.get(m);
            List<Integer> listmin = ListM.get(min);
            //把标号m的listm里面的像素点全部转移到最小标号min的listmin里面
            for (int index : listm) {
                unit.get(index).m = min;//标号m列表里面的像素点的m置换为最小标号min
                listmin.add(index);//转移到标号min所在的list
            }
            listm.clear();//然后清空listm里面的对应m标号的像素点,size=0
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private List<Integer> FindLongestList() {
        List<Integer> LongestList = ListM.get(1);
        for (List<Integer> list : ListM) {
            if (list.size() > LongestList.size()) {
                LongestList = list;
            }
        }
        return LongestList;
    }

    private int FindMinM(List<Integer> mValue) {
        try {
            //从mValue中删除标号为0的点
            for (int i = 0; i < mValue.size(); i++) {
                if (mValue.get(i) == 0) {
                    mValue.remove(i);
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            e.getLocalizedMessage();
        }

        int minM = mValue.get(0);
        int size = mValue.size();
        try {
            if (size > 0) {
                for (int m : mValue) {
                    if (m < minM) minM = m;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            e.getLocalizedMessage();
        }
        return minM;
    }


}

