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
 * 螺蛳街村地图
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.fragment_ls)
public class LsFragment extends Fragment {

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

    @Event(value = {R.id.ls_01, R.id.ls_02, R.id.ls_03, R.id.ls_04, R.id.ls_05, R.id.ls_06, R.id
            .ls_07, R.id.ls_08, R.id.ls_09}, type = View.OnClickListener.class)
    private void onClick(View v) {
        String gId = "";
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ls_01:
                gId = "LA001";
                LogUtil.i(gId);
                break;
            case R.id.ls_02:
                gId = "LA002";
                LogUtil.i(gId);
                break;
            case R.id.ls_03:
                gId = "LA003";
                LogUtil.i(gId);
                break;
            case R.id.ls_04:
                gId = "LA004";
                LogUtil.i(gId);
                break;
            case R.id.ls_05:
                gId = "LA005";
                LogUtil.i(gId);
                break;
            case R.id.ls_06:
                gId = "LA006";
                LogUtil.i(gId);
                break;
            case R.id.ls_07:
                gId = "LA007";
                LogUtil.i(gId);
                break;
            case R.id.ls_08:
                gId = "LA008";
                LogUtil.i(gId);
                break;
            case R.id.ls_09:
                gId = "LA009";
                LogUtil.i(gId);
                break;
        }
//        if (application.getGridIds().contains(gId)) {
            if (type == 0) {
                intent.setClass(getActivity(), GridFunctionActivity.class);
                intent.putExtra("gridId", gId);
                startActivity(intent);
            } else if (type == 98) {
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
            } else {
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