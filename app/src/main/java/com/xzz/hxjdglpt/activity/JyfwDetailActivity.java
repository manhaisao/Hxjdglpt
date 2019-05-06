package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jyfw;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyImageLoader;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
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
 * Created by dbz on 2017/8/23.
 */
@ContentView(R.layout.aty_jyfw_detail)
public class JyfwDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private User user;

    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back_tv)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title_right)
    private TextView tvRight;

    @ViewInject(R.id.jyfw_detail_title)
    private TextView tvBTitle;
    @ViewInject(R.id.jyfw_detail_time)
    private TextView tvTime;
    @ViewInject(R.id.jyfw_detail_content)
    private TextView tvContent;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    @ViewInject(R.id.container)
    private LinearLayout container;


    private Jyfw jyfw;

    private String[] path;

    protected List<String> sourceImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        jyfw = getIntent().getParcelableExtra("jyfw");
        path = jyfw.getPath().split(",");
        if (isGly()) {
            tvRight.setText("删除");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
        initView();
        initData();
    }


    private boolean isGly() {
        for (Role role : application.getRolesList()) {
            if ("4237".equals(role.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    public void initView() {
        tvTitle.setText("就业服务");
        if (jyfw.getPath() == null || TextUtils.isEmpty(jyfw.getPath())) {
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
                        intent.putExtra("index", postion);
                        intent.setClass(JyfwDetailActivity.this, ShowImageActivity.class);
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
        tvBTitle.setText(jyfw.getTitle());
        tvTime.setText("发布时间：" + jyfw.getFbsj().substring(0, 10));
        tvContent.setText(jyfw.getContent());
    }


    @Event(value = {R.id.iv_back_tv, R.id.hx_title_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.hx_title_right:
                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final Dialog dialog = new Dialog(JyfwDetailActivity.this);
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
                tvContent.setText("确认删除?");
                butOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        delJyfw();
                    }
                });
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = dm.widthPixels - 50;
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                break;
        }
    }

    private void delJyfw() {
        String url = ConstantUtil.BASE_URL + "/jyfw/deleteJyfwList";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(jyfw.getId()));
        SuccinctProgress.showSuccinctProgress(JyfwDetailActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "deleteJyfwList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        finish();
                        ToastUtil.show(JyfwDetailActivity.this, "成功删除");
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JyfwDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JyfwDetailActivity.this, R.string.load_fail);
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
        BaseApplication.getRequestQueue().cancelAll("deleteJyfwList");
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

}
