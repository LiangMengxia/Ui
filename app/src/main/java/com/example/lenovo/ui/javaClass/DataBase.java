package com.example.lenovo.ui.javaClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lenovo on 2017/5/25.
 */
public class DataBase {
    MyDataBaseHelper myDataBaseHelper;

    public DataBase(Context context, String databaseName, int databaseVersion) {
        myDataBaseHelper = new MyDataBaseHelper(context, databaseName, databaseVersion);
    }

    public void insertData(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();//得到可写入数据的数据库
        db.insert(tableName, null, contentValues);
        db.close();
    }

    public String[] searchData(String tableName, String[] columns) {
        SQLiteDatabase db = myDataBaseHelper.getReadableDatabase();//得到可读取数据的数据库
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
        String[] searchResult = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String s = cursor.getString(i) + "\n";
                searchResult[i] += s;//将第i列的第一行到最后一行数据都放到searchResult[i]里面
            }
        }
        cursor.close();
        db.close();
        return searchResult;
    }
}
