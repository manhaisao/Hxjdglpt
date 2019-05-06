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
import com.xzz.hxjdglpt.model.Cjry;
import com.xzz.hxjdglpt.model.Company;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 企业基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_company_info)
public class CompanyInfo extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Company company;
    private String gridId;
    @ViewInject(R.id.company_name)
    private TextView mName;
    @ViewInject(R.id.company_cun)
    private TextView mCun;
    @ViewInject(R.id.company_grid)
    private TextView mGrid;
    @ViewInject(R.id.company_qylx)
    private TextView mQylx;
    @ViewInject(R.id.company_fr)
    private TextView mFr;
    @ViewInject(R.id.company_lxdh)
    private TextView mLxdh;
    @ViewInject(R.id.company_qylb)
    private TextView mQylb;
    @ViewInject(R.id.company_t_zczb)
    private TextView mZczbt;//注册资本
    @ViewInject(R.id.company_zczb)
    private TextView mZczb;

    @ViewInject(R.id.company_zcsj)
    private TextView mZcsj;
    @ViewInject(R.id.company_t_zgs)
    private TextView mZgst;
    @ViewInject(R.id.company_zgs)
    private TextView mZgs;
    @ViewInject(R.id.company_rjgz)
    private TextView mRjgz;
    @ViewInject(R.id.company_isns)
    private TextView mIsns;
    @ViewInject(R.id.company_jyaddress)
    private TextView mJyAddress;
    @ViewInject(R.id.company_jyfw)
    private TextView mJyfw;
    @ViewInject(R.id.company_kpqk)
    private TextView mKpqk;
    @ViewInject(R.id.company_nsqk)
    private TextView mNsqk;
    @ViewInject(R.id.company_aqzrr)
    private TextView mAqzrr;
    @ViewInject(R.id.company_aqzrrPhone)
    private TextView mAqzrrPhone;
    @ViewInject(R.id.company_aqy)
    private TextView mAqy;
    @ViewInject(R.id.company_aqyPhone)
    private TextView mAqyPhone;
    @ViewInject(R.id.company_bz)
    private TextView mBz;


    @ViewInject(R.id.company_t_aqzrr)
    private TextView mAqzrrt;
    @ViewInject(R.id.company_t_aqzrrPhone)
    private TextView mAqzrrPhonet;
    @ViewInject(R.id.company_t_aqy)
    private TextView mAqyt;
    @ViewInject(R.id.company_t_aqyPhone)
    private TextView mAqyPhonet;

    @ViewInject(R.id.company_t_cun)
    private TextView mCunt;
    @ViewInject(R.id.company_t_grid)
    private TextView mWgt;

    @ViewInject(R.id.company_dm)
    private TextView mDm;
    @ViewInject(R.id.company_wgzrr)
    private TextView mZrr;
    @ViewInject(R.id.company_wgzrrdh)
    private TextView mZrrdh;
    @ViewInject(R.id.company_danwei)
    private TextView mDw;

    @ViewInject(R.id.company_xfsnum)
    private TextView mXfsNum;
    @ViewInject(R.id.company_mhqnum)
    private TextView mMhqNum;
    @ViewInject(R.id.company_jb)
    private TextView mJb;
    @ViewInject(R.id.company_t_xfsnum)
    private TextView mXfsNumt;
    @ViewInject(R.id.company_t_mhqnum)
    private TextView mMhqNumt;
    @ViewInject(R.id.company_t_jb)
    private TextView mJbt;
    @ViewInject(R.id.company_nstj)
    private TextView mNsqk1;
    @ViewInject(R.id.company_nstj_table)
    private TableLayout mNsqkTable;


    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();
    @ViewInject(R.id.container)
    private LinearLayout container;

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

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        company = getIntent().getParcelableExtra("company");
        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        if (user == null) {
            user = application.getUser();
        }
        if ("9999".equals(company.getGridId())) {
            mCunt.setVisibility(View.GONE);
            mWgt.setVisibility(View.GONE);
            mCun.setVisibility(View.GONE);
            mGrid.setVisibility(View.GONE);
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (!TextUtils.isEmpty(company.getPicpath())) path = company.getPicpath().split(",");
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(R.string.qy);
        if (type == 1) {
            mJcTitle.setText("消防安全检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
            //纳税情况
            mNsqk1.setVisibility(View.GONE);
            mNsqkTable.setVisibility(View.GONE);
            //消防
            mXfsNum.setVisibility(View.VISIBLE);
            mMhqNum.setVisibility(View.VISIBLE);
            mJb.setVisibility(View.VISIBLE);
            mXfsNumt.setVisibility(View.VISIBLE);
            mMhqNumt.setVisibility(View.VISIBLE);
            mJbt.setVisibility(View.VISIBLE);

            //注冊資本
            mZczbt.setVisibility(View.GONE);
            mZczb.setVisibility(View.GONE);
            //员工数
            mZgst.setVisibility(View.GONE);
            mZgs.setVisibility(View.GONE);

            //安全责任人、安全员
            mAqzrr.setVisibility(View.VISIBLE);
            mAqzrrt.setVisibility(View.VISIBLE);
            mAqzrrPhone.setVisibility(View.VISIBLE);
            mAqzrrPhonet.setVisibility(View.VISIBLE);
            mAqy.setVisibility(View.VISIBLE);
            mAqyt.setVisibility(View.VISIBLE);
            mAqyPhone.setVisibility(View.VISIBLE);
            mAqyPhonet.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            mJcTitle.setText("安全生产检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
            //消防
            mXfsNum.setVisibility(View.GONE);
            mMhqNum.setVisibility(View.GONE);
            mJb.setVisibility(View.GONE);
            mXfsNumt.setVisibility(View.GONE);
            mMhqNumt.setVisibility(View.GONE);
            mJbt.setVisibility(View.GONE);

            //注冊資本
            mZczbt.setVisibility(View.GONE);
            mZczb.setVisibility(View.GONE);
            //员工数
            mZgst.setVisibility(View.GONE);
            mZgs.setVisibility(View.GONE);

            //纳税情况
            mNsqk1.setVisibility(View.GONE);
            mNsqkTable.setVisibility(View.GONE);
            //安全责任人、安全员
            mAqzrr.setVisibility(View.VISIBLE);
            mAqzrrt.setVisibility(View.VISIBLE);
            mAqzrrPhone.setVisibility(View.VISIBLE);
            mAqzrrPhonet.setVisibility(View.VISIBLE);
            mAqy.setVisibility(View.VISIBLE);
            mAqyt.setVisibility(View.VISIBLE);
            mAqyPhone.setVisibility(View.VISIBLE);
            mAqyPhonet.setVisibility(View.VISIBLE);
        } else {
            tableLayout.setVisibility(View.GONE);
            mJcTitle.setVisibility(View.GONE);
            //消防
            mXfsNum.setVisibility(View.GONE);
            mMhqNum.setVisibility(View.GONE);
            mJb.setVisibility(View.GONE);
            mXfsNumt.setVisibility(View.GONE);
            mMhqNumt.setVisibility(View.GONE);
            mJbt.setVisibility(View.GONE);
            //纳税情况
            mNsqk1.setVisibility(View.VISIBLE);
            mNsqkTable.setVisibility(View.VISIBLE);
            //安全责任人、安全员
            mAqzrr.setVisibility(View.GONE);
            mAqzrrt.setVisibility(View.GONE);
            mAqzrrPhone.setVisibility(View.GONE);
            mAqzrrPhonet.setVisibility(View.GONE);
            mAqy.setVisibility(View.GONE);
            mAqyt.setVisibility(View.GONE);
            mAqyPhone.setVisibility(View.GONE);
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
                intent.setClass(CompanyInfo.this, ShowImageActivity.class);
                startActivity(intent);
            }
        });


        if (company.getPicpath() == null || TextUtils.isEmpty(company.getPicpath())) {
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
                            intent.setClass(CompanyInfo.this, ShowImageActivity.class);
                            startActivity(intent);
                        }
                    });
                    views.add(img);
                }
            }
        }
        viewPager.setAdapter(new MyAdapter()); // 为viewpager设置adapter
        viewPager.setOnPageChangeListener(this);// 设置监听器
    }

    public void initData() {
        if (!"9999".equals(company.getGridId())) {
            loadData();
        } else {
            mName.setText(company.getName());
            mIsns.setText(company.getIsnas());
            mZgs.setText(String.valueOf(company.getpNum()));
            mFr.setText(company.getLinkman());
            mLxdh.setText(company.getLinkmanPhone());
            mJyAddress.setText(company.getAddress());
            mJyfw.setText(company.getArea());
            mKpqk.setText(company.getKpqk());
            mNsqk.setText(company.getNsqk());
            if ("01".equals(company.getComtype())) {
                mQylx.setText("列统企业");
            } else if ("02".equals(company.getComtype())) {
                mQylx.setText("民营企业");
            } else if ("03".equals(company.getComtype())) {
                mQylx.setText("集体企业");
            } else if ("04".equals(company.getComtype())) {
                mQylx.setText("总部企业");
            }
            mXfsNum.setText(company.getXfqnum());
            mMhqNum.setText(company.getMhqnum());
            mJb.setText(company.getQingk());
//                        else if ("04".equals(company.getComtype())) {
//                            mQylx.setText("其他");
//                        }
            mDm.setText(company.getXinyong());
            mZrr.setText(company.getGridzrr());
            mZrrdh.setText(company.getGridzrrdh());
            mDw.setText(company.getDanwie());
            mQylb.setText(company.getType());
            mRjgz.setText(company.getGongzi());
            mZcsj.setText(company.getZcsj());
            mZczb.setText(company.getZczb());
            mAqy.setText(company.getAqy());
            mAqyPhone.setText(company.getAqyPhone());
            mAqzrr.setText(company.getAqzrr());
            mAqzrrPhone.setText(company.getAqzrrPhone());
            mBz.setText(company.getBz());
            if (company.getPicpath() != null) {
                picPath = company.getPicpath();
                SuccinctProgress.showSuccinctProgress(CompanyInfo.this, "图片努力加载中···",
                        SuccinctProgress.THEME_ULTIMATE, false, true);
                new Thread(runnable1).start();
            }
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
                    map.put("itemImage", BitmapUtil.getNetBitmap(CompanyInfo.this, ConstantUtil
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
        SuccinctProgress.showSuccinctProgress(CompanyInfo.this, "数据请求中···", SuccinctProgress
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
                        mName.setText(company.getName());
                        mCun.setText(v.getName());
                        mGrid.setText(gridId);
                        mIsns.setText(company.getIsnas());
                        mZgs.setText(String.valueOf(company.getpNum()));
                        mFr.setText(company.getLinkman());
                        mLxdh.setText(company.getLinkmanPhone());
                        mJyAddress.setText(company.getAddress());
                        mJyfw.setText(company.getArea());
                        mKpqk.setText(company.getKpqk());
                        mNsqk.setText(company.getNsqk());
                        if ("01".equals(company.getComtype())) {
                            mQylx.setText("列统企业");
                        } else if ("02".equals(company.getComtype())) {
                            mQylx.setText("民营企业");
                        } else if ("03".equals(company.getComtype())) {
                            mQylx.setText("集体企业");
                        } else if ("04".equals(company.getComtype())) {
                            mQylx.setText("总部企业");
                        }
                        mXfsNum.setText(company.getXfqnum());
                        mMhqNum.setText(company.getMhqnum());
                        mJb.setText(company.getQingk());
//                        else if ("04".equals(company.getComtype())) {
//                            mQylx.setText("其他");
//                        }
                        mDm.setText(company.getXinyong());
                        mZrr.setText(company.getGridzrr());
                        mZrrdh.setText(company.getGridzrrdh());
                        mDw.setText(company.getDanwie());
                        mQylb.setText(company.getType());
                        mRjgz.setText(company.getGongzi());
                        mZcsj.setText(company.getZcsj());
                        mZczb.setText(company.getZczb());
                        mAqy.setText(company.getAqy());
                        mAqyPhone.setText(company.getAqyPhone());
                        mAqzrr.setText(company.getAqzrr());
                        mAqzrrPhone.setText(company.getAqzrrPhone());
                        mBz.setText(company.getBz());
                        if (company.getPicpath() != null) {
                            picPath = company.getPicpath();
                            SuccinctProgress.showSuccinctProgress(CompanyInfo.this, "图片努力加载中···",
                                    SuccinctProgress.THEME_ULTIMATE, false, true);
                            new Thread(runnable1).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CompanyInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CompanyInfo.this, R.string.load_fail);
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
        intent.setClass(CompanyInfo.this, XfaqActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("pId", String.valueOf(company.getId()));
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
                if (company != null) {
                    intent.setClass(this, NsqkInfo.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
                break;
            case R.id.jd2:
                if (company != null) {
                    intent.setClass(this, NsqkInfo.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "2");
                    startActivity(intent);
                }
                break;
            case R.id.jd3:
                if (company != null) {
                    intent.setClass(this, NsqkInfo.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "3");
                    startActivity(intent);
                }
                break;
            case R.id.jd4:
                if (company != null) {
                    intent.setClass(this, NsqkInfo.class);
                    intent.putExtra("comType", company.getComtype());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "4");
                    startActivity(intent);
                }
                break;
        }
    }
}
