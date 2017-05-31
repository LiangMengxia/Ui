package com.example.lenovo.ui.imageProcessing;

import android.graphics.Color;

/**
 * Created by lenovo on 2016/11/8.
 */
public class Framework {
    int width;
    int height;
    int Fpn;
    int[] pixels;
    int[] treeValue;
    GRBTree[] grbTree;
    int currentTree;
    TList[] L;
    int[] InfluencedPnt;
    int lutTreeNum[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 1, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0, 0, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 1, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0
    };

    int lutTreeIdx1[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 3, 3, 0, 0, 3, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 5, 5, 0, 0, 5, 2, 0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 3,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            7, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            7, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 5, 5, 6, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            7, 0, 0, 0, 6, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            4, 0, 0, 0, 7, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0
    };

    int lutTreeIdx2[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public Framework() {

    }

    //TList建立一个矩阵，矩阵元素包含TreeIndex，Visited两个字段；
    // TreeIndex记录待移除点对应的红黑树序号（0~7），TreeIndex为-1时，该点为非移除点；Visited标记点是否被访问过了
    public class TList {
        int TreeIndex;
        int Visited;

        public TList() {
            this.TreeIndex = -1;
            this.Visited = 0;
        }
    }

    //提取骨架
    public int[] getFramework(int w, int h, int[] inputs) {
        width = w;
        height = h;
        pixels = new int[width * height];
        //黑色为1，白色为0
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (inputs[y * width + x] == Color.BLACK)
                    pixels[y * width + x] = 1;
                else pixels[y * width + x] = 0;
            }
        }
        //新建8个红黑树对象
        grbTree = new GRBTree[8];
        for (int i = 0; i <= 7; i++) {
            grbTree[i] = new GRBTree();
        }
        currentTree = 0;

        L = new TList[width * height];
        for (int i = 0; i < width * height; i++) {
            L[i] = new TList();
        }
        //第一次遍历二值化图像
        FirstMatchTree();

        //当所有的树里面不再插入结点的时候，说明骨架已经生成，即n0~n7都为0
        while (grbTree[0].root != null || grbTree[1].root != null || grbTree[2].root != null || grbTree[3].root != null ||
                grbTree[4].root != null || grbTree[5].root != null || grbTree[6].root != null || grbTree[7].root != null) {
            for (int round = 0; round <= 7; round++) {
                treeValue = new int[width * height];
                //前序遍历树currentTree的移除点索引值，将它们先存放在数组treeValue里面
                treeValue = grbTree[currentTree].preOrder(width, height);
                //存放在树currentTree里面的移除点总数
                int valueCount = grbTree[currentTree].getiCount();

                //再清空树currentTree里面的数据,root置为null
                grbTree[currentTree].Free();

                currentTree++;
                if (currentTree == 8) currentTree = 0;

                //更新pixels,L
                for (int i1 = 0; i1 < valueCount; i1++) {
                    int pntIndex = treeValue[i1];
                    pixels[pntIndex] = 0;
                    L[pntIndex].TreeIndex = -1;//TreeIndex为-1时，该点为非移除点
                    L[pntIndex].Visited = -1;//Visited为-1时，表明该点已成为非待移除点
                }
                //遍历数组treeValue，InfluencedPnt记录受影响的领域点坐标，Fpn为受影响点的个数
                InfluencedPnt = new int[width * height];
                Fpn = 0;
                for (int i1 = 0; i1 < valueCount; i1++) {
                    int pntIndex = treeValue[i1];
                    //查找移除点的8领域点
                    for (int i2 = -1; i2 <= 1; i2++) {
                        for (int i3 = -1; i3 <= 1; i3++) {
                            if (i2 == 0 && i3 == 0) continue;
                            //8个领域点的下标索引值index
                            int index = pntIndex + i2 * width + i3;
                            if (index < 0) index = 0;
                            if (index > width * height - 1)
                                index = width * height - 1;
                            //该领域点为白点直接退出
                            if (pixels[index] == 0) continue;
                            //Visite为1时，表明该领域点已被访问过，直接退出；
                            if (L[index].Visited == 1) continue;
                            else {
                                L[index].Visited = 1;
                                InfluencedPnt[Fpn] = index;
                                Fpn++;
                            }
                            //更新树T0到T7，只要检测到8领域点是待移除点的话，首先将它从原来的树里面删除；不是的话，不操作；
                            // 之后再根据该领域点的3*3结构（可能发生变化）来判断它是否是待移除点，以及它该插入到哪棵红黑树里面
                            int treeindex = L[index].TreeIndex;
                            if (treeindex >= 0)
                                grbTree[treeindex].Delete(index);
                            //获取领域点index的3*3结构
                            int[] b = new int[9];
                            for (int i4 = -1; i4 <= 1; i4++) {
                                for (int i5 = -1; i5 <= 1; i5++) {
                                    int index8 = index + i5 * width + i4;
                                    if (index8 < 0) index = 0;
                                    if (index8 > width * height - 1)
                                        index8 = width * height - 1;
                                    b[(i4 + 1) * 3 + (i5 + 1)] = pixels[index8];
                                }
                            }
                            //计算领域点index对应的lutTreeNum的下标值lutValue
                            int lutValue = 0;
                            for (int i = 0; i <= 8; i++) {
                                if (b[i] == 1)
                                    lutValue += Math.pow(2, i);
                            }
                            //将待移除的领域点index插入对应的红黑树k里面，并将k值记录到L中
                            int k = Put(index, lutValue);
                            if (k >= 0) L[index].TreeIndex = k;
                            else L[index].TreeIndex = -1;
                        }
                    }
                }

                for (int i = 0; i < Fpn; i++) {
                    int f = InfluencedPnt[i];
                    L[f].Visited = 0;
                }

            }
        }
        //while循环结束，再也没有移除点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y * width + x] == 1) inputs[y * width + x] = Color.BLACK;
                else inputs[y * width + x] = Color.WHITE;
            }
        }
        return inputs;
    }

    private void FirstMatchTree() {
        //第一次遍历二值化图像，currentTree=0,把待移除点插入对应的红黑树里面
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pntIndex = y * width + x;
                //是白色点的话直接退出本次循环，检测下一个点
                if (pixels[pntIndex] == 0) continue;
                //检测像素点的3*3领域结构，组成a[8]a[7]...a[1]a[0]的九位二进制数
                int[] a = new int[9];
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        a[(i + 1) * 3 + (j + 1)] = pixels[(y + j) * width + (x + i)];
                    }
                }
                //通过上面的九位二进制数，计算出该像素点对应于lutTreeNum的下标值
                int lutValue = 0;
                for (int i = 0; i <= 8; i++) {
                    if (a[i] == 1)
                        lutValue += Math.pow(2, i);
                }
                //k为树的序号数，pntIndex为待移除点的话，k的范围为0到7；不是待移除点的话，k为-1
                int k = Put(pntIndex, lutValue);
                if (k >= 0) L[pntIndex].TreeIndex = k;
                else L[pntIndex].TreeIndex = -1;
            }
        }
    }

    private int GetTreeIndex(int lutValue) {
        if (lutTreeNum[lutValue] == 0)
            return -1;

        if (lutTreeNum[lutValue] == 1)
            return lutTreeIdx1[lutValue];

        if (currentTree > lutTreeIdx1[lutValue] && currentTree <= lutTreeIdx2[lutValue])
            return lutTreeIdx2[lutValue];

        return lutTreeIdx1[lutValue];
    }

    private int Put(int pntIndex, int lutValue) {
        int ti = GetTreeIndex(lutValue);

        if (ti < 0)
            return -1;

        grbTree[ti].Insert(pntIndex);

        return ti;
    }

    private void Delete(int pntIndex, int lutValue) {
        int ti = GetTreeIndex(lutValue);

        if (ti < 0 || ti == currentTree)
            return;

        grbTree[ti].Delete(pntIndex);
    }


}
