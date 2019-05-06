package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.fragment.CjFragment;
import com.xzz.hxjdglpt.fragment.CyFragment;
import com.xzz.hxjdglpt.fragment.GyFragment;
import com.xzz.hxjdglpt.fragment.HbFragment;
import com.xzz.hxjdglpt.fragment.HqFragment;
import com.xzz.hxjdglpt.fragment.HtcFragment;
import com.xzz.hxjdglpt.fragment.HxjFragment;
import com.xzz.hxjdglpt.fragment.LsFragment;
import com.xzz.hxjdglpt.fragment.WsFragment;
import com.xzz.hxjdglpt.fragment.XlFragment;
import com.xzz.hxjdglpt.fragment.ZhFragment;
import com.xzz.hxjdglpt.fragment.ZqxFragment;
import com.xzz.hxjdglpt.fragment.ZtsFragment;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.model.ls.Shbx;
import com.xzz.hxjdglpt.model.ls.VillageAll;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 劳动保障-村
 */
@ContentView(R.layout.activity_ls_village)
public class LsVillageActivity extends BaseActivity {

   /* @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;*/

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.tv_yanglao_num)
    private TextView tvYanglaoNum;
    @ViewInject(R.id.tv_yanglao_fzr)
    private TextView tvYanglaoFzr;
    @ViewInject(R.id.tv_yanglao_fzr_phone)
    private TextView tvYanglaoFzrPhone;
    @ViewInject(R.id.tv_yanglao_jbr)
    private TextView tvYanglaoJbr;
    @ViewInject(R.id.tv_yanglao_jbr_phone)
    private TextView tvYanglaoJbrPhone;
    @ViewInject(R.id.tv_yiliao_num)
    private TextView tvYiliaoNum;
    @ViewInject(R.id.tv_yiliao_fzr)
    private TextView tvYiliaoFzr;
    @ViewInject(R.id.tv_yiliao_fzr_phone)
    private TextView tvYiliaoFzrPhone;
    @ViewInject(R.id.tv_yiliao_jbr)
    private TextView tvYiliaoJbr;
    @ViewInject(R.id.tv_yiliao_jbr_phone)
    private TextView tvYiliaoJbrPhone;

    private Village village;
    public int lsType = 98;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @ViewInject(R.id.sxgz_gzdt_btn)
    private Button gzdtBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        village = (Village) getIntent().getExtras().getParcelable("village");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(village.getName());
    }

    @Override
    public void initData() {
        super.initData();
        try {
            loadData();
            initGrid();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.iv_back, R.id.sxgz_gzdt_btn}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.sxgz_gzdt_btn:
                Intent intent = new Intent();
                intent.setClass(this, GzdtVillageActivity.class);
                intent.putExtra("vId", village.getId());
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取村信息
     */
    private void loadData() throws UnsupportedEncodingException {
        SuccinctProgress.showSuccinctProgress(LsVillageActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/xshs/queryBycun?vname=" + URLEncoder.encode(village.getName(), "utf-8");
        VolleyRequest.RequestGet(this, url, "queryBycun", new VolleyListenerInterface
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
                        if (result.getJSONObject("data") != null) {
                            VillageAll villageAll = (VillageAll) gson.fromJson(result.getJSONObject("data").toString
                                    (), VillageAll.class);

                            int yanglaos = result.isNull("yanglaos") ? 0 : result.getInt("yanglaos");
                            int yiliaos = result.isNull("yiliaos") ? 0 : result.getInt("yiliaos");

                        /*int yanglaos = result.getInt("yanglaos");
                        int yiliaos = result.getInt("yiliaos");*/

                            //绑定
                            tvYanglaoNum.setText(String.valueOf(yanglaos));
                            tvYanglaoFzr.setText(villageAll.getFzr());
                            tvYanglaoFzrPhone.setText(villageAll.getFzrphone());
                            tvYanglaoJbr.setText(villageAll.getJbr());
                            tvYanglaoJbrPhone.setText(villageAll.getJbrphone());

                            tvYiliaoNum.setText(String.valueOf(yiliaos));
                            tvYiliaoFzr.setText(villageAll.getFzr1());
                            tvYiliaoFzrPhone.setText(villageAll.getFzrphone1());
                            tvYiliaoJbr.setText(villageAll.getJbr1());
                            tvYiliaoJbrPhone.setText(villageAll.getJbrphone1());
                        }

                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LsVillageActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LsVillageActivity.this, R.string.load_fail);
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

    /**
     * 初始化村
     */
    public void initGrid() {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        switch (village.getId()) {
            case "1":
                transaction.add(R.id.main_content, new LsFragment());
                break;
            case "2":
                transaction.add(R.id.main_content, new ZtsFragment());
                break;
            case "3":
                transaction.add(R.id.main_content, new HxjFragment());
                break;
            case "4":
                transaction.add(R.id.main_content, new XlFragment());
                break;
            case "5":
                transaction.add(R.id.main_content, new CjFragment());
                break;
            case "6":
                transaction.add(R.id.main_content, new HbFragment());
                break;
            case "7":
                transaction.add(R.id.main_content, new GyFragment());
                break;
            case "8":
                transaction.add(R.id.main_content, new HtcFragment());
                break;
            case "9":
                transaction.add(R.id.main_content, new CyFragment());
                break;
            case "10":
                transaction.add(R.id.main_content, new ZhFragment());
                break;
            case "11":
                transaction.add(R.id.main_content, new WsFragment());
                break;
            case "12":
                transaction.add(R.id.main_content, new HqFragment());
                break;
            case "13":
                transaction.add(R.id.main_content, new ZqxFragment());
                break;
        }
        transaction.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryBycun");
    }
}
