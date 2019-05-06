package com.xzz.hxjdglpt.activity;

import java.util.ArrayList;
import java.util.List;

import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.volley.VolleyImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class ShowImageActivity extends Activity implements OnClickListener, OnPageChangeListener {

    private ViewPager mViewPager;
    private TextView mTextView;
    private ArrayList<View> viewcontainter;
    private int index = 0;
    private String[] fileName;
    private boolean[] isLocal;
    private RelativeLayout mRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        initView();
        initData();
    }

    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.show_image_viewpager);
        mTextView = (TextView) findViewById(R.id.show_image_num);
        mRl = (RelativeLayout) findViewById(R.id.image_container);
        viewcontainter = new ArrayList<View>();
        mViewPager.setOnPageChangeListener(this);
        mRl.setOnClickListener(this);
        mViewPager.setOnClickListener(this);
    }

    public void initData() {
        fileName = getIntent().getStringArrayExtra("imagesName");
        isLocal = getIntent().getBooleanArrayExtra("isLocal");
        index = getIntent().getIntExtra("index", 0);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fileName.length; i++) {
            if (fileName[i] != null && !TextUtils.isEmpty(fileName[i])) {
                ImageView mImage = new ImageView(this);
                mImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                mImage.setLayoutParams(params);
                if (isLocal != null && isLocal[i]) {
                    Bitmap bm = BitmapFactory.decodeFile(fileName[i]);
                    mImage.setImageBitmap(bm);
                } else {
                    VolleyImageLoader.setImageRequest(this, ConstantUtil.FILE_DOWNLOAD_URL +
                            fileName[i], mImage);

                }
                list.add(fileName[i]);
                viewcontainter.add(mImage);
            }
        }
        fileName = (String[]) list.toArray(new String[list.size()]);
        mViewPager.setAdapter(new PagerAdapter() {

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
        mTextView.setText((index + 1) + "/" + fileName.length);
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_container:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        mTextView.setText((arg0 + 1) + "/" + fileName.length);
    }

}
