package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.VillageAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
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
import java.util.Map;

/**
 * Created by dbz on 2017/6/5.
 */
@ContentView(R.layout.aty_village_map)
public class VillageMapActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private User user;
    private BaseApplication application;

    private List<Village> villages = new ArrayList<>();

    @ViewInject(R.id.map_village_list)
    private ListView listview;

    private VillageAdapter villageAdapter;

    @ViewInject(R.id.map_village_list_map)
    private FrameLayout mMapLay;

    @ViewInject(R.id.hx_btn_right)
    private ImageView btnRight;

    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(R.string.ckdt);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageDrawable(getResources().getDrawable(R.mipmap.exchange));
    }

    public void initData() {
        villageAdapter = new VillageAdapter(this, villages);
        listview.setAdapter(villageAdapter);
        listview.setOnItemClickListener(this);
        request();
    }

    private void request() {
        SuccinctProgress.showSuccinctProgress(VillageMapActivity.this, "请求数据中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "village_queryList", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Village> v = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Village>>() {
                        }.getType());
                        villages.addAll(v);
                        villageAdapter.notifyDataSetChanged();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(VillageMapActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(VillageMapActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    SuccinctProgress.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Event(value = {R.id.hx_btn_right, R.id.iv_back, R.id.village_01, R.id.village_02, R.id
            .village_03, R.id.village_04, R.id.village_05, R.id.village_06, R.id.village_07, R.id
            .village_08, R.id.village_09, R.id.village_10, R.id.village_11, R.id.village_12, R.id
            .village_13}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Village village = null;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.hx_btn_right:
//                if ("网格地图".equals(btnRight.getText().toString())) {
//                    btnRight.setText("村居列表");
//                    mMapLay.setVisibility(View.GONE);
//                    listview.setVisibility(View.VISIBLE);
//                } else if ("村居列表".equals(btnRight.getText().toString())) {
//                    btnRight.setText("网格地图");
//                    mMapLay.setVisibility(View.VISIBLE);
//                    listview.setVisibility(View.GONE);
//                }
                int jd = 360;
                if (flag) {
                    mMapLay.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    tvTitle.setText(getText(R.string.cjlb));
                    jd = -360;
                } else {
                    mMapLay.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    tvTitle.setText(getText(R.string.ckdt));
                    jd = 360;
                }
                RotateAnimation rotateAnim = new RotateAnimation(0, jd, Animation
                        .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(rotateAnim);
                set.setFillAfter(true);
                set.setDuration(300);
                btnRight.startAnimation(set);
                flag = !flag;
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.village_01:
                for (Village v1 : villages) {
                    if ("9".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_02:
                for (Village v1 : villages) {
                    if ("8".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_03:
                for (Village v1 : villages) {
                    if ("7".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_04:
                for (Village v1 : villages) {
                    if ("11".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_05:
                for (Village v1 : villages) {
                    if ("4".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_06:
                for (Village v1 : villages) {
                    if ("3".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_07:
                for (Village v1 : villages) {
                    if ("6".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_08:
                for (Village v1 : villages) {
                    if ("2".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_09:
                for (Village v1 : villages) {
                    if ("5".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_10:
                for (Village v1 : villages) {
                    if ("1".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_11:
                for (Village v1 : villages) {
                    if ("12".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_12:
                for (Village v1 : villages) {
                    if ("10".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.village_13:
                for (Village v1 : villages) {
                    if ("13".equals(v1.getId())) {
                        village = v1;
                        break;
                    }
                }
                intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("village_queryList");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Village vv = villages.get(position);
        Intent intent = new Intent();
        intent.setClass(VillageMapActivity.this, MapdetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("village", vv);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
