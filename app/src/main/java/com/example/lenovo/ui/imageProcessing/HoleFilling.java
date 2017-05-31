package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/4.
 */
public class HoleFilling {
    int w, h;
    int[] BWimg;
    List<List<Integer>> AllConnectList;
    public  int[][] STRUCTURE_ELEMENT = new int[][]{
            {0, 1, 0}, {1, 1, 1}, {0, 1, 0}
    };
    public HoleFilling() {
    }

     //基于边界的孔洞填充
    public int[] holeFill(int width, int height, List<Integer> edgeList) {
        w = width;
        h = height;
        BWimg = new int[w * h];
        int[] bmpInput = new int[w * h];//边界
        int[] anotherInput=new int[w * h];//边界膨胀
        List<Integer> outerList = new LinkedList<>();
        Arrays.fill(BWimg, Color.WHITE);
        Arrays.fill(bmpInput, Color.WHITE);
        //原图白点部分（孔洞和原图外围矩形框）置为黑色，构成新的二值图像BWimg
        for (int index : edgeList) {
            bmpInput[index] = Color.BLACK;
        }
        //为边界添加内外轮廓,即膨胀,得到新的边界E1
        DilationFilter.setStructureElements(STRUCTURE_ELEMENT);
        anotherInput= DilationFilter.filter(w, h, bmpInput);
        //以E1为边界，把图像分为两个连通域
        for (int y =1; y < h-1; y++) {
            for (int x = 1; x < w-1; x++) {
                    int index = y * w + x;
                if (anotherInput[index] == Color.WHITE)
                    BWimg[index] = Color.BLACK;
                else BWimg[index] = Color.WHITE;
            }
        }
        //再去掉E1的外轮廓
        DminClass dminClass=new DminClass();
        List<Integer> list=dminClass.getEdge(w,h,anotherInput);
        for (int index:list){
            anotherInput[index]=Color.WHITE;
        }
        //原图最外圈白点集合outerList
        for (int y =1; y < h-1; y++) {
            for (int x = 1; x < w-1; x++) {
                if (y == 1 || y == h - 2 || x == 1 || x == w - 2) {
                    int index = y * w + x;
                    outerList.add(index);
                }
            }
        }
        //提取T1中的所有连通域
        ConnectedClass connectedClass = new ConnectedClass();
        AllConnectList = connectedClass.FindAllConnectedArea(w, h, BWimg);
        //检测每一个连通域中点是否含有最外圈点，是的话，该连通域所有的点全部置为白点。最终得到的黑色区域为原图孔洞填充的结果
        boolean bl=false;
        for (List<Integer> connectList : AllConnectList) {
            for (int index : connectList) {
                if (outerList.contains(index)) {
                    upDateBWimg(connectList);
                    bl=true;
                    break;
                }
            }
            if (bl) break;
        }
        //获取边界bmpInput孔洞填充后的结果
        upDatebmpInput(anotherInput);
        return anotherInput;
    }

    //基于某二值图像的孔洞填充
    public int[] holeFill(int width, int height, int[] bmpInput) {
        w = width;
        h = height;
        BWimg = new int[w * h];
        List<Integer> outerList = new LinkedList<>();
        Arrays.fill(BWimg, Color.WHITE);
        //原图白点部分（孔洞和原图外围矩形框）置为黑色，构成新的二值图像BWimg
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int index = y * w + x;
                if (bmpInput[index] == Color.WHITE)
                    BWimg[index] = Color.BLACK;
            }
        }
        //原图最外圈白点集合outerList
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (y == 0 || y == h - 1 || x == 0 || x == w - 1) {
                    int index = y * w + x;
                    outerList.add(index);
                }
            }
        }
        //提取T1中的所有连通域
        ConnectedClass connectedClass = new ConnectedClass();
        AllConnectList = connectedClass.FindAllConnectedArea(w, h, BWimg);
        //检测每一个连通域中点是否含有最外圈点，是的话，该连通域所有的点全部置为白点。最终得到的黑色区域为原图孔洞填充的结果
        for (List<Integer> connectList : AllConnectList) {
            for (int index : connectList) {
                if (outerList.contains(index)) {
                    upDateBWimg(connectList);
                    break;
                }
            }
        }
        //获取原二值图像bmpInput孔洞填充后的结果
        upDatebmpInput(bmpInput);
        return bmpInput;

    }

    public void upDateBWimg(List<Integer> connectList) {
        for (int index : connectList) {
            BWimg[index] = Color.WHITE;
        }
    }

    public void upDatebmpInput(int[] bmpInput) {
        for (int index = 0; index < w * h; index++) {
            if (BWimg[index] == Color.BLACK) bmpInput[index] = Color.BLACK;
        }
    }
}
