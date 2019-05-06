package com.xzz.hxjdglpt.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.woozzu.android.widget.IndexableListView;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.adapter.ContactAdapter;
import com.xzz.hxjdglpt.adapter.ContactGridZrrAdapter;
import com.xzz.hxjdglpt.adapter.SearchContactAdapter;
import com.xzz.hxjdglpt.adapter.SearchContactGridZrrAdapter;
import com.xzz.hxjdglpt.model.GridZrr;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbz on 2017/7/26.
 */

public class CustomSearchGridZrrDialog extends Dialog {


    private List<GridZrr> mList = new ArrayList<GridZrr>();

    public List<GridZrr> mSearchList = new ArrayList<GridZrr>();

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private ContactGridZrrAdapter mAdapter;

    private SearchContactGridZrrAdapter mSearchAdapter;

    private String currentSearchTip;

    private IndexableListView mContactList;
    private SearchView mSearchView;
    private ListView mSearchContactList;
    private ImageView mImageView;

    private Context context;
    private LayoutInflater inflater;


    private ClickListenerInterface clickListenerInterface;

    private ItemClickListenerInterface itemClickListenerInterface;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public interface ItemClickListenerInterface {

        public void contactClick(AdapterView<?> parent, View view, int position, long id, boolean
                isChecked);

        public void searchClick(AdapterView<?> parent, View view, int position, long id, boolean
                isChecked);

    }


    public CustomSearchGridZrrDialog(@NonNull Context context, List<GridZrr> mList) {
        super(context);
        this.context = context;
        this.mList = mList;
    }

    public CustomSearchGridZrrDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_search_list, null);
        setContentView(layout);
        mContactList = (IndexableListView) layout.findViewById(R.id.dialog_contact);
        mSearchView = (SearchView) layout.findViewById(R.id.dialog_search_view);
        mSearchContactList = (ListView) layout.findViewById(R.id.dialog_search_contact);
        mImageView = (ImageView) layout.findViewById(R.id.imageview);
        Button btn1 = (Button) layout.findViewById(R.id.custom_dialog_ok);
        Button btn2 = (Button) layout.findViewById(R.id.custom_dialog_cancel);
        btn1.setOnClickListener(new clickListener());
        btn2.setOnClickListener(new clickListener());
        initView();
        initData();
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        if (mSearchView != null) {
            try {        //--拿到字节码
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                // --暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mContactList.setFastScrollEnabled(true);

        mContactList.setOnItemClickListener(new itemClickListener());

        mSearchContactList.setOnItemClickListener(new itemClickListener());

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                // to avoid click x button and the edittext hidden
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText (ContactsActivity.this ,
                // "begin search" , Toast . LENGTH_SHORT ) . show ( ) ;
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    query(newText);
                } else {
                    mContactList.setVisibility(View.VISIBLE);
                    mSearchContactList.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    private void initData() {
        mAdapter = new ContactGridZrrAdapter(inflater, mList, indexMap);
        mSearchAdapter = new SearchContactGridZrrAdapter(inflater, mSearchList);
        mContactList.setAdapter(mAdapter);
        mSearchContactList.setAdapter(mSearchAdapter);
        notifyDataSetChanged();
    }

    private void query(String newText) {
        currentSearchTip = newText;
        if (TextUtils.isEmpty(newText)) {
            ToastUtil.show(context, R.string.search_not_null, Toast.LENGTH_SHORT);
        } else {
            search();
            mSearchAdapter.notifyDataSetChanged();
            mContactList.setVisibility(View.INVISIBLE);
            mSearchContactList.setVisibility(View.VISIBLE);
            //
            handler.sendEmptyMessage(0);
        }
    }

    private void search() {
        mSearchList.clear();
        for (GridZrr contact : mList) {
            if (contact.getRealName().contains(currentSearchTip)) {
                mSearchList.add(contact);
            }
        }
    }

    private void processData() {
        Collections.sort(mList, new GridZrr.ContactsComparactor());

        int size = mList.size();
        for (int i = 0; i < size; i++) {
            GridZrr contact = mList.get(i);
            String firstChar = contact.getTag() + "";
            if (!indexMap.containsKey(firstChar)) {
                indexMap.put(firstChar, i);
            }
        }
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void setItemClicklistener(ItemClickListenerInterface itemClickListenerInterface) {
        this.itemClickListenerInterface = itemClickListenerInterface;
    }

    public void notifyDataSetChanged() {
        if (mList.size() <= 0) {
            mImageView.setVisibility(View.VISIBLE);
            Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
            mImageView.startAnimation(loadAnimation);
        } else {
            processData();
            mImageView.setVisibility(View.GONE);
            mImageView.clearAnimation();
            mSearchAdapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.custom_dialog_ok:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.custom_dialog_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }

    private class itemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LogUtil.i("onItemClick");
            CheckBox cBox = (CheckBox) view.findViewById(R.id.dialog_item_checkBox);
            if (cBox.isChecked()) {
                cBox.setChecked(false);
            } else {
                cBox.setChecked(true);
            }
            int v = parent.getId();
            switch (v) {
                case R.id.dialog_contact:
                    itemClickListenerInterface.contactClick(parent, view, position, id, cBox
                            .isChecked());
                    break;
                case R.id.dialog_search_contact:
                    handler.sendEmptyMessage(1);
                    itemClickListenerInterface.searchClick(parent, view, position, id, cBox
                            .isChecked());
                    break;
            }
        }
    }

    private void setCheckByTag(ListView list1, ListView list2) {
        for (int i = 0; i < list1.getChildCount(); i++) {
            CheckBox cb1 = (CheckBox) list1.getChildAt(i).findViewById(R.id.dialog_item_checkBox);
            String tag1 = String.valueOf(cb1.getTag());
            LogUtil.i("cb1_" + i + "=" + cb1.isChecked());
            for (int j = 0; j < list2.getChildCount(); j++) {
                CheckBox cb2 = (CheckBox) list2.getChildAt(j).findViewById(R.id
                        .dialog_item_checkBox);
                LogUtil.i("cb2_" + j + "=" + cb2.isChecked());
                String tag2 = String.valueOf(cb2.getTag());
                if (tag1.equals(tag2)) {
                    cb2.setChecked(cb1.isChecked());
                }
            }
        }
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    setCheckByTag(mContactList, mSearchContactList);
                    break;
                case 1:
                    setCheckByTag(mSearchContactList, mContactList);
                    break;
            }
        }
    };

    public void showView() {
        this.show();
        notifyDataSetChanged();
    }

}
