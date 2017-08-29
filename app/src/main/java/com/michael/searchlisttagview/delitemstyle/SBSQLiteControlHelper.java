package com.michael.searchlisttagview.delitemstyle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.michael.searchlisttagview.normalstyle.SearchListTagActivity;

import java.util.Date;

/**
 * Created by michaelluo on 17/8/24.
 *
 * @desc sql插入、删除、查询相关控制
 */

public class SBSQLiteControlHelper {

    private SearchBarStyleActivity searchBarStyleActivity;
    private SBSQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public SBSQLiteControlHelper(SearchBarStyleActivity searchBarStyleActivity) {
        this.searchBarStyleActivity = searchBarStyleActivity;
        helper = new SBSQLiteOpenHelper(searchBarStyleActivity);
    }

    /**
     * 初始化历史记录测试数据-每次进入添加一条数据（取当前时间戳||random随机数）
     */
    private void insertTestData() {
        Date date = new Date();
        long time = date.getTime();
        insertData("test" + time);
    }

    /**
     * 插入数据
     */
    public void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    public void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        //页面刷新
        if(searchBarStyleActivity != null) {
            searchBarStyleActivity.refreshListviewAdapter(cursor);
        }

    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    public boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 是否为空（表没有记录）
     * @return
     */
    public boolean isEmpty() {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select name from records", null);
        if(cursor.getCount() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 清空数据
     */
    public void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
}
