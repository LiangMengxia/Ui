package com.example.lenovo.ui.imageProcessing;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2016/12/23.
 */
public class FrameworkTree {
    TreeNode root;
    List<FrameworkPoints.LinkedClass> AllLinkedClass;
    List<TreeNode> nodeList;

    public FrameworkTree(List<Integer> list) {
        root = new TreeNode(list);
    }

    public TreeNode getRoot() {
        return root;
    }

    public class TreeNode {
        List<Integer> key;
        TreeNode left;
        TreeNode right;
        TreeNode parent;
        boolean inserted;

        public TreeNode(List<Integer> list) {
            key = list;
            left = null;
            right = null;
            parent = null;
            inserted = false;
        }
    }

    //从叶子结点（终端分支）出发，找到树的根结点为止，构成一条骨架路径
    public List<Integer> getTreeRode(List<Integer> endBranch) {
        List<List<Integer>> rode = new LinkedList<>();
        List<Integer> onerode = new LinkedList<>();
        TreeNode leafNode = null;
        //确定叶子结点
        for (TreeNode node : nodeList) {
            if (node.key == endBranch)
                leafNode = node;
        }
        //提取路径
        while (leafNode != null) {
            rode.add(leafNode.key);
            leafNode = leafNode.parent;
        }
        //将一个路径中的多条分支合并为一个list返回,
        // 合并的时候是按终端分支到根结点分支的顺序，这样才能保证路径点的下标是按从上到下从左到右的顺序排列；否则路径点的下标顺序是乱的
        for (List<Integer> list:rode){
            onerode.addAll(list);
        }
        return onerode;
    }

    //从中轴起点所在的终端分支开始，建立骨架的树结构
    public void setUpTree(List<Integer> startList, List<List<Integer>> AllList, List<FrameworkPoints.LinkedClass> TotalLinkedClass) {
        AllLinkedClass = TotalLinkedClass;
        nodeList = new LinkedList<>();
        //首先把所有的骨架分支单元初始化为一个结点，并且标记为未插入
        for (List<Integer> list : AllList) {
            TreeNode node = new TreeNode(list);
            nodeList.add(node);
        }
        TreeNode node = this.root;
        this.insert(node, startList);//插入根结点
    }

    //父结点和父结点list：确定父结点parentlist所有的连接单元，将每一个连接单元作为子结点插入
    public void insert(TreeNode parentNode, List<Integer> parentlist) {
        TreeNode parent=null;
        for (FrameworkPoints.LinkedClass linkedClass : AllLinkedClass) {
            List<Integer> list=linkedClass.currentList;
            if (list .equals(parentlist)  && !parentNode.inserted) {
                List<List<Integer>> linkedList = linkedClass.LinkedElements;
                for (List<Integer> leaflist : linkedList) {
                    //插入下一个结点之前，标记前一个结点已访问
                    parentNode.inserted = true;
                    //在parentNode结点插入相连单元，作为其子结点
                    parent=this.anotherinsert(parentNode, leaflist);
                    if (parent != null)
                        this.insert(parent, leaflist);
                }
            }
        }
    }

    //父结点和子结点list：确定父结点的left和right，及子结点的parent
    public TreeNode anotherinsert(TreeNode parentNode, List<Integer> leaflist) {
        for (TreeNode sonNode : nodeList) {
            List<Integer> list = sonNode.key;
            if (list.equals(leaflist)&&!sonNode.inserted) {
                if (list.size() < parentNode.key.size()) {
                    parentNode.left = sonNode;
                    sonNode.parent = parentNode;
                    leaflist = list;
                    return sonNode;
                } else {
                    parentNode.right = sonNode;
                    sonNode.parent = parentNode;
                    leaflist = list;
                    return sonNode;
                }
            }
        }
        return null;
    }

}
