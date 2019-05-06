package com.xzz.hxjdglpt.activity;

import java.util.ArrayList;

import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.VersionInfoUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuidePageActivity extends BaseActivity {

    private ViewPager viewPage;
    private ArrayList<View> viewcontainter;
    private ImageView but;
    private SharedPreferences isFirstShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstShow = getSharedPreferences(ConstantUtil.FILE_NAME_SHARE, Activity.MODE_PRIVATE);
        String version = VersionInfoUtil.getVersionName(this);
        if (version.equals(isFirstShow.getString("isFirst", ""))) {
            Intent intent = new Intent();
            intent.setClass(GuidePageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_guide_page);
            SharedPreferences.Editor editor = isFirstShow.edit();
            editor.putString("isFirst", version);
            editor.commit();
            initView();
            initData();
        }

    }

    public void initView() {
        viewPage = (ViewPager) findViewById(R.id.guide_page_viewpage);

    }

    public void initData() {
        viewcontainter = new ArrayList<View>();
        View view1 = getLayoutInflater().inflate(R.layout.guide_page_image_one, null);
        View view2 = getLayoutInflater().inflate(R.layout.guide_page_image_two, null);
        but = (ImageView) view2.findViewById(R.id.guide_page_start);
        but.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(GuidePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewcontainter.add(view1);
        viewcontainter.add(view2);
        viewPage.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return viewcontainter.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(viewcontainter.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewcontainter.get(position), 0);
                return viewcontainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return super.getPageTitle(position);
            }
        });

    }

}
