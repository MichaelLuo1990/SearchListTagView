package com.michael.searchlisttagview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.michael.searchlisttagview.delitemstyle.SearchBarStyleActivity;
import com.michael.searchlisttagview.normalstyle.SearchListTagActivity;

/**
 * Created by michaelluo on 17/8/29.
 *
 * @desc 显示搜索标签列表样式人口
 * add->delitemstyle(带删除item)  &   normalstyle（普通样式）
 * mark：多标签待添加。。。
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * 带删除item
     * @param view
     */
    public void toDelItemStyleAty(View view) {
        Intent intent = new Intent(MainActivity.this, SearchBarStyleActivity.class);
        startActivity(intent);
    }

    /**
     * 普通样式
     * @param view
     */
    public void toNormalStyleAty(View view) {
        Intent intent = new Intent(MainActivity.this, SearchListTagActivity.class);
        startActivity(intent);
    }

}
