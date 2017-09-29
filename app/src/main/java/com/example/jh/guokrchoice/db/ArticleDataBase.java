package com.example.jh.guokrchoice.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.jh.guokrchoice.bean.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dazz on 2016/4/23.
 */
public class ArticleDataBase {
    private final DataBaseHelper helper;

    public ArticleDataBase(Context context) {
        helper = new DataBaseHelper(context);
    }

    /**
     * 增
     * @param article
     */
    public void insert(Article article){
        String sql = "insert into " + DataBaseHelper.TABLE_NAME
                +"( id, read, favor, date_picked, data) values( ?, ?, ?, ?, ?)";
        SQLiteDatabase sqlite = helper.getWritableDatabase();
        sqlite.execSQL(sql,new String[]{
                article.getId()+"",article.getInFavor()+"",article.getDate_picked()+"",article.getTitle()
        });
        sqlite.close();
    }

    /**
     * 删
     * @param id
     */
    public void delete(int id){
        String sql = "delete from "+DataBaseHelper.TABLE_NAME +" where id=?";
        SQLiteDatabase sqlite = helper.getWritableDatabase();
        sqlite.execSQL(sql,new Integer[]{id});
        sqlite.close();
    }

    /**
     * 改
     * @param article
     */
    public void update(Article article){
        String sql = "update "+ DataBaseHelper.TABLE_NAME
                + " set favor=?,date_picked=?,data=? where id=?";

        SQLiteDatabase sqlite = helper.getWritableDatabase();
        sqlite.execSQL(sql,new String[]{
                article.getInFavor()+"",article.getDate_picked()+"",article.getTitle(),article.getId()+""
        });
        sqlite.close();
    }


    /**
     * 查
     * @param where
     * @return
     */
    public List<Article> query(String where){
        SQLiteDatabase sqlite = helper.getReadableDatabase();
        ArrayList<Article> data = null;
        data = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DataBaseHelper.TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Article article = new Article();
            article.setId(cursor.getInt(0));
            article.setInFavor(cursor.getInt(1));
            article.setDate_picked(cursor.getInt(2));
            article.setTitle(cursor.getString(3));
            data.add(article);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Article query(int id){
        List<Article> data = query("where id="+id);
        if (data.size()>0){
            return data.get(0);
        }
        return null;
    }

    /**
     * 查询所有
     * @return
     */
    public List<Article> query(){
        return query(" ");
    }

}
