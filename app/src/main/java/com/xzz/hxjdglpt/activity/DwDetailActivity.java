package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.ArrayColumn;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.fragment.DwCunFragment;
import com.xzz.hxjdglpt.fragment.DwTypeFragment;
import com.xzz.hxjdglpt.fragment.PartyBuildingFragment;
import com.xzz.hxjdglpt.model.DwDetail;
import com.xzz.hxjdglpt.model.DwDetailAdr;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019\3\25 0025.
 */

@ContentView(R.layout.aty_dw_detail)
public class DwDetailActivity extends BaseActivity {

    //标题
    @ViewInject(R.id.hx_title)
    TextView hx_title;

    //右侧菜单
    @ViewInject(R.id.hx_btn_right)
    ImageView hx_btn_right;

    private String tabNames[] = new String[4];

    @ViewInject(R.id.partybuilding_rg)
    private RadioGroup rgChannel;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private DwTypeFragment dwTypeFragment;
    private DwCunFragment dwCunFragment;

    private User user;

    public int tabflag = 0;
    private BaseApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        tabNames = getResources().getStringArray(R.array.dw_info);
        initView();
    }

    public void initView() {
        hx_title.setText("点位详情");
        hx_btn_right.setVisibility(View.GONE);
        rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tabflag = checkedId + 1;
                onItemSelected(checkedId);
            }
        });
        initTab();//动态产生RadioButton
        rgChannel.check(0);
    }

    @Event(value = {R.id.iv_back}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
    private void initTab() {
        for (int i = 0; i < tabNames.length; i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(this).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(tabNames[i]);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams
                    .WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            rb.setLayoutParams(params);
            rgChannel.addView(rb, i);
        }
    }

    public void onItemSelected(int tabId) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        switch (tabId) {
            case 0:
                hideFragment();
                if (dwTypeFragment == null) {
                    dwTypeFragment = new DwTypeFragment();
                    transaction.add(R.id.main_content, dwTypeFragment);
                } else {
                    transaction.show(dwTypeFragment);
                }
                transaction.commit();
                break;
            case 1:
                hideFragment();
                if (dwCunFragment == null) {
                    dwCunFragment = new DwCunFragment();
                    transaction.add(R.id.main_content, dwCunFragment);
                } else {
                    transaction.show(dwCunFragment);
                }
                transaction.commit();
                break;
        }
    }

    private void hideFragment() {
        if (dwTypeFragment != null) {
            transaction.hide(dwTypeFragment);
        }
        if (dwCunFragment != null) {
            transaction.hide(dwCunFragment);
        }

    }

}
