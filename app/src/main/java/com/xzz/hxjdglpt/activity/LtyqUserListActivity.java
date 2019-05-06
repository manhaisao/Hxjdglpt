package com.xzz.hxjdglpt.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woozzu.android.widget.IndexableListView;
import com.xzz.hxjdglpt.adapter.ContactAdapter;
import com.xzz.hxjdglpt.adapter.SearchContactAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.PlotLt;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示信息LIST
 * Created by dbz on 2017/5/14.
 */

@ContentView(R.layout.aty_ltya_userlist)
public class LtyqUserListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener {
    @ViewInject(R.id.hx_title_tv)
    private TextView tvTitle;
    private List<User> mList = new ArrayList<User>();

    public List<User> mSearchList = new ArrayList<User>();

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private ContactAdapter mAdapter;

    private SearchContactAdapter mSearchAdapter;

    private String currentSearchTip;

    @ViewInject(R.id.dialog_contact)
    private IndexableListView mContactList;
    @ViewInject(R.id.dialog_search_view)
    private SearchView mSearchView;
    @ViewInject(R.id.dialog_search_contact)
    private ListView mSearchContactList;

    private LayoutInflater inflater;

    private User user;

    private String plotid;

    //最终存储选择的user
    private ArrayList<User> receivors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (user == null) {
            user = application.getUser();
        }
        plotid = getIntent().getStringExtra("plotid");
        initView();
        initData();
        mContactList.setOnItemClickListener(this);
        mSearchContactList.setOnItemClickListener(this);
    }

    public void initView() {
        tvTitle.setText("人员列表");
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

//        mContactList.setOnItemClickListener(new CustomSearchDialog.itemClickListener());
//
//        mSearchContactList.setOnItemClickListener(new CustomSearchDialog.itemClickListener());

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


    public void initData() {
        mAdapter = new ContactAdapter(inflater, mList, indexMap);
        mSearchAdapter = new SearchContactAdapter(inflater, mSearchList);
        mContactList.setAdapter(mAdapter);
        mSearchContactList.setAdapter(mSearchAdapter);
        loadData();
    }

    private void query(String newText) {
        currentSearchTip = newText;
        if (TextUtils.isEmpty(newText)) {
            ToastUtil.show(this, R.string.search_not_null, Toast.LENGTH_SHORT);
        } else {
            search();
            mSearchAdapter.notifyDataSetChanged();
            mContactList.setVisibility(View.INVISIBLE);
            mSearchContactList.setVisibility(View.VISIBLE);
            //
//            handler.sendEmptyMessage(0);
        }
    }

    private void search() {
        mSearchList.clear();
        for (User contact : mList) {
            if (contact.getRealName().contains(currentSearchTip)) {
                mSearchList.add(contact);
            }
        }
    }

    private void processData() {
        Collections.sort(mList, new User.ContactsComparactor());

        int size = mList.size();
        for (int i = 0; i < size; i++) {
            User contact = mList.get(i);
            String firstChar = contact.getTag() + "";
            if (!indexMap.containsKey(firstChar)) {
                indexMap.put(firstChar, i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 信息获取
     */
    private void loadData() {
        SuccinctProgress.showSuccinctProgress(LtyqUserListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/queryAllUser";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());

        VolleyRequest.RequestPost(this, url, "queryList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<User> data = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<User>>() {
                                }.getType());
                        mList.addAll(data);
                        mAdapter.notifyDataSetChanged();
                        if (mList.size() > 0) {
                            processData();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LtyqUserListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LtyqUserListActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Event(value = {R.id.iv_back_tv, R.id.custom_dialog_ok, R.id.custom_dialog_cancel}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_tv:
                finish();
                break;
            case R.id.custom_dialog_ok:
                setName();
                break;
            case R.id.custom_dialog_cancel:
                finish();
                break;
        }
    }

    private StringBuffer names = new StringBuffer();//展示姓名
    private StringBuffer userId = new StringBuffer();//USERID拼接，作为参数传到后台

    private void setName() {
        names.delete(0, names.length());
        userId.delete(0, userId.length());
        for (User u : receivors) {
            names.append(u.getRealName()).append(",");
            userId.append(u.getUserId()).append(",");
        }
        if (receivors.size() > 0) {
            LogUtil.i("name=" + names.toString());
            joinAll(userId.toString());
        } else {
            ToastUtil.show(this, "请选择用户");
        }
    }

    private void joinAll(String ids) {
        SuccinctProgress.showSuccinctProgress(LtyqUserListActivity.this, "数据请求中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("ids", ids);
        params.put("plotid", plotid);
        String url = ConstantUtil.BASE_URL + "/plotlt/joinAll";
        VolleyRequest.RequestPost(this, url, "joinAll", params, new VolleyListenerInterface(this,
                VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:xxx ；2：token不一致；3：添加失败；4：同意加入论坛；5：申请加入小区论坛；
                    if ("1".equals(resultCode)) {
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(LtyqUserListActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LtyqUserListActivity.this, "请求失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryList");
        BaseApplication.getRequestQueue().cancelAll("joinAll");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox cBox = (CheckBox) view.findViewById(R.id.dialog_item_checkBox);
        if (cBox.isChecked()) {
            cBox.setChecked(false);
        } else {
            cBox.setChecked(true);
        }
        int v = parent.getId();
        switch (v) {
            case R.id.dialog_contact:
                User us = mList.get(position);
                if (cBox.isChecked()) {
                    // 选中
                    receivors.add(us);
                } else {
                    // 取消选中
                    receivors.remove(us);
                }
                break;
            case R.id.dialog_search_contact:
                handler.sendEmptyMessage(1);
                User u = mSearchList.get(position);
                if (cBox.isChecked()) {
                    // 选中
                    receivors.add(u);
                } else {
                    // 取消选中
                    receivors.remove(u);
                }
                break;
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

    private void setCheckByTag(ListView list1, ListView list2) {
        for (int i = 0; i < list1.getChildCount(); i++) {
            CheckBox cb1 = (CheckBox) list1.getChildAt(i).findViewById(R.id.dialog_item_checkBox);
            String tag1 = String.valueOf(cb1.getTag());
            for (int j = 0; j < list2.getChildCount(); j++) {
                CheckBox cb2 = (CheckBox) list2.getChildAt(j).findViewById(R.id
                        .dialog_item_checkBox);
                String tag2 = String.valueOf(cb2.getTag());
                if (tag1.equals(tag2)) {
                    cb2.setChecked(cb1.isChecked());
                }
            }
        }
    }

}
