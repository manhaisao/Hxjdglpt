package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Ldrk;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyImageLoader;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 流动人员基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_ldrk_info)
public class LdrkInfo extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Ldrk ldrk;
    private String gridId;
    @ViewInject(R.id.ldrk_name)
    private TextView mName;
    @ViewInject(R.id.ldrk_cun)
    private TextView mCun;
    @ViewInject(R.id.ldrk_grid)
    private TextView mGrid;
    @ViewInject(R.id.ldrk_sfzh)
    private TextView mCode;
    @ViewInject(R.id.ldrk_address)
    private TextView mAddress;
    @ViewInject(R.id.ldrk_reason)
    private TextView mReason;
    @ViewInject(R.id.ldrk_bz)
    private TextView mBz;
    @ViewInject(R.id.ldrk_info_ldlx)
    private TextView mLdlx;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    @ViewInject(R.id.container)
    private LinearLayout container;

    private String[] path = {};

    private LayoutInflater mInflater;

    protected List<String> sourceImageList = new ArrayList<>();

    private List<Role> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        ldrk = getIntent().getParcelableExtra("ldrk");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("流动人口");
//        if (cjry.getPicpath() == null || TextUtils.isEmpty(cjry.getPicpath())) {
        container.setVisibility(View.GONE);
//        }
        // 1.设置幕后item的缓存数目
        viewPager.setOffscreenPageLimit(4);
        // 2.设置页与页之间的间距
        viewPager.setPageMargin(10);
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        for (int i = 0; i < path.length; i++) {
            if (!TextUtils.isEmpty(path[i])) {
                final int postion = i;
                ImageView img = new ImageView(this);
                VolleyImageLoader.setImageRequest(this, ConstantUtil.FILE_DOWNLOAD_URL + path[i],
                        img);
                sourceImageList.add(ConstantUtil.FILE_DOWNLOAD_URL + path[i]);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("imagesName", path);
                        intent.putExtra("index", postion);
                        intent.setClass(LdrkInfo.this, ShowImageActivity.class);
                        startActivity(intent);
                    }
                });
                views.add(img);
            }
        }
        viewPager.setAdapter(new MyAdapter()); // 为viewpager设置adapter
        viewPager.setOnPageChangeListener(this);// 设置监听器
    }

    public void initData() {
        loadData();
    }

    // PagerAdapter是object的子类
    class MyAdapter extends PagerAdapter {

        /**
         * PagerAdapter管理数据大小
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 关联key 与 obj是否相等，即是否为同一个对象
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj; // key
        }

        /**
         * 销毁当前page的相隔2个及2个以上的item时调用
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
        }

        /**
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return views.get(position); // 返回该view对象，作为key
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (viewPager != null) {
            viewPager.invalidate();
        }
    }

    // 一个新页被调用时执行,仍为原来的page时，该方法不被调用
    public void onPageSelected(int position) {
//        tvTitle.setText(getFile(position));
    }

    /*
     * SCROLL_STATE_IDLE: pager处于空闲状态 SCROLL_STATE_DRAGGING： pager处于正在拖拽中
     * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
     */
    public void onPageScrollStateChanged(int state) {
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(LdrkInfo.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageByGridId";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                LogUtil.i("result=" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Village v = (Village) gson.fromJson(result.getJSONObject("data").toString
                                (), Village.class);
                        mName.setText(ldrk.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
//                        mCode.setText(ldrk.getSfzh());
                        if(isContain()){
                            mCode.setText(ldrk.getSfzh());
                        }else if (!TextUtils.isEmpty(ldrk.getSfzh()) && ldrk.getSfzh().length() > 8) {
                            String sfzh = ldrk.getSfzh().substring(0, ldrk.getSfzh().length() - 8);
                            mCode.setText(sfzh + "********");
                        }
                        mAddress.setText(ldrk.getAddress());
                        mReason.setText(ldrk.getNowAddress());
                        mLdlx.setText(ldrk.getLdlx());
                        mBz.setText(ldrk.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LdrkInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LdrkInfo.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
