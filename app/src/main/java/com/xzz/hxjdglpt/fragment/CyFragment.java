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
 * 河北村地图
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.fragment_cy)
public class CyFragment extends Fragment {

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

    @Event(value = {R.id.cy_01, R.id.cy_02, R.id.cy_03, R.id.cy_04, R.id.cy_05, R.id.cy_06, R.id
            .cy_07, R.id.cy_08, R.id.cy_09, R.id.cy_10}, type = View.OnClickListener.class)
    private void onClick(View v) {
        String gId = "";
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.cy_01:
                gId = "03301";
                LogUtil.i(gId);
                break;
            case R.id.cy_02:
                gId = "03302";
                LogUtil.i(gId);
                break;
            case R.id.cy_03:
                gId = "03303";
                LogUtil.i(gId);
                break;
            case R.id.cy_04:
                gId = "03304";
                LogUtil.i(gId);
                break;
            case R.id.cy_05:
                gId = "03305";
                LogUtil.i(gId);
                break;
            case R.id.cy_06:
                gId = "03306";
                LogUtil.i(gId);
                break;
            case R.id.cy_07:
                gId = "03307";
                LogUtil.i(gId);
                break;
            case R.id.cy_08:
                gId = "03308";
                LogUtil.i(gId);
                break;
            case R.id.cy_09:
                gId = "03309";
                LogUtil.i(gId);
                break;
            case R.id.cy_10:
                gId = "03310";
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
