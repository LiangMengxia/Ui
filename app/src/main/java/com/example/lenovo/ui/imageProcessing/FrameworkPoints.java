package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/12.
 */
public class FrameworkPoints {
    int w;
    int h;
    double pi = Math.PI;
    List<Integer> expansionList;//记录需要进行骨架拓展的点
    List<LinkedClass> TotalLinkedClass;//记录每一个分支的连接单元
    List<List<Integer>> L;//所有分支
    List<List<Integer>> L2;//终端分支

    int[] img;
    int[] mNum;//记录每个骨架点的m领接点个数
    boolean[] pntVisited;//标记已访问的点

    public FrameworkPoints() {
    }

    //标记与当前单元相连的单元集合类
    public class LinkedClass {
        int mark;//标记值，主要是用于寻找交叉点的连接单元
        List<Integer> currentList;//当前list
        List<List<Integer>> LinkedElements;//与当前list相连的其他单元

        public LinkedClass() {
            mark = 0;
            currentList=new LinkedList<>();
            LinkedElements = new LinkedList<>();
        }

        public void addLinkedElement(List<Integer> list) {
            LinkedElements.add(list);
        }
    }

    //按m邻接点定义把骨架点分为三类点：端点、普通点、交叉点
    public void classifyPoints(int width, int height, int[] framePixels) {
        w = width;
        h = height;
        img = new int[w * h];
        mNum = new int[w * h];//记录骨架点的m邻接点的个数
        try {
            for (int index = 0; index < w * h; index++) {
                if (framePixels[index] == Color.BLACK) img[index] = 1;
            }
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    int index = y * w + x;
                    if (framePixels[index] == Color.WHITE) continue;
                    int count = 0;
                    int[] a = new int[9];
                    System.arraycopy(img, index - w - 1, a, 0, 3);
                    System.arraycopy(img, index - 1, a, 3, 3);
                    System.arraycopy(img, index + w - 1, a, 6, 3);
                    for (int i = 1; i <= 7; i = i + 2) {
                        if (a[i] == 1) count++;
                    }
                    if (a[0] == 1 && a[1] == 0 && a[3] == 0) count++;
                    if (a[2] == 1 && a[1] == 0 && a[5] == 0) count++;
                    if (a[6] == 1 && a[3] == 0 && a[7] == 0) count++;
                    if (a[8] == 1 && a[5] == 0 && a[7] == 0) count++;
                    mNum[index] = count;
/*                    if (count == 1) framePixels[index] = Color.RED;//端点
                    if (count == 2) framePixels[index] = Color.GREEN;//普通点，count>2的为交叉点*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //提取起点SpIndex所在的分支，作为树的根节点，建立骨架的树结构,最终返回骨架路径
    public List<List<Integer>> findTreeRode(int SpIndex, List<List<Integer>> AllList) {
        int[] output = new int[w * h];
        Arrays.fill(output, Color.WHITE);
        List<Integer> startList = new LinkedList<>();
        List<List<Integer>> rodeList=new LinkedList<>();
        for (List<Integer> list : AllList) {
            for (int index : list) {
                if (SpIndex == index) {
                    startList = list;
                }
            }
        }
        try {
            //建立骨架的树结构
            FrameworkTree tree = new FrameworkTree(startList);
            tree.setUpTree(startList,AllList, TotalLinkedClass);
            //树的路径提取：所有的终端分支都是作为树的叶子结点，以叶子结点为起点，不断寻找他们的父结点，直到根结点结束
            for (List<Integer> endBranch : L2) {
               List<Integer> rode = tree.getTreeRode(endBranch);
                rodeList.add(rode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rodeList;
    }

    //得到所有的骨架分支
    public List<List<Integer>> getAllBranches(int width, int height, int[] framePixels) {
        pntVisited = new boolean[w * h];
        L = new LinkedList<>();
        L2 = new LinkedList<>();
        TotalLinkedClass = new LinkedList<>();
        List<List<Integer>> L3 = new LinkedList<>();//L3内部分支列表

        this.classifyPoints(width, height, framePixels);//骨架点分类,对延拓后的新的骨架点分类
        //一：交叉点部分
        try {
            for (int index = 0; index < w * h; index++) {
                if (mNum[index] > 2) {
                    //标记交叉点已访问
                    pntVisited[index] = true;
                    List<Integer> list = new LinkedList<>();
                    list.add(index);
                    L.add(list);
                    //交叉点下标作为交叉点标号
                    LinkedClass linkedClass = new LinkedClass();
                    linkedClass.mark = index;
                    linkedClass.currentList = list;
                    TotalLinkedClass.add(linkedClass);
                }
            }

            //二：终端分支部分
            L2 = this.getMarkEndBranches(width, height, framePixels);//提取并标记终端分支上的点已访问
            for (List<Integer> endBranch : L2) {
                L.add(endBranch);
            }

            //三：内部分支部分,内部分支放到L3列表
            //当终端分支和交叉点都被pntVisited标记的时候，剩余骨架部分就是内部分支了
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    int index = y * w + x;
                    if (!pntVisited[index] && mNum[index] == 2) {//从m邻域至少有一个交叉点的普通点开始，找与跟它相连的普通点构成的骨架分支
                        List<Integer> branchList = new LinkedList<>();
                        innermNeibor(x, y, branchList);
                        L3.add(branchList);
                    }
                }
            }
/*            //标记内部分支
            for (List<Integer> innerBranch : L3) {
                markInnerBranch(innerBranch);//寻找与每一个内部分支相连接的两个交叉点，并标记
            }*/
            for (List<Integer> innerBranch : L3) {
                L.add(innerBranch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return L;
    }

    //得到骨架终端分支,并标记与该分支相连的交叉点
    public List<List<Integer>> getMarkEndBranches(int width, int height, int[] framePixels) {
        List<List<Integer>> L = new LinkedList<>();
        //  this.classifyPoints(width, height, framePixels);//骨架点分类
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int index = y * w + x;
                if (!pntVisited[index] && mNum[index] == 1) {//从端点位置开始，找与跟它相连的普通点构成的骨架分支
                    List<Integer> branchList = new LinkedList<>();
                    endmNeibor(x, y, branchList);
                    L.add(branchList);
                }
            }
        }
        return L;
    }

    //标记内部分支
    //提取与内部分支相连的交叉点，先膨胀一个像素点，再检测膨胀后的单元存在的交叉点，内部分支一般是存在两个相连的交叉点的
    public void markInnerBranch(List<Integer> innerBranch) {
        LinkedClass innerlinkedClass = new LinkedClass();//标记内部分支
        int[] input = new int[w * h];
        Arrays.fill(input, Color.WHITE);
        for (int index : innerBranch) {
            input[index] = Color.BLACK;
        }
        DilationFilter dilationFilter = new DilationFilter();
        input = dilationFilter.filter(w, h, input);
        for (int index = 0; index < w * h; index++) {
            if (input[index] == Color.BLACK && mNum[index] > 2) {//膨胀部分的黑点，且为交叉点
                //标记内部分支相连单元
                List<Integer> list = new LinkedList<>();
                list.add(index);
                innerlinkedClass.addLinkedElement(list);
                //标记并更新某一交叉点的连接单元
                for (LinkedClass linkedClass : TotalLinkedClass) {
                    if (linkedClass.mark == index) {
                        linkedClass.addLinkedElement(innerBranch);
                    }
                }
            }
        }
        innerlinkedClass.currentList = innerBranch;
        TotalLinkedClass.add(innerlinkedClass);

    }

    //骨架分支用红色标记
    public int[] frameBranches(int width, int height, int[] framePixels) {
        List<List<Integer>> L = this.getMarkEndBranches(width, height, framePixels);//获得骨架分支列表
        int[] pntPixels = new int[w * h];
        System.arraycopy(framePixels, 0, pntPixels, 0, framePixels.length);
        for (List<Integer> l : L) {
            for (int index : l) {
                pntPixels[index] = Color.RED;
            }
        }
        return pntPixels;
    }

    //获取终端分支，由m领域性质逐个寻找分支上的所有点；并标记终端分支的相连单元
    private void endmNeibor(int x, int y, List<Integer> endBranch) {
        LinkedClass endBlinkedClass = new LinkedClass();//标记终端分支连接单元
        int newindex = y * w + x;
        int middleindex = y * w + x;
        boolean bl;
        int crossingNum = 0;
        try {
            do {
                bl = false;
                endBranch.add(newindex);
                pntVisited[newindex] = true;//标记已经访问过的骨架点，让查找骨架分支的方向只沿着某一个方向
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int index = newindex + j * w + i;
                        if (mNum[index] > 2) {
                            //统计当前骨架点m邻域有几点交叉点
                            crossingNum++;

                            //一个交叉点作为一个单元list
                            //标记与当前终端分支相连的单元
                            List<Integer> list = new LinkedList<>();
                            list.add(index);
                            endBlinkedClass.addLinkedElement(list);

                            //标记并更新某一交叉点的连接单元
                            for (LinkedClass linkedClass : TotalLinkedClass) {
                                if (linkedClass.mark == index) {
                                    linkedClass.addLinkedElement(endBranch);
                                }
                            }

                        }
                        if (!pntVisited[index] && (mNum[index] == 2 || mNum[index] == 1)) {//沿着一个方向找与端点（x，y）相连接的所有普通点
                            middleindex = index;
                            bl = true;
                        }
                    }
                }
                newindex = middleindex;
            } while (bl && crossingNum < 1);//循环结束条件：当前骨架点newindex的m邻域有至少1个交叉点
            endBlinkedClass.currentList = endBranch;//标记当前list
            TotalLinkedClass.add(endBlinkedClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取内部分支，由m领域性质逐个寻找分支上的所有点;并标记内部分支的相连单元
    private void innermNeibor(int x, int y, List<Integer> innerBranch) {
        LinkedClass innerBlinkedClass = new LinkedClass();//标记内部分支连接单元
        int newindex = y * w + x;
        int middleindex = y * w + x;
        boolean bl;
        int crossingNum = 0;
        do {
            bl = false;
            innerBranch.add(newindex);
            pntVisited[newindex] = true;
            for (int j = -1; j <= 1; j++) {
                for (int i = -1; i <= 1; i++) {
                    int index = newindex + j * w + i;
                    if (mNum[index] > 2) {
                        //crossing==1时检测到的是分支起点端的交叉点，crossing==2时检测到的是分支终点端的交叉点
                        crossingNum++;

                        //一个交叉点作为一个单元list
                        //标记与当前内部分支相连的单元
                        List<Integer> list = new LinkedList<>();
                        list.add(index);
                        innerBlinkedClass.addLinkedElement(list);

                        //标记并更新某一交叉点的连接单元
                        for (LinkedClass linkedClass : TotalLinkedClass) {
                            if (linkedClass.mark == index) {
                                linkedClass.addLinkedElement(innerBranch);
                            }
                        }

                    }
                    if (!pntVisited[index] && mNum[index] == 2) {//沿着一个方向找与交叉点（x，y）相连接的所有普通点
                        middleindex = index;
                        bl = true;
                    }
                }
            }
            newindex = middleindex;
        } while (bl && crossingNum < 2);//内部分支：由两个交叉点之间的普通点组成，条件必须是crossingNum < 2，不能是crossingNum < 1
        innerBlinkedClass.currentList = innerBranch;//标记当前list
        TotalLinkedClass.add(innerBlinkedClass);
    }


    //骨架延拓，让骨架延伸至重构图像边缘；从骨架端点开始延拓
    public int[] expansion(int width, int height, int[] framePixels, int[] refactPixels, double[] disMatrix) {
        w = width;
        h = height;
        expansionList = new LinkedList<>();
        int[] newframePixels = new int[w * h];
        try {
            System.arraycopy(framePixels, 0, newframePixels, 0, framePixels.length);
            this.classifyPoints(width, height, framePixels);//骨架点分类
            for (int y = 1; y < h - 1; y++) {
                for (int x = 1; x < w - 1; x++) {
                    int index = y * w + x;
                    if (mNum[index] == 1 && disMatrix[index] > 0) {     //端点D值等于0的时候，说明该点在重构图像的边缘上，不需要延拓；
                        dealEndPoint(x, y, framePixels, refactPixels);   //当D大于0的话，才需要将端点处的骨架延拓
                    }
                }
            }
            //骨架更新,得到延拓后的骨架,待延拓的点都放在expansionList里面
            for (int index : expansionList) {
                newframePixels[index] = Color.BLACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newframePixels;
    }

    //对端点处的骨架进行延拓，所有延拓产生的新的骨架点都放到expansionList里面
    private void dealEndPoint(int x, int y, int[] framePixels, int[] refactPixels) {
        int newx = 0;
        int newy = 0;
        //找与端点最近的点，构成直线方程yi=k(xi-x)+y，沿该直线方向延拓骨架
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (i == 0 && j == 0) continue;
                int index = (y + j) * w + x + i;
                if (framePixels[index] == Color.BLACK) {
                    newx = x + i;
                    newy = y + j;
                }
            }
        }
        if (x - newx == 0){
            if (y<newy){
                for (int yi=y;yi>0;yi--){
                    int index=yi*w+x;
                    if (index<0||index>w*h-1) index=0;
                    if (refactPixels[index] == Color.BLACK)//骨架延拓至重构区域边缘，重构区域点为白点时说明已超出边缘了
                        expansionList.add(index);
                    else break;
                }
            }else {
                for (int yi=y;yi<h;yi++){
                    int index=yi*w+x;
                    if (index<0||index>w*h-1) index=0;
                    if (refactPixels[index] == Color.BLACK)//骨架延拓至重构区域边缘，重构区域点为白点时说明已超出边缘了
                        expansionList.add(index);
                    else break;
                }
            }
            return;
        }
        // x - newx != 0的时候
        double k = (y - newy) / (x - newx);
        if (x < newx) {
            for (int xi = x - 1; xi > 0; xi--) {
                int yi = (int) (k * (xi - x) + y);
                int index = yi * w + xi;
                if (index<0||index>w*h-1) index=0;
                if (refactPixels[index] == Color.BLACK)//骨架延拓至重构区域边缘，重构区域点为白点时说明已超出边缘了
                    expansionList.add(index);
                else break;
            }
        } else {
            for (int xi = x + 1; xi < w; xi++) {
                int yi = (int) (k * (xi - x) + y);
                int index = yi * w + xi;
                if (index<0||index>w*h-1) index=0;
                if (refactPixels[index] == Color.BLACK)
                    expansionList.add(index);
                else break;
            }
        }
    }


    //对端点处的骨架进行延拓，所有延拓产生的新的骨架点都放到expansionList里面
    private void deal(int x, int y, double r, int[] refactPixels) {
        int[] circle = new int[w * h];
        for (int index = 0; index < w * h; index++) {
            circle[index] = Color.WHITE;
        }
        for (int theta = 0; theta < 360; theta++) {
            int newx = (int) (x + r * Math.cos(theta * pi / 180));
            int newy = (int) (y + r * Math.sin(theta * pi / 180));
            int newindex = newy * w + newx;
            if (newindex < 0) newindex = 0;
            if (newindex > w * h - 1) newindex = w * h - 1;
            circle[newindex] = Color.BLACK;
        }
        //端点处以距离变换值D作圆，获取圆与重构区域的交集上的所有点，并选择交集的中点
        //端点与中点构成的线段就是该端点处的骨架延拓
        List<List<Integer>> PntList = intersection(refactPixels, circle);
        int index = PntList.get(0).size() / 2;
        int middlePntX = PntList.get(0).get(index);
        int middlePntY = PntList.get(1).get(index);
        if (x - middlePntX == 0) return;
        double k = (y - middlePntY) / (x - middlePntX);
        if (x < middlePntX) {
            for (int xx = x + 1; xx <= middlePntX; xx++) {
                int yy = (int) (k * (xx - x) + y);
                int newindex = yy * w + xx;
                expansionList.add(newindex);
            }
        } else {
            for (int xx = middlePntX; xx < x; xx++) {
                int yy = (int) (k * (xx - x) + y);
                int newindex = yy * w + xx;
                expansionList.add(newindex);
            }
        }
    }

    //求两个区域的交集
    private List<List<Integer>> intersection(int[] a, int[] b) {//重构区域相与，a与b，同为黑则黑，异为白
        List<Integer> list0 = new LinkedList<>();
        List<Integer> list1 = new LinkedList<>();
        List<List<Integer>> list = new LinkedList<>();
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int index = y * w + x;
                if (a[index] == Color.BLACK && b[index] == Color.BLACK) {
                    list0.add(x);
                    list1.add(y);
                }
            }
        }
        list.add(list0);
        list.add(list1);
        return list;
    }

}
