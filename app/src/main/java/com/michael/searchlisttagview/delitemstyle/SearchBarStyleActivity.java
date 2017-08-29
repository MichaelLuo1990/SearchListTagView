package com.michael.searchlisttagview.delitemstyle;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.michael.searchlisttagview.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import static com.michael.searchlisttagview.R.id.et_search;

public class SearchBarStyleActivity extends AppCompatActivity {
    private SearchBarLayout searchBarLayout;
    private TagFlowLayout tagFlowLayout;
    private ListView listView;
    private LinearLayout llSearch;
    private TextView tv_clear;
    private SBSQLiteControlHelper sbsqLiteControlHelper;

    //标签布局测试数据
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome", "Button", "TextView", "Hello"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar_style);
        initView();
        initSearchBarLayout();
        initTagFlowLayout();
        initSearchListListener();
    }

    /**
     * 初始化view
     */
    private void initView() {
        searchBarLayout = (SearchBarLayout) findViewById(R.id.chooseedittext);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.tag_flow_layout);
        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        listView = (ListView) findViewById(R.id.listView);
    }

    /**
     * 初始化搜索栏
     */
    private void initSearchBarLayout() {
        searchBarLayout.setOnChooseEditTextListener(new SearchBarListener() {
            @Override
            public void onTextChangeed(String text) {
                // 根据tempName去模糊查询数据库中有没有数据
                sbsqLiteControlHelper.queryData(text);
            }
        });
    }

    /**
     * 初始化标签检索
     */
    private void initTagFlowLayout() {
        tagFlowLayout.setMaxSelectCount(1);
        //点击tag事件监听
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                searchBarLayout.removeItem();
                searchBarLayout.addSearchBarItem(mVals[position]);
                boolean hasData = sbsqLiteControlHelper.hasData(searchBarLayout.getText());
                if (!hasData) {
                    sbsqLiteControlHelper.insertData(mVals[position]);
                    sbsqLiteControlHelper.queryData(mVals[position]);
                }
                if(llSearch.getVisibility() == View.GONE) {
                    llSearch.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        //tag适配
        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        TagAdapter<String> tagDepAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String str) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.tv_tag_item,
                        tagFlowLayout, false);
                tv.setText(str);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(tagDepAdapter);
    }

    /**
     * 初始化搜索列表监听
     */
    private void initSearchListListener() {
        // 第一次进入查询所有的历史记录
        sbsqLiteControlHelper = new SBSQLiteControlHelper(SearchBarStyleActivity.this);
        sbsqLiteControlHelper.queryData("");
        if (!sbsqLiteControlHelper.isEmpty()) {
            llSearch.setVisibility(View.VISIBLE);
        } else {
            llSearch.setVisibility(View.GONE);
        }
        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbsqLiteControlHelper.deleteData();
                sbsqLiteControlHelper.queryData("");
                llSearch.setVisibility(View.GONE);
            }
        });

        //触摸事件中获取焦点（软键盘）
        searchBarLayout.getmEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBarLayout.getmEditText(), InputMethodManager.SHOW_FORCED);
                if (!sbsqLiteControlHelper.isEmpty()) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }
        });

        // 搜索框的键盘搜索键点击回调
        searchBarLayout.getmEditText().setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = sbsqLiteControlHelper.hasData(searchBarLayout.getText());
                    if (!hasData) {
                        sbsqLiteControlHelper.insertData(searchBarLayout.getText());
                        sbsqLiteControlHelper.queryData(searchBarLayout.getText());
                    }
                    // 搜索服务请求跳转页面（根据业务逻辑拓展）
                    //...
                }
                return true;
            }
        });

        // 搜索框的文本变化实时监听
        searchBarLayout.getmEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempName = searchBarLayout.getText();
                // 根据tempName去模糊查询数据库中有没有数据
                sbsqLiteControlHelper.queryData(tempName);
            }
        });

        //列表item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                searchBarLayout.getmEditText().setText(name);
//                Toast.makeText(SearchListTagActivity.this, name, Toast.LENGTH_SHORT).show();
                // 搜索服务请求跳转页面（根据业务逻辑拓展）
                //...
            }
        });
    }

    /**
     * 依据SQL查询返回结果刷新页面
     *
     * @param cursor
     */
    public void refreshListviewAdapter(Cursor cursor) {
        // 创建adapter适配器对象
        BaseAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"},
                new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(listView.getCount() <= 0) {
            llSearch.setVisibility(View.GONE);
        } else {
            llSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回按键
     * @param view
     */
    public void backClick(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                searchBarLayout.getmEditText().getWindowToken(), 0);
        this.finish();
    }

}
