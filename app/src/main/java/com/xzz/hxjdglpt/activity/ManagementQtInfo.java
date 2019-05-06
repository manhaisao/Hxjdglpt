package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Management;
import com.xzz.hxjdglpt.model.ManagementQt;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.BitmapUtil;
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
 * 个体基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_management_qt_info)
public class ManagementQtInfo extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private ManagementQt managementQt;
    private String gridId;
    @ViewInject(R.id.management_qt_name)
    private TextView mName;
    @ViewInject(R.id.management_qt_cun)
    private TextView mCun;
    @ViewInject(R.id.management_qt_grid)
    private TextView mGrid;
    @ViewInject(R.id.management_qt_gtlx)
    private TextView mQylx;
    @ViewInject(R.id.management_qt_fr)
    private TextView mFr;
    @ViewInject(R.id.management_qt_phone)
    private TextView mLxdh;
    @ViewInject(R.id.management_qt_zgs)
    private TextView mZgs;
    @ViewInject(R.id.management_qt_jyaddress)
    private TextView mJyAddress;
    //    @ViewInject(R.id.management_jyfw)
//    private TextView mJyfw;
    @ViewInject(R.id.management_qt_bz)
    private TextView mBz;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    @ViewInject(R.id.container)
    private LinearLayout container;

    private String[] path;

    private LayoutInflater mInflater;

    protected List<String> sourceImageList = new ArrayList<>();

    private String picPath = "";
    @ViewInject(R.id.management_qt_yyzz)
    private GridView gridView1;                   //营业执照
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private ArrayList<String> mPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        managementQt = getIntent().getParcelableExtra("managementQt");
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        path = managementQt.getPicpath().split(",");
        initView();
        initData();
    }

    public void initView() {
        int type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 46:
                tvTitle.setText("建筑工地");
                break;
            case 47:
                tvTitle.setText("特种设备");
                break;
            case 48:
                tvTitle.setText("危险化学品");
                break;
            case 49:
                tvTitle.setText("成品油");
                break;
        }
        imageItem = new ArrayList<HashMap<String, Object>>();
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.yyzz_add_pic, new
                String[]{"itemImage"}, new int[]{R.id.yyzz_add_pc});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("imagesName", picPath.split(","));
                intent.putExtra("index", position);
                intent.setClass(ManagementQtInfo.this, ShowImageActivity.class);
                startActivity(intent);
            }
        });

        if (managementQt.getPicpath() == null || TextUtils.isEmpty(managementQt.getPicpath())) {
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
                        intent.setClass(ManagementQtInfo.this, ShowImageActivity.class);
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

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String[] ps = picPath.split(",");
            for (int i = 0; i < ps.length; i++) {
                if (!TextUtils.isEmpty(ps[i])) {
                    mPath.add(ps[i]);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", BitmapUtil.getNetBitmap(ManagementQtInfo.this,
                            ConstantUtil.FILE_DOWNLOAD_URL + ps[i]));
                    imageItem.add(map);
                }
            }
            handler.sendEmptyMessage(0);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SuccinctProgress.dismiss();
            simpleAdapter.notifyDataSetChanged();
        }
    };

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
        SuccinctProgress.showSuccinctProgress(ManagementQtInfo.this, "数据请求中···", SuccinctProgress
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
                        mName.setText(managementQt.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        mZgs.setText(String.valueOf(managementQt.getEmployeenum()));
                        mFr.setText(managementQt.getLinkman());
                        mLxdh.setText(managementQt.getLinkmanphone());
                        mJyAddress.setText(managementQt.getAddress());
//                        mJyfw.setText(managementQt.getArea());
                        mQylx.setText(managementQt.getType());
                        mBz.setText(managementQt.getBz());
                        if (managementQt.getPicpath() != null) {
                            picPath = managementQt.getPicpath();
                            SuccinctProgress.showSuccinctProgress(ManagementQtInfo.this,
                                    "图片努力加载中···", SuccinctProgress.THEME_ULTIMATE, false, true);
                            new Thread(runnable1).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ManagementQtInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ManagementQtInfo.this, R.string.load_fail);
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
