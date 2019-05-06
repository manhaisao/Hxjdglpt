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
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jzgd;
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
 * 企业基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_jzgd_info)
public class JzgdInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Jzgd jzgd;
    private String gridId;
    @ViewInject(R.id.company_name)
    private TextView mName;//业主名称
    @ViewInject(R.id.company_cun)
    private TextView mCun;
    @ViewInject(R.id.company_grid)
    private TextView mGrid;
    @ViewInject(R.id.company_fr)
    private TextView mFr;//法定代表人
    @ViewInject(R.id.company_frdh)
    private TextView mFrdh;//法定代表人
    @ViewInject(R.id.company_qylb)
    private TextView mQylb;//应急器具配备情况
    @ViewInject(R.id.company_t_qylb)
    private TextView mQylbt;//应急器具配备情况
    @ViewInject(R.id.company_t_zczb)
    private TextView mZczbt;//注册资本
    @ViewInject(R.id.company_zczb)
    private TextView mZczb;//注册资本
    @ViewInject(R.id.company_t_jyfw)
    private TextView mJyfwt;//护税责任人
    @ViewInject(R.id.company_t_jyfwdh)
    private TextView mJyfwdht;//护税责任人
    @ViewInject(R.id.company_jyfw)
    private TextView mJyfw;//护税责任人
    @ViewInject(R.id.company_jyfwdh)
    private TextView mJyfwdh;//护税责任人
    @ViewInject(R.id.company_aqzrr)
    private TextView mAqzrr;//安全责任人
    @ViewInject(R.id.company_aqzrrdh)
    private TextView mAqzrrdh;//安全责任人
    @ViewInject(R.id.company_aqy)
    private TextView mAqy;//安全员
    @ViewInject(R.id.company_aqydh)
    private TextView mAqydh;//安全员
    @ViewInject(R.id.company_t_aqzrr)
    private TextView mAqzrrt;
    @ViewInject(R.id.company_t_aqzrrdh)
    private TextView mAqzrrPhonet;
    @ViewInject(R.id.company_t_aqy)
    private TextView mAqyt;
    @ViewInject(R.id.company_t_aqydh)
    private TextView mAqyPhonet;
    @ViewInject(R.id.company_bz)
    private TextView mBz;
    @ViewInject(R.id.company_dm)
    private TextView mDm;//施工方
    @ViewInject(R.id.company_wgzrr)
    private TextView mZrr;
    @ViewInject(R.id.company_wgzrrdh)
    private TextView mZrrdh;
    @ViewInject(R.id.company_danwei)
    private TextView mDw;
    @ViewInject(R.id.company_name_)
    private TextView mName_;

    @ViewInject(R.id.company_t_cun)
    private TextView mCunt;
    @ViewInject(R.id.company_t_grid)
    private TextView mWgt;

    //    @ViewInject(R.id.viewpager)
//    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();

    private String[] path;

    private LayoutInflater mInflater;

    protected List<String> sourceImageList = new ArrayList<>();

    private String picPath = "";
    @ViewInject(R.id.company_yyzz)
    private GridView gridView1;                   //营业执照
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private ArrayList<String> mPath = new ArrayList<>();

    @ViewInject(R.id.company_jctj)
    private TextView mJcTitle;
    @ViewInject(R.id.company_jctj_table)
    private TableLayout tableLayout;

    @ViewInject(R.id.company_nstj)
    private TextView mNsqk1;
    @ViewInject(R.id.company_nstj_table)
    private TableLayout mNsqkTable;

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        jzgd = getIntent().getParcelableExtra("jzgd");
        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        if (user == null) {
            user = application.getUser();
        }
        if ("9999".equals(jzgd.getgId())) {
            mCunt.setVisibility(View.GONE);
            mWgt.setVisibility(View.GONE);
            mCun.setVisibility(View.GONE);
            mGrid.setVisibility(View.GONE);
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (!TextUtils.isEmpty(jzgd.getPicpath())) path = jzgd.getPicpath().split(",");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("建筑工地");
        if (type == 1) {
            mJcTitle.setText("消防安全检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
            //开票情况
            mNsqk1.setVisibility(View.GONE);
            mNsqkTable.setVisibility(View.GONE);
            //消防
            mQylb.setVisibility(View.GONE);
            mQylbt.setVisibility(View.GONE);
            //安全责任人、安全员
            mAqzrr.setVisibility(View.VISIBLE);
            mAqzrrt.setVisibility(View.VISIBLE);
            mAqzrrdh.setVisibility(View.VISIBLE);
            mAqzrrPhonet.setVisibility(View.VISIBLE);
            mAqy.setVisibility(View.VISIBLE);
            mAqyt.setVisibility(View.VISIBLE);
            mAqydh.setVisibility(View.VISIBLE);
            mAqyPhonet.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            mJcTitle.setText("安全生产检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
            //消防
            mQylb.setVisibility(View.VISIBLE);
            mQylbt.setVisibility(View.VISIBLE);
            //开票情况
            mNsqk1.setVisibility(View.GONE);
            mNsqkTable.setVisibility(View.GONE);

            //注冊資本
            mZczbt.setVisibility(View.GONE);
            mZczb.setVisibility(View.GONE);

            //护税责任人
            mJyfwt.setVisibility(View.GONE);
            mJyfwdht.setVisibility(View.GONE);
            mJyfw.setVisibility(View.GONE);
            mJyfwdh.setVisibility(View.GONE);
            //安全责任人、安全员
            mAqzrr.setVisibility(View.VISIBLE);
            mAqzrrt.setVisibility(View.VISIBLE);
            mAqzrrdh.setVisibility(View.VISIBLE);
            mAqzrrPhonet.setVisibility(View.VISIBLE);
            mAqy.setVisibility(View.VISIBLE);
            mAqyt.setVisibility(View.VISIBLE);
            mAqydh.setVisibility(View.VISIBLE);
            mAqyPhonet.setVisibility(View.VISIBLE);
        } else {
            tableLayout.setVisibility(View.GONE);
            mJcTitle.setVisibility(View.GONE);
            //消防
            mQylb.setVisibility(View.GONE);
            mQylbt.setVisibility(View.GONE);
            //开票情况
            mNsqk1.setVisibility(View.VISIBLE);
            mNsqkTable.setVisibility(View.VISIBLE);
            //安全责任人、安全员
            mAqzrr.setVisibility(View.GONE);
            mAqzrrt.setVisibility(View.GONE);
            mAqzrrdh.setVisibility(View.GONE);
            mAqzrrPhonet.setVisibility(View.GONE);
            mAqy.setVisibility(View.GONE);
            mAqyt.setVisibility(View.GONE);
            mAqydh.setVisibility(View.GONE);
            mAqyPhonet.setVisibility(View.GONE);
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
                intent.setClass(JzgdInfo.this, ShowImageActivity.class);
                startActivity(intent);
            }
        });


//        // 1.设置幕后item的缓存数目
//        viewPager.setOffscreenPageLimit(4);
//        // 2.设置页与页之间的间距
//        viewPager.setPageMargin(10);
//        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
//        container.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return viewPager.dispatchTouchEvent(event);
//            }
//        });
        if (path != null) {
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
//                        intent.putExtra("isLocal", false);
                            intent.putExtra("index", postion);
                            intent.setClass(JzgdInfo.this, ShowImageActivity.class);
                            startActivity(intent);
                        }
                    });
                    views.add(img);
                }
            }
        }
//        viewPager.setAdapter(new MyAdapter()); // 为viewpager设置adapter
//        viewPager.setOnPageChangeListener(this);// 设置监听器
    }

    public void initData() {
        if (!"9999".equals(jzgd.getgId())) {
//            loadData();
            mGrid.setText(gridId);
        }
        mName.setText(jzgd.getYzname());
        mFr.setText(jzgd.getFr());
        mFrdh.setText(jzgd.getFrdh());
        mJyfw.setText(jzgd.getHszrr());
        mJyfwdh.setText(jzgd.getHszrrdh());
        mDm.setText(jzgd.getSgf());
        mZrr.setText(jzgd.getWgzrr());
        mZrrdh.setText(jzgd.getWgzrrdh());
        mDw.setText(jzgd.getSddw());
        mQylb.setText(jzgd.getPbqk());
        mZczb.setText(jzgd.getZczb());
        mAqy.setText(jzgd.getAqy());
        mAqydh.setText(jzgd.getAqydh());
        mAqzrr.setText(jzgd.getAqzrr());
        mAqzrrdh.setText(jzgd.getAqzrrdh());
        mBz.setText(jzgd.getBz());
        mName_.setText(jzgd.getName());
        if (jzgd.getPicpath() != null) {
            picPath = jzgd.getPicpath();
            SuccinctProgress.showSuccinctProgress(JzgdInfo.this, "图片努力加载中···",
                    SuccinctProgress.THEME_ULTIMATE, false, true);
            new Thread(runnable1).start();
        }
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String[] ps = picPath.split(",");
            for (int i = 0; i < ps.length; i++) {
                if (!TextUtils.isEmpty(ps[i])) {
                    mPath.add(ps[i]);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", BitmapUtil.getNetBitmap(JzgdInfo.this, ConstantUtil
                            .FILE_DOWNLOAD_URL + ps[i]));
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


    /**
     * 获取村名
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(JzgdInfo.this, "数据请求中···", SuccinctProgress
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
                        mName.setText(jzgd.getYzname());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        mFr.setText(jzgd.getFr());
                        mFrdh.setText(jzgd.getFrdh());
                        mJyfw.setText(jzgd.getHszrr());
                        mJyfwdh.setText(jzgd.getHszrrdh());
                        mDm.setText(jzgd.getSgf());
                        mZrr.setText(jzgd.getWgzrr());
                        mZrrdh.setText(jzgd.getWgzrrdh());
                        mDw.setText(jzgd.getSddw());
                        mQylb.setText(jzgd.getPbqk());
                        mZczb.setText(jzgd.getZczb());
                        mAqy.setText(jzgd.getAqy());
                        mAqydh.setText(jzgd.getAqydh());
                        mAqzrr.setText(jzgd.getAqzrr());
                        mAqzrrdh.setText(jzgd.getAqzrrdh());
                        mBz.setText(jzgd.getBz());
                        mName_.setText(jzgd.getName());
                        if (jzgd.getPicpath() != null) {
                            picPath = jzgd.getPicpath();
                            SuccinctProgress.showSuccinctProgress(JzgdInfo.this, "图片努力加载中···",
                                    SuccinctProgress.THEME_ULTIMATE, false, true);
                            new Thread(runnable1).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JzgdInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JzgdInfo.this, R.string.load_fail);
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

    @Event(value = {R.id.iv_back, R.id.moth1, R.id.moth2, R.id.moth3, R.id.moth4, R.id.moth5, R
            .id.moth6, R.id.moth7, R.id.moth8, R.id.moth9, R.id.moth10, R.id.moth11, R.id
            .moth12, R.id.jd1, R.id.jd2, R.id.jd3, R.id.jd4}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(JzgdInfo.this, XfaqActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("cType", "1");
        intent.putExtra("pId", String.valueOf(jzgd.getId()));
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.moth1:
                intent.putExtra("currDate", "01");
                startActivity(intent);
                break;
            case R.id.moth2:
                intent.putExtra("currDate", "02");
                startActivity(intent);
                break;
            case R.id.moth3:
                intent.putExtra("currDate", "03");
                startActivity(intent);
                break;
            case R.id.moth4:
                intent.putExtra("currDate", "04");
                startActivity(intent);
                break;
            case R.id.moth5:
                intent.putExtra("currDate", "05");
                startActivity(intent);
                break;
            case R.id.moth6:
                intent.putExtra("currDate", "06");
                startActivity(intent);
                break;
            case R.id.moth7:
                intent.putExtra("currDate", "07");
                startActivity(intent);
                break;
            case R.id.moth8:
                intent.putExtra("currDate", "08");
                startActivity(intent);
                break;
            case R.id.moth9:
                intent.putExtra("currDate", "09");
                startActivity(intent);
                break;
            case R.id.moth10:
                intent.putExtra("currDate", "10");
                startActivity(intent);
                break;
            case R.id.moth11:
                intent.putExtra("currDate", "11");
                startActivity(intent);
                break;
            case R.id.moth12:
                intent.putExtra("currDate", "12");
                startActivity(intent);
                break;
            case R.id.jd1:
                if (jzgd != null) {
                    intent.setClass(this, KpqkInfo.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
                break;
            case R.id.jd2:
                if (jzgd != null) {
                    intent.setClass(this, KpqkInfo.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "2");
                    startActivity(intent);
                }
                break;
            case R.id.jd3:
                if (jzgd != null) {
                    intent.setClass(this, KpqkInfo.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "3");
                    startActivity(intent);
                }
                break;
            case R.id.jd4:
                if (jzgd != null) {
                    intent.setClass(this, KpqkInfo.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "4");
                    startActivity(intent);
                }
                break;
        }
    }
}
