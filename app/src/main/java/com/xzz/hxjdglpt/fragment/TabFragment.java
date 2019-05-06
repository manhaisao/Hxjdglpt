package com.xzz.hxjdglpt.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.OnItemSelectedListener;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by dbz on 2017/5/10.
 */

@ContentView(R.layout.fragment_tab)
public class TabFragment extends Fragment {
    @ViewInject(R.id.index_img)
    private ImageView index_img;
    @ViewInject(R.id.my_img)
    private ImageView my_img;
    @ViewInject(R.id.connect_address_img)
    private ImageView connect_img;

    @ViewInject(R.id.index_tv)
    private TextView index_tv;
    @ViewInject(R.id.my_tv)
    private TextView my_tv;
    @ViewInject(R.id.connect_address_tv)
    private TextView connect_tv;

    @ViewInject(R.id.index)
    private LinearLayout index;
    @ViewInject(R.id.my)
    private LinearLayout my;
    @ViewInject(R.id.connect_address)
    private LinearLayout connect;


    private int currentIndex;

    private int[] pressed = {R.mipmap.index_pressed, R.mipmap.connect_pressed, R.mipmap.my_pressed};
    private int[] unpressed = {R.mipmap.index_unpressed, R.mipmap.connect_unpressed, R.mipmap
            .my_unpressed};
    private ImageView[] button = new ImageView[3];
    private TextView[] textViews = new TextView[3];

    private List<Role> roles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        button[0] = index_img;
        button[1] = connect_img;
        button[2] = my_img;
        textViews[0] = index_tv;
        textViews[1] = connect_tv;
        textViews[2] = my_tv;
        ((OnItemSelectedListener) getActivity()).onItemSelected(R.id.index);
        currentIndex = R.id.index;
        BaseApplication application = (BaseApplication) (getActivity().getApplication());
        roles = application.getRolesList();
        return view;
    }


    @Event(value = {R.id.index, R.id.my, R.id.connect_address}, type = View.OnClickListener.class)
    private void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.index:
                if (currentIndex == R.id.index) {
                    return;
                }
                currentIndex = R.id.index;
                ((OnItemSelectedListener) getActivity()).onItemSelected(R.id.index);
                updateBackground(0);
                break;
            case R.id.my:
                if (currentIndex == R.id.my) {
                    return;
                }
                currentIndex = R.id.my;
                ((OnItemSelectedListener) getActivity()).onItemSelected(R.id.my);
                updateBackground(2);
                break;
            case R.id.connect_address:
                if (roles != null && roles.size() > 0) {
                    if (currentIndex == R.id.connect_address) {
                        return;
                    }
                    currentIndex = R.id.connect_address;
                    ((OnItemSelectedListener) getActivity()).onItemSelected(R.id.connect_address);
                    updateBackground(1);
                } else {
                    ToastUtil.show(getActivity(), "暂无此权限");
                }
                break;
            default:
                break;
        }
    }

    private void updateBackground(int j) {
        for (int i = 0; i < pressed.length; i++) {
            if (j == i) {
                button[i].setImageResource(pressed[i]);
                textViews[i].setTextColor(getResources().getColor(R.color.tab_tv_color_));
            } else {
                button[i].setImageResource(unpressed[i]);
                textViews[i].setTextColor(getResources().getColor(R.color.tab_tv_color));
            }
        }
    }
}
