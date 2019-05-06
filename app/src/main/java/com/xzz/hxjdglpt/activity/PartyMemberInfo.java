package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.hitomi.universalloader.UniversalImageLoader;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.PartyMember;
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
 * 党员基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_partymember_info)
public class PartyMemberInfo extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private PartyMember partyMember;
    private String gridId;
    @ViewInject(R.id.party_name)
    private TextView mName;
    @ViewInject(R.id.party_cun)
    private TextView mCun;
    @ViewInject(R.id.party_grid)
    private TextView mGrid;
    @ViewInject(R.id.party_dzb)
    private TextView mDzb;
    @ViewInject(R.id.party_phone)
    private TextView mPhone;
    @ViewInject(R.id.party_xl)
    private TextView mXl;
    @ViewInject(R.id.party_type)
    private TextView mType;
    @ViewInject(R.id.party_bz)
    private TextView mBz;

    @ViewInject(R.id.party_sex)
    private TextView mXb;
    @ViewInject(R.id.party_mz)
    private TextView mMz;
    @ViewInject(R.id.party_sfzh)
    private TextView mSfzh;
    @ViewInject(R.id.party_birth)
    private TextView mBirth;
    @ViewInject(R.id.party_time)
    private TextView mTime;
    @ViewInject(R.id.party_dw)
    private TextView mDw;
    @ViewInject(R.id.party_ldtype)
    private TextView mLdlx;
    @ViewInject(R.id.party_reason)
    private TextView mReason;
    @ViewInject(R.id.party_reason_lay)
    private LinearLayout mReasonLay;


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
        partyMember = getIntent().getParcelableExtra("partyMember");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (partyMember.getPicpath() != null) {
            path = partyMember.getPicpath().split(",");
        }
        initView();
        initData();
    }

    //打码权限
    private boolean isContain() {
        for (Role r : roles) {
            if ("4257".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        tvTitle.setText(R.string.party_info_);
        if (partyMember.getPicpath() == null || TextUtils.isEmpty(partyMember.getPicpath())) {
            container.setVisibility(View.GONE);
        }
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
//                        intent.putExtra("isLocal", false);
                        intent.putExtra("index", postion);
                        intent.setClass(PartyMemberInfo.this, ShowImageActivity.class);
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

    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(PartyMemberInfo.this, "数据请求中···", SuccinctProgress
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
                        mName.setText(partyMember.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        mDzb.setText(partyMember.getDepart());
                        mPhone.setText(partyMember.getPhone());
                        mXl.setText(partyMember.getXl());
                        mBz.setText(partyMember.getBz());
                        String[] dytypes = getResources().getStringArray(R.array.dy_type_list);
                        if ("01".equals(partyMember.getType())) {
                            mType.setText(dytypes[0]);
                            mReasonLay.setVisibility(View.GONE);
                        } else if ("02".equals(partyMember.getType())) {
                            mType.setText(dytypes[1]);
                            mReason.setText(partyMember.getReason());
                            mReasonLay.setVisibility(View.VISIBLE);
                        }
                        String[] dyldtypes = getResources().getStringArray(R.array.dy_ldtype_list);
                        if ("1".equals(partyMember.getIsliuru())) {
                            mLdlx.setText(dyldtypes[0]);
                        } else if ("2".equals(partyMember.getIsliuru())) {
                            mLdlx.setText(dyldtypes[1]);
                        } else if ("3".equals(partyMember.getIsliuru())) {
                            mLdlx.setText(dyldtypes[2]);
                        }
                        mXb.setText(partyMember.getXb());
                        mMz.setText(partyMember.getMz());
                        if (isContain()) {
                            mSfzh.setText(partyMember.getNo());
                        } else if (!TextUtils.isEmpty(partyMember.getNo()) && partyMember.getNo().length() > 8) {
                            String sfzh = partyMember.getNo().substring(0, partyMember.getNo().length() - 8);
                            mSfzh.setText(sfzh + "********");
                        }
                        mBirth.setText(partyMember.getBirth());
                        mTime.setText(partyMember.getStime());
                        mDw.setText(partyMember.getGzdwjzw());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PartyMemberInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PartyMemberInfo.this, R.string.load_fail);
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
