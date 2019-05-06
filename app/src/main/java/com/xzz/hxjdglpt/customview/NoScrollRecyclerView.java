package com.xzz.hxjdglpt.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(Context context) {
        super(context);
    }
    public NoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int IheightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, IheightSpec);
    }
}
