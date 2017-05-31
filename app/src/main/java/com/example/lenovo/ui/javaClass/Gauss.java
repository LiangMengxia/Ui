package com.example.lenovo.ui.javaClass;

/**
 * Created by lenovo on 2016/7/5.
 */
public class Gauss {
    private static float[][] a;
    private static float[] b;
    private static int n;
    private static float[] x;
    private static int n2 = 0; //记录换行的次数

    public Gauss() {
        System.out.println("");
    }

    public static void setN(int i) {
        n = i;
    }

    public static void elimination(float[][] aa, float[] bb) {//消元
        a = new float[n + 1][n + 1];
        b = new float[n + 1];
        x = new float[n + 1];
        a = aa;
        b = bb;
        for (int k = 1; k <= n - 1; k++) {
            wrap(k);
            for (int i = k + 1; i <= n; i++) {
                if (a[k][k] != 0) {
                    float l = a[i][k] / a[k][k];
                    a[i][k] = 0;
                    for (int j = k + 1; j <= n; j++)
                        a[i][j] = a[i][j] - l * a[k][j];

                    b[i] = b[i] - l * b[k];
                } else break;
            }
        }
    }

    public static float[] back()//回代
    {
        if (a[n][n] != 0)
            x[n] = b[n] / a[n][n];

        for (int i = n - 1; i >= 1; i--) {
            if (a[i][i] != 0)
                x[i] = (b[i] - jisuan(i)) / a[i][i];
            else break;
        }
        return x;
    }


    public static float jisuan(int i) {
        float he = 0;
        for (int j = i + 1; j <= n; j++)
            he = he + x[j] * a[i][j];
        return he;
    }

    public static void wrap(int k) {//换行
        double max = Math.abs(a[k][k]);
        int n1 = k;                   //记住要交换的行
        for (int i = k + 1; i <= n; i++)     //找到要交换的行
        {
            if (Math.abs(a[i][k]) > max) {
                n1 = i;
                max = Math.abs(a[i][k]);
            }
        }
        if (n1 != k)//就交换k行和n1行，n1行为主元素所在行
        {
            System.out.println("当k=" + k + "时,要交换的行是：" + k + "和" + n1);
            for (int j = k; j <= n; j++)  //交换a的行
            {
                float x1;
                x1 = a[k][j];
                a[k][j] = a[n1][j];
                a[n1][j] = x1;
            }
            float b1;   //交换b的行
            b1 = b[k];
            b[k] = b[n1];
            b[n1] = b1;
            System.out.println("交换后：");
            PrintA();
        }
    }

    public static void Determinant() {//求行列式
        double DM = 1.0;
        for (int i = 1; i <= n; i++) {
            double a2 = a[i][i];
            DM = DM * a2;
        }
        double n3 = (double) n2;
        DM = DM * Math.pow(-1.0, n3);
        System.out.println("该方程组的系数行列式：det A = " + DM);
    }

    public static void PrintA() {//输出增广矩阵
        System.out.println("增广矩阵为：");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                System.out.print(a[i][j] + "    ");
            System.out.print(b[i] + "    ");
            System.out.print("\n");
        }
    }

    public static void Print() {//输出方程的根
        System.out.println("方程组的根为：");
        for (int i = 1; i <= n; i++)
            System.out.println("x" + i + " = " + x[i]);
    }

}

