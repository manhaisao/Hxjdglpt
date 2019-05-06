package com.xzz.hxjdglpt.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.GridFunctionActivity;
import com.xzz.hxjdglpt.activity.GridSxgzDetailActivity;
import com.xzz.hxjdglpt.activity.LsInsuranceVillageActivity;
import com.xzz.hxjdglpt.activity.LsInsuranceVillageSecondaryActivity;
import com.xzz.hxjdglpt.activity.LsMedicalVillageActivity;
import com.xzz.hxjdglpt.activity.LsMedicalVillageSecondaryActivity;
import com.xzz.hxjdglpt.activity.LsVillageActivity;
import com.xzz.hxjdglpt.activity.LsVillageSecondaryActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.activity.VillageSxgzActivity;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;


/**
 * 河下居村地图
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.fragment_hxj)
public class HxjFragment extends Fragment {

    private BaseApplication application;

    private int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initView();
        initData();
        return view;
    }


    public void initView() {
        if (getActivity() instanceof VillageSxgzActivity) {
            type = ((VillageSxgzActivity) getActivity()).type;
        } else if (getActivity() instanceof LsVillageActivity) {
            type = ((LsVillageActivity) getActivity()).lsType;
        } else if (getActivity() instanceof LsInsuranceVillageActivity) {
            type = ((LsInsuranceVillageActivity) getActivity()).lsType;
        } else if (getActivity() instanceof LsMedicalVillageActivity) {
            type = ((LsMedicalVillageActivity) getActivity()).lsType;
        }
    }

    public void initData() {
        application = (BaseApplication) getActivity().getApplication();
    }

    @Event(value = {R.id.hxj_grid001, R.id.hxj_grid002, R.id.hxj_grid003, R.id.hxj_grid004, R.id
            .hxj_grid005, R.id.hxj_grid006, R.id.hxj_grid007, R.id.hxj_grid008, R.id.hxj_grid009,
            R.id.hxj_grid010, R.id.hxj_grid011, R.id.hxj_grid012}, type = View.OnClickListener
            .class)
    private void onClick(View v) {
        String gId = "";
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.hxj_grid001:
                gId = "00001";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid002:
                gId = "00002";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid003:
                gId = "00003";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid004:
                gId = "00004";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid005:
                gId = "00005";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid006:
                gId = "00006";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid007:
                gId = "00007";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid008:
                gId = "00008";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid009:
                gId = "00009";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid010:
                gId = "00010";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid011:
                gId = "00011";
                LogUtil.i(gId);
                break;
            case R.id.hxj_grid012:
                gId = "00012";
                LogUtil.i(gId);
                break;
        }
//        if (application.getGridIds().contains(gId)) {
            if (type == 0) {
                intent.setClass(getActivity(), GridFunctionActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
            }  else if (type == 98) {
                intent.setClass(getActivity(), LsVillageSecondaryActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
            } else if (type == 99) {
                intent.setClass(getActivity(), LsInsuranceVillageSecondaryActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
            } else if (type == 100) {
                intent.setClass(getActivity(), LsMedicalVillageSecondaryActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
            }else {
                intent.setClass(getActivity(), GridSxgzDetailActivity.class);
                intent.putExtra("gridId", gId);
                intent.putExtra("type", type);
                startActivity(intent);
            }
//        } else {
//            ToastUtil.show(getActivity(), R.string.no_power);
//        }
    }

}
