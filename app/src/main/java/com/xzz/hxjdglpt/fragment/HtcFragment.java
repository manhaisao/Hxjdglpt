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
import com.xzz.hxjdglpt.activity.JlyActivity;
import com.xzz.hxjdglpt.activity.JlyInfo;
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
 * 华亭村地图
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.fragment_htc)
public class HtcFragment extends Fragment {

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

    @Event(value = {R.id.htc_grid03201, R.id.htc_grid03202, R.id.htc_grid03203, R.id
            .htc_grid03204, R.id.htc_grid03205, R.id.htc_grid03206, R.id.htc_grid03207, R.id
            .htc_grid03208,R.id.htc_jly}, type = View.OnClickListener.class)
    private void onClick(View v) {
        String gId = "";
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.htc_grid03201:
                gId = "03201";
                break;
            case R.id.htc_grid03202:
                gId = "03202";
                break;
            case R.id.htc_grid03203:
                gId = "03203";
                break;
            case R.id.htc_grid03204:
                gId = "03204";
                break;
            case R.id.htc_grid03205:
                gId = "03205";
                break;
            case R.id.htc_grid03206:
                gId = "03206";
                break;
            case R.id.htc_grid03207:
                gId = "03207";
                break;
            case R.id.htc_grid03208:
                gId = "03208";
                break;
            case R.id.htc_jly:
                gId = "jly";
                break;
        }
        LogUtil.i("gid===="+gId);
        if("jly".equals(gId)){
            intent.setClass(getActivity(), JlyInfo.class);
            startActivity(intent);
        }else {
//            if (application.getGridIds().contains(gId)) {
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
//            } else {
//                ToastUtil.show(getActivity(), R.string.no_power);
//            }
        }
    }

}
