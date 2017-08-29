package com.michael.searchlisttagview.normalstyle;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.michael.searchlisttagview.comm.MyEdittext;
import com.michael.searchlisttagview.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * 搜索style1
 * 带删除按键搜索栏+搜索标签+搜索历史+sqlite存储
 */
public class SearchListTagActivity extends AppCompatActivity {

    private MyEdittext et_search;
    private ListView listView;
    private TextView tv_clear;
    private TagFlowLayout tagFlowLayout;
    private LinearLayout llSearch;
    private SLTSQLiteControlHelper sltsqLiteControlHelper;

    //标签布局测试数据
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome", "Button", "TextView", "Hello"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_lv_tag);
        initView();
        initSearchListener();
        initTagFlowLayout();
    }

    /**
     * 初始化view
     */
    private void initView() {
        et_search = (MyEdittext) findViewById(R.id.et_search);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                et_search.getWindowToken(), 0);
        listView = (ListView) findViewById(R.id.listView);
        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.tag_flow_layout);

        // 调整EditText左边的搜索按钮的大小
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
        et_search.setCompoundDrawables(drawable, null, null, null);// 只放左边
    }

    /**
     * 初始化搜索监听
     */
    private void initSearchListener() {
        // 第一次进入查询所有的历史记录
        sltsqLiteControlHelper = new SLTSQLiteControlHelper(this);
        sltsqLiteControlHelper.queryData("");
        if (!sltsqLiteControlHelper.isEmpty()) {
            llSearch.setVisibility(View.VISIBLE);
        } else {
            llSearch.setVisibility(View.GONE);
        }
        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sltsqLiteControlHelper.deleteData();
                sltsqLiteControlHelper.queryData("");
                llSearch.setVisibility(View.GONE);
            }
        });

        //触摸事件中获取焦点（软键盘）
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_search, InputMethodManager.SHOW_FORCED);
                if (!sltsqLiteControlHelper.isEmpty()) {
                    llSearch.setVisibility(View.VISIBLE);
                } else {
                    llSearch.setVisibility(View.GONE);
                }
            }
        });

        // 搜索框的键盘搜索键点击回调
        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = sltsqLiteControlHelper.hasData(et_search.getText().toString().trim());
                    if (!hasData) {
                        sltsqLiteControlHelper.insertData(et_search.getText().toString().trim());
                        sltsqLiteControlHelper.queryData(et_search.getText().toString().trim());
                    }
                    // 搜索服务请求跳转页面（根据业务逻辑拓展）
                    //...
                }
                return true;
            }
        });

        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                sltsqLiteControlHelper.queryData(tempName);
//                if(tempName.equals("")) {
//                    tagFlowLayout.setSelected(false);
//                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                et_search.setText(name);
//                Toast.makeText(SearchListTagActivity.this, name, Toast.LENGTH_SHORT).show();
                // 搜索服务请求跳转页面（根据业务逻辑拓展）
                //...
            }
        });
    }

    /**
     * 初始化标签检索
     */
    private void initTagFlowLayout() {
        tagFlowLayout.setMaxSelectCount(1);
        //已选中tag事件监听
//        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet) {
//
//            }
//        });
        //点击tag事件监听
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                et_search.setText(mVals[position]);
                boolean hasData = sltsqLiteControlHelper.hasData(et_search.getText().toString().trim());
                if (!hasData) {
                    sltsqLiteControlHelper.insertData(mVals[position]);
                    sltsqLiteControlHelper.queryData(mVals[position]);
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
                et_search.getWindowToken(), 0);
        this.finish();
    }

}
