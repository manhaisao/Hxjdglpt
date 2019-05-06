package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.xzz.hxjdglpt.model.Village;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_ls_grid_map)
public class LsGridMapActivity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    private Village village;
    public int lsType;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        village = (Village) getIntent().getExtras().getParcelable("village");
        lsType = getIntent().getExtras().getInt("lsType");
        initView();
        initData();
    }

   @Override
    public void initView() {
        super.initView();
        hXtitle.setText(village.getName());
    }

    @Override
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

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}
