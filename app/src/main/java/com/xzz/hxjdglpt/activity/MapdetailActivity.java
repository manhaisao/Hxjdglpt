package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 地图详情
 * Created by dbz on 2017/7/13.
 */
@ContentView(R.layout.aty_map_detail)
public class MapdetailActivity extends BaseActivity {

    private Village village;

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private User user;
    private BaseApplication application;
    private FragmentManager manager;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        village = getIntent().getParcelableExtra("village");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(village.getName());
    }

    public void initData() {
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

    @Event(value = {R.id.map_title_gk, R.id.iv_back, R.id.map_title_jj, R.id.map_title_jg}, type
            = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.map_title_gk:
                intent.setClass(MapdetailActivity.this, VillageIntroductionActivity.class);
                bundle.putParcelable("village", village);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.map_title_jj:
                intent.setClass(MapdetailActivity.this, CjjjActivity.class);
                intent.putExtra("type", village.getId());
                startActivity(intent);
                break;
            case R.id.map_title_jg:
                intent.setClass(MapdetailActivity.this, ZzjgActivity.class);
                intent.putExtra("type", village.getId());
                startActivity(intent);
                break;
        }
    }
}
