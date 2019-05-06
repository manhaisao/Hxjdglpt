package com.xzz.hxjdglpt.activity.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woozzu.android.widget.IndexableListView;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.BaseActivity;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.adapter.MISelectFriendContactAdapter;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.db.Groups;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.server.utils.NLog;
import com.xzz.hxjdglpt.server.utils.NToast;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by AMing on 16/1/21.
 * Company RongCloud
 */
public class SelectFriendsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 发起讨论组的 adapter
     */
    private MISelectFriendContactAdapter adapter;

    private TextView mHeadRightText;

    private IndexableListView mContactList;

    private List<Friend> mList = new ArrayList<Friend>();

    private List<Friend> selectUser = new ArrayList<Friend>();

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private LayoutInflater mInflater;
    private User user;

    private TextView mTitle;

    private boolean isCrateGroup;

    private boolean isAddGroupMember;
    private boolean isDeleteGroupMember;
    private String groupId;

    private List<User> addGroupMemberList;//群里已有用户，继续添加
    private List<User> deleteGroupMemberList;//群里已有用户，要删除

    private Groups groups;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_disc);
        mHeadRightText = (TextView) findViewById(R.id.text_right);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mHeadRightText.setText("确定");
        mHeadRightText.setOnClickListener(this);
        isCrateGroup = getIntent().getBooleanExtra("createGroup", false);
        isAddGroupMember = getIntent().getBooleanExtra("isAddGroupMember", false);
        isDeleteGroupMember = getIntent().getBooleanExtra("isDeleteGroupMember", false);
        groupId = getIntent().getStringExtra("GroupId");
        if (groupId != null && !TextUtils.isEmpty(groupId)) {
            groups = SealUserInfoManager.getInstance().getGroupsByID(groupId);
        }
//        mSelectedFriend = new ArrayList<>();
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        initView();

        /**
         * 根据进行的操作初始化数据,添加删除群成员和获取好友信息是异步操作,所以做了很多额外的处理
         * 数据添加后还需要过滤已经是群成员,讨论组成员的用户
         * 最后设置adapter显示
         * 后两个操作全都根据异步操作推后
         */
        initData();
    }


    public void initView() {
        if (isDeleteGroupMember) {
            mTitle.setText(getString(R.string.remove_group_member));
        } else if (isAddGroupMember) {
            mTitle.setText(getString(R.string.add_group_member));
        } else if (isCrateGroup) {
            mTitle.setText(getString(R.string.select_group_member));
        }
        mContactList = (IndexableListView) findViewById(R.id.mi_disc_indexList);
        mContactList.setFastScrollEnabled(true);
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend c = mList.get(position);
//                CheckBox cBox = (CheckBox) view.findViewById(R.id.dis_select);
                if (c.isSelect()) {
                    startDisList.remove(c.getUserId());
                    selectUser.remove(c);
//                    cBox.setChecked(false);
                    c.setSelect(false);
                } else {
                    startDisList.add(c.getUserId());
                    selectUser.add(c);
//                    cBox.setChecked(true);
                    c.setSelect(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void initData() {
        adapter = new MISelectFriendContactAdapter(mInflater, mList, indexMap);
        mContactList.setAdapter(adapter);
        if (isAddGroupMember || isDeleteGroupMember) {
            initGroupMemberList();
        } else {
            loadUsersData();
        }
    }

    private void initGroupMemberList() {
        SuccinctProgress.showSuccinctProgress(this, "请求数据中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mi/queryGroupUser";
        HashMap<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        VolleyRequest.RequestPost(SelectFriendsActivity.this, url, "queryGroupUser", params, new
                VolleyListenerInterface(SelectFriendsActivity.this, VolleyListenerInterface
                        .mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("200".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<User> users = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<User>>() {
                        }.getType());
                        if (isAddGroupMember) {
                            addGroupMemberList = users;
                            loadUsersData();
                        } else if (isDeleteGroupMember) {
                            deleteGroupMemberList = users;
                            fillSourceDataListForDeleteGroupMember();
                        }
                    } else {
                        ToastUtil.show(SelectFriendsActivity.this, R.string.load_fail);
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

    private void fillSourceDataListForDeleteGroupMember() {
        for (User u : deleteGroupMemberList) {
            if (!u.getUserId().equals(user.getUserId())) {
                Friend f = new Friend(u.getUserId(), u.getRealName(), Uri.parse(u.getPicture()),
                        u.getRealName(), u.getPicture());
                mList.add(f);
            }
        }
        if (mList.size() > 0) {
            processData();
            adapter.notifyDataSetChanged();
        }
    }

    private void processData() {
        Collections.sort(mList, new Friend.ContactsComparactor());
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            Friend contact = mList.get(i);
            String firstChar = contact.getTag() + "";
            if (!indexMap.containsKey(firstChar)) {
                indexMap.put(firstChar, i);
            }
        }
    }


    private void loadUsersData() {
        SuccinctProgress.showSuccinctProgress(this, "请求数据中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/user/queryUserByIsMI";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(SelectFriendsActivity.this, url, "queryUserByIsMI", params, new
                VolleyListenerInterface(SelectFriendsActivity.this, VolleyListenerInterface
                        .mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Friend> users = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Friend>>() {
                        }.getType());
                        if (isAddGroupMember) {
                            List<Friend> tempF = new ArrayList<Friend>();
                            for (Friend f : users) {
                                boolean isContain = false;
                                for (User u : addGroupMemberList) {
                                    if (f.getUserId().equals(u.getUserId())) {
                                        isContain = true;
                                        break;
                                    }
                                }
                                if (!isContain && !f.getUserId().equals(user.getUserId())) {
                                    tempF.add(f);
                                }
                            }
                            if (users != null) {
                                users.clear();
                                users.addAll(tempF);
                            }
                        }
                        mList.addAll(users);
                        if (mList.size() > 0) {
                            processData();
                            adapter.notifyDataSetChanged();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(SelectFriendsActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(SelectFriendsActivity.this, R.string.load_fail);
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


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryUserByIsMI");
        BaseApplication.getRequestQueue().cancelAll("quitGroup");
        BaseApplication.getRequestQueue().cancelAll("join");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private List<String> startDisList = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_right:
                if (startDisList != null && startDisList.size() > 0) {
                    if (isCrateGroup) {
                        if (mList.size() > 0) {
                            mHeadRightText.setClickable(true);
                            Intent intent = new Intent(SelectFriendsActivity.this,
                                    CreateGroupActivity.class);
                            intent.putExtra("GroupMember", (Serializable) selectUser);
                            startActivity(intent);
                            finish();
                        } else {
                            NToast.shortToast(SelectFriendsActivity.this, "请至少邀请一位好友创建群组");
                            mHeadRightText.setClickable(true);
                        }
                    } else if (isAddGroupMember && addGroupMemberList != null) {
                        addGroupMember();
                    } else if (isDeleteGroupMember && deleteGroupMemberList != null) {
                        deleteGroupMember();
                    }
                } else {
                    Toast.makeText(SelectFriendsActivity.this, getString(R.string
                            .least_one_friend), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }


    private void addGroupMember() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mi/join";
        HashMap<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("groupName", groups.getName());
        StringBuffer sb = new StringBuffer();
        for (String uId : startDisList) {
            sb.append(uId).append(",");
        }
        params.put("userId", sb.toString());
        VolleyRequest.RequestPost(this, url, "join", params, new VolleyListenerInterface
                (SelectFriendsActivity.this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("200".equals(resultCode)) {
                        ToastUtil.show(SelectFriendsActivity.this, "添加成功");
                        finish();
                    } else {
                        ToastUtil.show(SelectFriendsActivity.this, "添加失败");
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

    private void deleteGroupMember() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/m_mi/quitGroup";
        HashMap<String, String> params = new HashMap<>();
        params.put("groupId", groupId);
        StringBuffer sb = new StringBuffer();
        for (String uId : startDisList) {
            sb.append(uId).append(",");
        }
        params.put("userId", sb.toString());
        VolleyRequest.RequestPost(this, url, "quitGroup", params, new VolleyListenerInterface
                (SelectFriendsActivity.this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("200".equals(resultCode)) {
                        ToastUtil.show(SelectFriendsActivity.this, "删除成功");
                        finish();
                    } else {
                        ToastUtil.show(SelectFriendsActivity.this, "删除失败");
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

}
