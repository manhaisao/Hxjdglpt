package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据采集
 * Created by dbz on 2017/5/18.
 */

@ContentView(R.layout.aty_datacollection)
public class DataCollectionActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.info_jc)
    private LinearLayout rlBase;


    @ViewInject(R.id.info_qy)
    private LinearLayout rlCompany;
    @ViewInject(R.id.index_gt)
    private LinearLayout rlManage;
    @ViewInject(R.id.info_rk)
    private LinearLayout rlPeoper;
    @ViewInject(R.id.info_dy)
    private LinearLayout rlParty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(R.string.xxcj);
        initData();
        initView();
    }

    public void initView() {
    }

    public void initData() {
        initPower();
    }

    private List<String> ms = new ArrayList<>();

    private void initPower() {
        List<Menu> menus = application.getMenus();
        for (Menu m : menus) {
            ms.add(m.getMenuId());
        }
    }

    @Event(value = {R.id.iv_back, R.id.info_jc, R.id.info_qy, R.id.index_gt, R.id.info_rk, R.id
            .info_dy, R.id.index_aqsc, R.id.index_dj, R.id.index_fwzw, R.id.index_xshs, R.id
            .index_qygz, R.id.index_mz, R.id.index_jhsy, R.id.index_sfxz, R.id.index_cxgl, R.id
            .index_xfwd, R.id.index_xqxx, R.id.index_jly, R.id.index_ls_form}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.info_jc:
                intent.setClass(this, BaseInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.info_qy:
                intent.setClass(this, CompanyActivity.class);
                startActivity(intent);
                break;
            case R.id.index_gt:
                intent.setClass(this, ManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.info_rk:
                intent.setClass(this, PeoperActivity.class);
                startActivity(intent);
                break;
            case R.id.info_dy:
                intent.setClass(this, PartyActivity.class);
                startActivity(intent);
                break;
            case R.id.index_aqsc:
                if (ms.contains("100107")) {
//                    intent.setClass(this, AqscActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.aqsc));
                    intent.putExtra("type", 2);//十项工作中应急服务ID
                    intent.putExtra("isYjfw", "1");
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_dj:
                if (ms.contains("100107")) {
//                    intent.setClass(this, DjActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.dj));
                    intent.putExtra("type", 8);//十项工作中党建ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_fwzw:
                if (ms.contains("100107")) {
//                    intent.setClass(this, FwzfActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.fwzw));
                    intent.putExtra("type", 10);//十项工作中综合执法ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_xshs:
                if (ms.contains("100107")) {
//                    intent.setClass(this, XshsActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.xshs));
                    intent.putExtra("type", 7);//十项工作中协税护税ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_qygz:
                if (ms.contains("100107")) {
//                    intent.setClass(this, QygzActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.qygz));
                    intent.putExtra("type", 4);//十项工作中企业服务ID
                    intent.putExtra("isYjfw", "0");
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_mz:
                if (ms.contains("100107")) {
//                    intent.setClass(this, MzActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.mz));
                    intent.putExtra("type", 5);//十项工作中民政ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_jhsy:
                if (ms.contains("100107")) {
//                    intent.setClass(this, JhsyActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.jhsy));
                    intent.putExtra("type", 3);//十项工作中卫生健康ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_sfxz:
                if (ms.contains("100107")) {
//                    intent.setClass(this, SfxzActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.sfxz));
                    intent.putExtra("type", 6);//十项工作中政法综治ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_cxgl:
                if (ms.contains("100107")) {
//                    intent.setClass(this, CscxglActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.cscxgl));
                    intent.putExtra("type", 1);//十项工作中长效管理ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_xfwd:
                if (ms.contains("100107")) {
//                    intent.setClass(this, XfwdActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.xfwd));
                    intent.putExtra("type", 9);//十项工作中和谐社区ID
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_xqxx:
                if (ms.contains("100107")) {
//                    intent.setClass(this, PlotActivity.class);
//                    startActivity(intent);
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.xqgsq));
                    intent.putExtra("type", 0);
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_jly:
                if (ms.contains("100107")) {
                    intent.setClass(this, JlyActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
            case R.id.index_ls_form:
                //ls form
                if (ms.contains("100107")) {
                    intent.setClass(this, XxcjVillageListActivity.class);
                    intent.putExtra("title", getString(R.string.ls));
                    intent.putExtra("type", 11); //劳动保障
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, R.string.no_power);
                }
                break;
        }
    }


}
