package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2017/1/10.
 */
public class FindCentralShaft {
    double sqrtPi = Math.sqrt(2 * Math.PI);
    double T = 5;  //将中轴分割成若干折线的阈值
    int w, h;
    int size;
    int[][] AllPointsXY;
    int[][] MiddlePnt;
    int MiddlePntNum;
    public FindCentralShaft() {
    }

    //确定叶片中轴，首先计算骨架路径左右两边区域的面积比
    public List<Integer> findCentralShaft(int width, int height, int SpIndex, int[] refactPixels, List<List<Integer>> rodeList) {
        w = width;
        h = height;
        int[] partPixels = new int[w * h];
        //整个重构区域的目标像素点总数TotalNum,其中一半的目标像素点总数PartNum,骨架路径目标像素点总数rodeNum
        int TotalNum = 0;
        List<List<Integer>> saveRodeList = new LinkedList<>();//saveRodeList记录面积比在0.9到1.0的路径rodeList

        //提取重构区域的内边界
        DminClass dminClass1 = new DminClass();
        List<Integer> edgeList = dminClass1.getEdge(w, h, refactPixels);
        for (int value : refactPixels) {
            if (value == Color.BLACK) TotalNum++;
        }
        int startIndex = edgeList.indexOf(SpIndex);//骨架路径起点即中轴起点，确定它在内边界edgeList里面的位置

        //对每一条骨架路径list，求分割面积比例
        for (List<Integer> list : rodeList) {
            int PartNum = 0;
            int rodeNum = list.size();
            List<Integer> circleList = new LinkedList<>();
            int endValue = list.get(0);//骨架路径结束点是骨架终端分支的端点，所以它总是在骨架路径list的第一个点，
            // 确定它在内边界edgeList里面的位置endIndex

            int endIndex = edgeList.indexOf(endValue);   //endIndex=-1时，说明骨架路径结束点不在区域边界edgeList上，
            // 那么这个时候就要搜索它的8邻域，其8领域内一定有一个点是内边界上的点
            if (endIndex != -1) {
                circleList = sureEndIndex(startIndex, endIndex, edgeList, list);
            } else {
                boolean bl = false;
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        if (i == 0 && j == 0) continue;
                        int newindex = endValue + j * w + i;
                        if (newindex < 0 || newindex > w * h - 1) newindex = 0;
                        endIndex = edgeList.indexOf(newindex);
                        if (endIndex != -1) {
                            bl = true;
                            break;
                        }
                    }
                    if (bl) break;
                }
                //这里得到的circleList是骨架路径与部分边界围成的连通环，然后填充
                circleList = sureEndIndex(startIndex, endIndex, edgeList, list);
            }
            //填充被骨架路径分割后的叶片区域
            HoleFilling holeFilling = new HoleFilling();
            partPixels = holeFilling.holeFill(w, h, circleList);

            //统计骨架路径分割后，分割部分的目标像素点总数PartNum
            for (int value : partPixels) {
                if (value == Color.BLACK) PartNum++;
            }
            //算左右面积比例
            double proportion = (double) PartNum / (double) (TotalNum - PartNum + rodeNum);
            //比例接近1的骨架路径保留到saveRodeList
            if (Math.abs(proportion - 1.0) <= 0.1)
                saveRodeList.add(list);
        }


        //计算满足等面积原则的骨架路径saveRodeList端点曲率，曲率最大的就是我们最后要找的叶片中轴CentralShaftList
        double maxCurvature = 0;
        List<Integer> CentralShaftList = new LinkedList<>();
        for (List<Integer> list : saveRodeList) {
            int index = list.get(0); //提取骨架端点index
            double curvature = curvature(index);//计算端点曲率curvature
            if (curvature > maxCurvature) {
                maxCurvature = curvature;
                CentralShaftList = list;
            }
        }
/*        Arrays.fill(partPixels, Color.WHITE);
        for (int index:finallyList){
            partPixels[index]=Color.BLUE;
        }*/
        return CentralShaftList;
    }

    //拟合中轴CentralShaft
    public int[][] getCentralShaftLength(List<Integer> CentralShaftList) {
        size = CentralShaftList.size();
        AllPointsXY = new int[size][2];
        List<List<Integer>> MiddlePntXY=new LinkedList<>();
        MiddlePnt=new int[size][2];//记录转折点坐标
        MiddlePntNum=0;//记录转折点个数
        AllPointsXY = getPointsXY(CentralShaftList);
        int x0 = AllPointsXY[0][0];
        int y0 = AllPointsXY[0][1];
        int x1 = AllPointsXY[size - 1][0];
        int y1 = AllPointsXY[size - 1][1];
        double length = Math.sqrt(Math.pow((x0 - x1), 2.0) + Math.pow((y0 - y1), 2.0));
        MiddlePnt[MiddlePntNum][0] = AllPointsXY[0][0];
        MiddlePnt[MiddlePntNum][1] = AllPointsXY[0][1];
        MiddlePntNum++;

        findMiddlePoint(x0, y0, x1, y1, 0, size - 1);

        MiddlePnt[MiddlePntNum][0] = AllPointsXY[size - 1][0];
        MiddlePnt[MiddlePntNum][1] = AllPointsXY[size - 1][1];
        return MiddlePnt;
    }


    private void findMiddlePoint(int x0, int y0, int x1, int y1, int i0, int i1) {
        double k = (double) (y0 - y1) / (double) (x0 - x1);
        double b = (double) (y0 * x1 - y1 * x0) / (double) (x1 - x0);
        double maxD = 0;
        int xMid = 0, yMid = 0, iMiddle = 0;
        for (int i = i0; i <= i1; i++) {
            int x2 = AllPointsXY[i][0];
            int y2 = AllPointsXY[i][1];
            double d = Math.abs(k * x2 - y2 + b) / Math.sqrt(1 + k * k);
            if (d > maxD) {
                maxD = d;
                xMid = x2;
                yMid = y2;
                iMiddle = i;
            }
        }
        if (xMid > 0) {
            MiddlePnt[MiddlePntNum][0] = xMid;
            MiddlePnt[MiddlePntNum][1] = yMid;
            MiddlePntNum++;
        }
        if (maxD >= T) {
            findMiddlePoint(x0, y0, xMid, yMid, i0, iMiddle);
            findMiddlePoint(x1, y1, xMid, yMid, iMiddle, i1);
        }
    }

   //找中轴上的点的坐标
    private int[][] getPointsXY(List<Integer> CentralShaftList){
        int[][] PointsXY=new int[size][2];
        for (int i=0;i<size;i++){
            int index=CentralShaftList.get(i);
            boolean bl=false;
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    if (index == y * w + x) {
                        PointsXY[i][0]=x;
                        PointsXY[i][1]=y;
                        bl = true;
                        break;
                    }
                }
                if (bl) break;
            }
        }
        return PointsXY;
    }

    //计算叶片中轴终点（也为骨架端点）处的曲率
    private double curvature(int index) {
        //确定端点坐标x0，y0
        int x0 = 0, y0 = 0;
        boolean bl=false;
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                if (index == y * w + x) {
                    x0 = x;
                    y0 = y;
                    bl=true;
                    break;
                }
            }
            if (bl)break;
        }
        int w = 3;//w高斯窗口半径，单位标准差sigema=1，w取3倍的sigema
        double gauss1 = 0, gauss2 = 0;
        double g1X = 0, g1Y = 0;
        double g2X = 0, g2Y = 0;

        for (int i = -w; i <= w; i++) {
            int xi = x0 + i;
            int yi = y0 + i;
            gauss1 = getG1(i);//高斯函数一阶导数权值系数
            g1X += gauss1 * xi;//g1X为x0一阶导数值
            g1Y += gauss1 * yi;// g1Y为y0一阶导数值

            gauss2 = getG2(i);//高斯函数一阶导数权值系数
            g2X += gauss2 * xi;//g2X为x0二阶导数值
            g2Y += gauss2 * yi;// g2Y为y0二阶导数值
        }
        //端点曲率
        double k = Math.abs(g1X * g2Y - g2X * g1Y) / Math.pow((g1X * g1X + g1Y * g1Y), 1.5);
        return k;
    }
//单位标准差高斯函数一阶导数
    private double getG1(int x) {
        double G1 = -x * Math.exp(-x * x / 2) / sqrtPi;
        return G1;
    }
    //单位标准差高斯函数二阶导数
    private double getG2(int x) {
        double G2 = (x * x - 1) * Math.exp(-x * x / 2) / sqrtPi;
        return G2;
    }

    //这里得到的circleList是骨架路径与部分边界围成的连通环
    private List<Integer> sureEndIndex(int startIndex, int endIndex, List<Integer> edgeList, List<Integer> list) {
        List<Integer> circleList = new LinkedList<>();
        circleList.addAll(list);
        if (startIndex < endIndex) {
            for (int i = startIndex; i <= endIndex; i++) {
                circleList.add(edgeList.get(i));
            }
        } else {
            for (int i = endIndex; i <= startIndex; i++) {
                circleList.add(edgeList.get(i));
            }
        }
        return circleList;
    }

}
