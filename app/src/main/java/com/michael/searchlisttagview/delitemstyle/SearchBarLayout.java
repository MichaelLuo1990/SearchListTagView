package com.michael.searchlisttagview.delitemstyle;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.michael.searchlisttagview.R;
import com.michael.searchlisttagview.comm.MyEdittext;

/**
 * Created by michaelluo on 17/8/26.
 *
 * @desc 搜索栏container布局
 */
public class SearchBarLayout extends FrameLayout implements View.OnClickListener, SearchBarItem.OnDeleteButtonClickListener, TextWatcher {

    private MyEdittext myEdittext;
    private RelativeLayout rlRootSearchBar;
    private LinearLayout llSearchBar;
    private TextView tvBlank;
    private boolean mIsShowEdit = true;
    private SearchBarListener mSearchBarListener;
    private String inputText;

    private int mChooseBgColor;
    private int mChooseTextColor;
    private int mTextSize;
    private CharSequence mHint;

    public SearchBarLayout(Context context) {
        super(context);
        init(context);
    }

    public SearchBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle(context, attrs, 0, 0);
        init(context);
    }

    public SearchBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs, defStyleAttr, 0);
        init(context);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStyle(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        rlRootSearchBar = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_search_bar, null);
        llSearchBar = (LinearLayout) rlRootSearchBar.findViewById(R.id.show_choose_ll);
        tvBlank = (TextView) rlRootSearchBar.findViewById(R.id.tv_blank);
        tvBlank.setOnClickListener(this);
        addView(rlRootSearchBar);
        myEdittext = new MyEdittext(context);
        myEdittext.setBackgroundColor(Color.TRANSPARENT);
        myEdittext.addTextChangedListener(this);
        myEdittext.setSingleLine();
        if (mTextSize > 0) {
            myEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        }
        if (mHint != null) {
            setHint(mHint.toString());
        }

        setBackgroundResource(R.drawable.search_bar_bg_default);
        addView(myEdittext);
    }

    protected void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mChooseTextColor = Color.WHITE;
        mChooseBgColor = Color.GRAY;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchBarLayout, defStyleAttr, defStyleRes);
        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchBarLayout_cet_TextSize) {
                mTextSize = DensityUtils.px2sp(context, a.getDimensionPixelSize(attr, 0));
            } else if (attr == R.styleable.SearchBarLayout_cet_Hint) {
                mHint = a.getText(attr);
            } else if (attr == R.styleable.SearchBarLayout_cet_ChooseBgColor) {
                mChooseBgColor = a.getColor(attr, 0);
            } else if (attr == R.styleable.SearchBarLayout_cet_ChooseTextColor) {
                mChooseTextColor = a.getColor(attr, 0);
            }
        }
    }

    public void setOnChooseEditTextListener(SearchBarListener listener) {
        mSearchBarListener = listener;
    }

    public String getText() {
        return inputText;
    }

    public void setHint(String hint) {
        myEdittext.setHint(hint);
    }

    public void addSearchBarItem(String str) {
        removeItem();
        inputText = str;
        SearchBarItem searchBarItem = genItemWithDel(inputText);
        llSearchBar.addView(searchBarItem);
        showTagView();
    }

    /**
     * 生成带删除item
     *
     * @param text
     * @return
     */
    private SearchBarItem genItemWithDel(String text) {
        SearchBarItem searchBarItem = new SearchBarItem(getContext());
        searchBarItem.setTextColor(mChooseTextColor);
        searchBarItem.setBgColor(mChooseBgColor);
        if (mTextSize > 0) {
            searchBarItem.setTextSize(mTextSize);
        }
        searchBarItem.setText(text);
        searchBarItem.setOnDeleteButtonClickListener(this);
        return searchBarItem;
    }

    /**
     * 搜索栏显示编辑视图
     */
    public void showEditView(String str) {
        if (mIsShowEdit) return;
        rlRootSearchBar.setVisibility(INVISIBLE);
        myEdittext.setVisibility(VISIBLE);
        myEdittext.setText(str);
        myEdittext.setSelection(str.length());
        myEdittext.setFocusable(true);
        myEdittext.setFocusableInTouchMode(true);
        myEdittext.requestFocus();
        mIsShowEdit = true;
    }

    /**
     * 搜索栏显示标签视图
     */
    public void showTagView() {
        if (mIsShowEdit) {
            rlRootSearchBar.setVisibility(VISIBLE);
            myEdittext.setVisibility(INVISIBLE);
            myEdittext.setFocusable(false);
            myEdittext.setFocusableInTouchMode(false);
            myEdittext.clearFocus();
            mIsShowEdit = false;
        }
    }

    public void removeItem() {
        llSearchBar.removeAllViews();
        inputChange();
    }

    /**
     * 搜索栏容器布局空白区域点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (R.id.tv_blank == v.getId()) {
            showEditView("");
        }
    }

    /**
     * item删除按键点击事件
     */
    @Override
    public void onDelecteClick() {
        removeItem();
        showEditView("");
    }

    /**
     * item内容点击事件
     * @param text
     */
    @Override
    public void onContentClick(String text) {
        showEditView(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        inputText = s.toString();
        inputChange();
    }

    private void inputChange() {
        if (mSearchBarListener != null) {
            mSearchBarListener.onTextChangeed(inputText);
        }
    }

    public EditText getmEditText() {
        return myEdittext;
    }

    public void setmEditText(MyEdittext myEdittext) {
        this.myEdittext = myEdittext;
    }
}
