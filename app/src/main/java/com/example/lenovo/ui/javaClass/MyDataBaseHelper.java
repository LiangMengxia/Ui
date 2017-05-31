package com.example.lenovo.ui.javaClass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2017/5/18.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {
   /*
   * context：上下文
   * databaseName:创建的数据库名称
   * databaseVersion：数据库版本
   * */
    public MyDataBaseHelper(Context context, String databaseName, int databaseVersion){
        super(context,databaseName,null,databaseVersion);
    }
    /*
    * 数据库第一次创建的时候，调用onCreate；数据库已经创建成功之后，就不调用它了
    * db就是创建的数据库
    * db.execSQL这句是用来创建数据库表
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("数据库创建成功");
        db.execSQL("create table imagetable(_id integer primary key autoincrement,word varchar(255),detail varchar(255))");//执行创建表的sql语句
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
