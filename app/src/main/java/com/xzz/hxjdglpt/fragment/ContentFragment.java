package com.xzz.hxjdglpt.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xzz.hxjdglpt.activity.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 *
 * Created by dbz on 2017/5/10.
 */

@ContentView(R.layout.fragment_content)
public class ContentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }
}
