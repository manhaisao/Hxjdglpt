package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jlfz;
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
import java.util.Map;

/**
 * 奖励扶助基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_jlfzinfo)
public class JlfzInfo extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    private User user;
    private Jlfz jlfz;
    private String gridId;

    @ViewInject(R.id.jlfz_info_name)
    private TextView mName;
    @ViewInject(R.id.jlfz_info_cun)
    private TextView mCun;
    @ViewInject(R.id.jlfz_info_grid)
    private TextView mGrid;
    @ViewInject(R.id.jlfz_info_sfzh)
    private TextView mSfzh;
    @ViewInject(R.id.jlfz_info_address)
    private TextView mAddress;
    @ViewInject(R.id.jlfz_info_lx)
    private TextView mType;
    @ViewInject(R.id.jlfz_info_phone)
    private TextView mPhone;
    @ViewInject(R.id.jlfz_info_zt)
    private TextView mZt;
    @ViewInject(R.id.jlfz_info_ldlx)
    private TextView mLdlx;

    @ViewInject(R.id.jlfz_info_bz)
    private TextView mBz;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    @ViewInject(R.id.container)
    private LinearLayout container;

    private String[] path;

    private LayoutInflater mInflater;

    protected List<String> sourceImageList = new ArrayList<>();

    @ViewInject(R.id.jlfz_drz_commit)
    private Button btnRz;

    @ViewInject(R.id.jlfz_drz_th)
    private Button btnTh;

    @ViewInject(R.id.jlfz_drz_lay)
    private LinearLayout mLay;

    private List<Role> roles;

    private MyAdapter myAdapter;
    @ViewInject(R.id.jlfz_info_t_thyy)
    private TextView mTvThyy;
    @ViewInject(R.id.jlfz_info_thyy)
    private TextView mThyy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        jlfz = getIntent().getParcelableExtra("jlfz");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        initView();
        initData();
    }

    /**
     * 判断是否是网格长或者网格员
     *
     * @return
     */
    private boolean isWg() {
        for (Role r : roles) {
            if ("4200".equals(r.getRoleId()) || "4224".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否具有确认认证的权限
     *
     * @return
     */
    private boolean isComm() {
        for (Role r : roles) {
            if ("4243".equals(r.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        tvTitle.setText("奖励扶助");
        if (jlfz.getPicture() == null || TextUtils.isEmpty(jlfz.getPicture())) {
            container.setVisibility(View.GONE);
        }
        if (jlfz.getIsRz() == 2 && isComm()) {
            mLay.setVisibility(View.VISIBLE);
        } else {
            mLay.setVisibility(View.GONE);
        }
        path = jlfz.getPicture().split(",");
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
                        intent.setClass(JlfzInfo.this, ShowImageActivity.class);
                        startActivity(intent);
                    }
                });
                views.add(img);
            }
        }
        myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter); // 为viewpager设置adapter
        viewPager.setOnPageChangeListener(this);// 设置监听器
    }

    public void initData() {
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == 1) {
                String result = data.getStringExtra("picture");
                jlfz.setPicture(result);
                jlfz.setIsRz(2);
                if (jlfz.getPicture() == null || TextUtils.isEmpty(jlfz.getPicture())) {
                    container.setVisibility(View.GONE);
                } else {
                    container.setVisibility(View.VISIBLE);
                }
                if (jlfz.getIsRz() == 2 && isComm()) {
                    mLay.setVisibility(View.VISIBLE);
                } else {
                    mLay.setVisibility(View.GONE);
                }
                path = jlfz.getPicture().split(",");
                views.clear();
                sourceImageList.clear();
                for (int i = 0; i < path.length; i++) {
                    if (!TextUtils.isEmpty(path[i])) {
                        final int postion = i;
                        ImageView img = new ImageView(this);
                        VolleyImageLoader.setImageRequest(this, ConstantUtil.FILE_DOWNLOAD_URL +
                                path[i], img);
                        sourceImageList.add(ConstantUtil.FILE_DOWNLOAD_URL + path[i]);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("imagesName", path);
                                intent.putExtra("index", postion);
                                intent.setClass(JlfzInfo.this, ShowImageActivity.class);
                                startActivity(intent);
                            }
                        });
                        views.add(img);
                    }
                }
                myAdapter.notifyDataSetChanged();
                if (jlfz.getIsRz() == 0) {
                    mZt.setText("未认证");
                    tvRight.setVisibility(View.VISIBLE);
                } else if (jlfz.getIsRz() == 1) {
                    mZt.setText("已认证");
                    tvRight.setVisibility(View.GONE);
                } else if (jlfz.getIsRz() == 2) {
                    mZt.setText("认证中");
                    tvRight.setVisibility(View.GONE);
                } else if (jlfz.getIsRz() == 3) {
                    mZt.setText("已退回");
                    tvRight.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        SuccinctProgress.showSuccinctProgress(JlfzInfo.this, "数据请求中···", SuccinctProgress
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
                        mName.setText(jlfz.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
//                        mSfzh.setText(jlfz.getSfzh());
                        if(isContain()){
                            mSfzh.setText(jlfz.getSfzh());
                        }else if (!TextUtils.isEmpty(jlfz.getSfzh()) && jlfz.getSfzh().length() > 8) {
                            String sfzh = jlfz.getSfzh().substring(0, jlfz.getSfzh().length() - 8);
                            mSfzh.setText(sfzh + "********");
                        }
                        mType.setText(jlfz.getType());
                        mAddress.setText(jlfz.getAddress());
                        mPhone.setText(jlfz.getPhone());
                        mLdlx.setText(jlfz.getLdlx());
                        if (jlfz.getIsRz() == 0) {
                            mZt.setText("未认证");
                            if (isWg()) {
                                tvRight.setText("认证");
                            }
                            mTvThyy.setVisibility(View.GONE);
                            mThyy.setVisibility(View.GONE);
                        } else if (jlfz.getIsRz() == 1) {
                            mZt.setText("已认证");
                            mTvThyy.setVisibility(View.GONE);
                            mThyy.setVisibility(View.GONE);
                        } else if (jlfz.getIsRz() == 2) {
                            mZt.setText("认证中");
                            mTvThyy.setVisibility(View.GONE);
                            mThyy.setVisibility(View.GONE);
                        } else if (jlfz.getIsRz() == 3) {
                            mZt.setText("已退回");
                            if (isWg()) {
                                tvRight.setText("认证");
                            }
                            mTvThyy.setVisibility(View.VISIBLE);
                            mThyy.setVisibility(View.VISIBLE);
                            mThyy.setText(jlfz.getReason());
                        }
                        mBz.setText(jlfz.getBz());
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlfzInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlfzInfo.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("shJlfzById");
    }

    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right, R.id.jlfz_drz_commit, R.id
            .jlfz_drz_th}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                Intent intent = new Intent();
                intent.setClass(JlfzInfo.this, JlfzRZActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("gridId", gridId);
                bundle.putParcelable("jlfz", jlfz);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                break;
            case R.id.jlfz_drz_commit:
                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final Dialog dialog = new Dialog(JlfzInfo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
                Button butOk = (Button) view.findViewById(R.id.dialog_ok);
                Button butCancle = (Button) view.findViewById(R.id.dialog_cancel);
                butOk.setText("确认");
                butCancle.setText("取消");
                butCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
                tvContent.setText("是否确认?");
                butOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        update("1", null);
                    }
                });
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = dm.widthPixels - 50;
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                break;
            case R.id.jlfz_drz_th:
                createThReasonDialog();
                break;
        }
    }

    private void createIsThDialog(final String reason) {
        View view1 = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Dialog dialog1 = new Dialog(JlfzInfo.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(view1);
        TextView tvContent1 = (TextView) view1.findViewById(R.id.dialog_content);
        Button butOk1 = (Button) view1.findViewById(R.id.dialog_ok);
        Button butCancle1 = (Button) view1.findViewById(R.id.dialog_cancel);
        butOk1.setText("确认");
        butCancle1.setText("取消");
        butCancle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
        tvContent1.setText("是否退回?");
        butOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                update("3", reason);
            }
        });
        DisplayMetrics dm1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm1);
        WindowManager.LayoutParams lp1 = dialog1.getWindow().getAttributes();
        lp1.width = dm1.widthPixels - 50;
        dialog1.getWindow().setAttributes(lp1);
        dialog1.show();
    }

    private void createThReasonDialog() {
        View view = getLayoutInflater().inflate(R.layout.jlfz_reason_dialog, null);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
        final EditText edtContent = (EditText) view.findViewById(R.id.epj_title_laber);
        Button butOk = (Button) view.findViewById(R.id.dialog_ok);
        Button butCancle = (Button) view.findViewById(R.id.dialog_cancel);
        butOk.setText("确认");
        butCancle.setText("取消");
        butCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        tvContent.setText("请填写退回原因");
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    dialog.dismiss();
                    if (!TextUtils.isEmpty(edtContent.getText().toString())) {
                        createIsThDialog(edtContent.getText().toString());
                    }
                } else {
                    ToastUtil.show(JlfzInfo.this, "请填写退回原因");
                }

            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    /**
     * 提交信息
     */
    private void update(final String type, String reason) {
        String url = ConstantUtil.BASE_URL + "/jlfz/shJlfzById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(jlfz.getId()));
        params.put("isRz", type);
        params.put("gId", gridId);
        params.put("name", jlfz.getName());
        params.put("sfzh", jlfz.getSfzh());
        params.put("type", jlfz.getType());
        params.put("address", jlfz.getAddress());
        params.put("phone", jlfz.getPhone());
        params.put("bz", jlfz.getBz());
        params.put("ldlx", jlfz.getLdlx()==null?"":jlfz.getLdlx());
        params.put("picture", jlfz.getPicture());
        if ("3".equals(type)) {
            params.put("reason", reason);
        }
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "shJlfzById", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        if ("3".equals(type)) {
                            ToastUtil.show(JlfzInfo.this, "已退回");
                        } else {
                            ToastUtil.show(JlfzInfo.this, "成功确认认证");
                        }
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlfzInfo.this);
                    } else if ("3".equals(resultCode)) {
                        if ("3".equals(type)) {
                            ToastUtil.show(JlfzInfo.this, "退回失败");
                        } else {
                            ToastUtil.show(JlfzInfo.this, "确认认证失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }
}
