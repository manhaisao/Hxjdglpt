package com.xzz.hxjdglpt.adapter;

import android.view.View;

/**
 * 回复评论表中控件自定义点击事件
 * Created by dbz on 2017/8/28.
 */

public interface ListItemClickHelp {
    void onUptClick(View widget, int position);

    void onDelClick(View widget, int position);
}
