package com.xzz.hxjdglpt.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xzz.hxjdglpt.fragment.PartyBuildingFragment;
import com.xzz.hxjdglpt.model.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * Created by dbz on 2017/5/23.
 */

@ContentView(R.layout.aty_party_building)
public class PartyBuildingActivity extends BaseActivity {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private String tabNames[] = new String[4];

    @ViewInject(R.id.partybuilding_rg)
    private RadioGroup rgChannel;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private PartyBuildingFragment partyBuildingFragment_1;
    private PartyBuildingFragment partyBuildingFragment_2;
    private PartyBuildingFragment partyBuildingFragment_3;
    private PartyBuildingFragment partyBuildingFragment_4;

    private User user;

    public int tabflag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        tabNames = getResources().getStringArray(R.array.building_info);
        initView();
        initData();
    }


    public void initView() {
        tvTitle.setText(R.string.djxx);
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

    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }


    @Override
    public void onStop() {
        super.onStop();
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
                if (partyBuildingFragment_1 == null) {
                    partyBuildingFragment_1 = new PartyBuildingFragment();
                    transaction.add(R.id.main_content, partyBuildingFragment_1);
                } else {
                    transaction.show(partyBuildingFragment_1);
                }
                transaction.commit();
                break;
            case 1:
                hideFragment();
                if (partyBuildingFragment_2 == null) {
                    partyBuildingFragment_2 = new PartyBuildingFragment();
                    transaction.add(R.id.main_content, partyBuildingFragment_2);
                } else {
                    transaction.show(partyBuildingFragment_2);
                }
                transaction.commit();
                break;
            case 2:
                hideFragment();
                if (partyBuildingFragment_3 == null) {
                    partyBuildingFragment_3 = new PartyBuildingFragment();
                    transaction.add(R.id.main_content, partyBuildingFragment_3);
                } else {
                    transaction.show(partyBuildingFragment_3);
                }
                transaction.commit();
                break;
            case 3:
                hideFragment();
                if (partyBuildingFragment_4 == null) {
                    partyBuildingFragment_4 = new PartyBuildingFragment();
                    transaction.add(R.id.main_content, partyBuildingFragment_4);
                } else {
                    transaction.show(partyBuildingFragment_4);
                }
                transaction.commit();
                break;
        }
    }

    private void hideFragment() {
        if (partyBuildingFragment_1 != null) {
            transaction.hide(partyBuildingFragment_1);
        }
        if (partyBuildingFragment_2 != null) {
            transaction.hide(partyBuildingFragment_2);
        }
        if (partyBuildingFragment_3 != null) {
            transaction.hide(partyBuildingFragment_3);
        }
        if (partyBuildingFragment_4 != null) {
            transaction.hide(partyBuildingFragment_4);
        }
    }
}
