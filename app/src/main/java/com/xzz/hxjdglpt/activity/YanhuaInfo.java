package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Roupidouzhi;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.Yanhua;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
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
 * 烟花爆竹经营商基本信息
 * Created by dbz on 2017/6/30.
 */
@ContentView(R.layout.aty_yanhua_info)
public class YanhuaInfo extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    private User user;
    private Yanhua yanhua;
    private String gridId;
    @ViewInject(R.id.company_name)
    private TextView mName;
    @ViewInject(R.id.company_cun)
    private TextView mCun;
    @ViewInject(R.id.company_grid)
    private TextView mGrid;
    @ViewInject(R.id.company_qylx)
    private TextView mQylx;//类型
    @ViewInject(R.id.company_zcsj)
    private TextView mZcsj;
    @ViewInject(R.id.company_jydz)
    private TextView mNsqk;//经营地址
    @ViewInject(R.id.company_bz)
    private TextView mBz;

    @ViewInject(R.id.company_kcyj)
    private TextView mKcyj;
    @ViewInject(R.id.company_pfqk)
    private TextView mPfqk;


    @ViewInject(R.id.company_dm)
    private TextView mDm;//经营者
    @ViewInject(R.id.company_wgzrr)
    private TextView mZrr;
    @ViewInject(R.id.company_wgzrrdh)
    private TextView mZrrdh;
    @ViewInject(R.id.company_danwei)
    private TextView mDw;

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
        yanhua = getIntent().getParcelableExtra("yanhua");
        gridId = getIntent().getStringExtra("gridId");
        type = getIntent().getIntExtra("type", 0);
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("烟花爆竹经营商");
        if (type == 1) {
            mJcTitle.setText("消防安全检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            mJcTitle.setText("安全生产检查情况统计");
            tableLayout.setVisibility(View.VISIBLE);
            mJcTitle.setVisibility(View.VISIBLE);
        } else {
            tableLayout.setVisibility(View.GONE);
            mJcTitle.setVisibility(View.GONE);
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
                intent.setClass(YanhuaInfo.this, ShowImageActivity.class);
                startActivity(intent);
            }
        });


    }

    public void initData() {
//            loadData();
        mKcyj.setText(yanhua.getYijian());
        mPfqk.setText(yanhua.getPifu());
        mName.setText(yanhua.getName());
        mGrid.setText(yanhua.getGridid());
        mQylx.setText(yanhua.getDengji());
        mZcsj.setText(yanhua.getTime());
        mNsqk.setText(yanhua.getAddress());
        mDm.setText(yanhua.getJyz());
        mZrr.setText(yanhua.getGridzrr());
        mZrrdh.setText(yanhua.getZrrphone());
        mDw.setText(yanhua.getDanwei());
        mBz.setText(yanhua.getBz());
        if (yanhua.getFilepath() != null) {
            picPath = yanhua.getFilepath();
            SuccinctProgress.showSuccinctProgress(YanhuaInfo.this, "图片努力加载中···",
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
                    map.put("itemImage", BitmapUtil.getNetBitmap(YanhuaInfo.this, ConstantUtil
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
        SuccinctProgress.showSuccinctProgress(YanhuaInfo.this, "数据请求中···", SuccinctProgress
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
                        mCun.setText(v.getName());
                        mKcyj.setText(yanhua.getYijian());
                        mPfqk.setText(yanhua.getPifu());
                        mName.setText(yanhua.getName());
                        mGrid.setText(yanhua.getGridid());
                        mQylx.setText(yanhua.getDengji());
                        mZcsj.setText(yanhua.getTime());
                        mNsqk.setText(yanhua.getAddress());
                        mDm.setText(yanhua.getJyz());
                        mZrr.setText(yanhua.getGridzrr());
                        mZrrdh.setText(yanhua.getZrrphone());
                        mDw.setText(yanhua.getDanwei());
                        mBz.setText(yanhua.getBz());
                        if (yanhua.getFilepath() != null) {
                            picPath = yanhua.getFilepath();
                            SuccinctProgress.showSuccinctProgress(YanhuaInfo.this, "图片努力加载中···",
                                    SuccinctProgress.THEME_ULTIMATE, false, true);
                            new Thread(runnable1).start();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(YanhuaInfo.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(YanhuaInfo.this, R.string.load_fail);
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
            .moth12}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(YanhuaInfo.this, XfaqActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("cType", "3");
        intent.putExtra("pId", String.valueOf(yanhua.getId()));
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
        }
    }
}
