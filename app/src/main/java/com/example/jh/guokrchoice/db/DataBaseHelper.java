package com.example.jh.guokrchoice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dazz on 2016/4/22.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "guokr";

    public final static String TABLE_NAME = "article";


    public final static String CREATE_ARTICLE_LIST_TABLE = "create table "
            + TABLE_NAME + "(" + "id integer primary key, "
            + "favor integer, date_picked integer, data text" + ")";

    public DataBaseHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
