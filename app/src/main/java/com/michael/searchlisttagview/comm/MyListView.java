package com.michael.searchlisttagview.comm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import static com.michael.searchlisttagview.R.id.listView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置不可滚动
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 分辨率装换
     *
     * @param context
     * @param dp
     * @return
     */
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 动态控制listView的高度
     * 在每次listView的adapter发生变化后，要调用setListViewHeightBasedOnChildren(listView)更新界面
     *
     * @param context
     * @param listView
     * @param count 显示条数 (-1 -> 显示所有)
     */
    public void setListViewHeightBasedOnChildren(Context context, ListView listView, int count) {
        //获取listview的适配器
        ListAdapter listAdapter = listView.getAdapter(); //item的高度
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        if (count == -1) {
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); //计算子项View 的宽高
                totalHeight += Dp2Px(context, listItem.getMeasuredHeight()) + listView.getDividerHeight();// 统计所有子项的总高度
            }
        } else {
            int singleItemHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); //计算子项View 的宽高
                singleItemHeight = Dp2Px(context, listItem.getMeasuredHeight()) + listView.getDividerHeight();
                break;
            }
            totalHeight = singleItemHeight;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

}
