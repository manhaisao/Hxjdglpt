package com.xzz.hxjdglpt.activity.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woozzu.android.widget.IndexableListView;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.activity.ui.activity.GroupListActivity;
import com.xzz.hxjdglpt.adapter.MIContactAdapter;
import com.xzz.hxjdglpt.adapter.MISearchContactAdapter;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.server.widget.SelectableRoundedImageView;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {

    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    //    private TextView mUnreadTextView;
//    private View mHeadView;
    //    private EditText mSearchEditText;
    private ListView mSearchContactList;
//    private PinyinComparator mPinyinComparator;
//    private SideBar mSidBar;

    private IndexableListView mContactList;
    /**
     * 中部展示的字母提示
     */
//    private TextView mDialogTextView;

//    private List<Friend> mFriendList;
//    private List<Friend> mFilteredFriendList;

    private List<Friend> mList = new ArrayList<Friend>();

    private List<Friend> mSearchList = new ArrayList<Friend>();

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private MIContactAdapter mAdapter;

    private MISearchContactAdapter mSearchAdapter;

    private String currentSearchTip;
    private LayoutInflater mInflater;
    /**
     * 好友列表的 mFriendListAdapter
     */
//    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
//    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;

    private User user;

//    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;

    private SearchView mSearchView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        if (user == null) {
            user = ((BaseApplication) getActivity().getApplication()).getUser();
        }
        initView(view);
        initData();
//        updateUI();
//        refreshUIListener();
        return view;
    }

    private void startFriendDetailsPage(Friend user) {
//        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
//        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
//        intent.putExtra("friend", friend);
//        startActivity(intent);
        user.setName(user.getDisplayName());
        user.setPortraitUri(Uri.parse(ConstantUtil.FILE_DOWNLOAD_URL + user.getDiaplayPic()));
        RongIM.getInstance().startPrivateChat(getActivity(), user.getUserId(), user
                .getDisplayName());
    }

    private void initView(View view) {
//        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mSearchContactList = (ListView) view.findViewById(R.id.mi_listview);
//        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
//        mDialogTextView = (TextView) view.findViewById(R.id.group_dialog);
//        mSidBar.setTextView(mDialogTextView);
        mContactList = (IndexableListView) view.findViewById(R.id.mi_indexList);
        mSearchView = (SearchView) view.findViewById(R.id.mi_search_view);
//        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
//        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header, null);
//        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
//        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id
//                .re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) view.findViewById(R.id.re_chatroom);
//        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id
//                .publicservice);
        RelativeLayout selfLayout = (RelativeLayout) view.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) view.findViewById(R.id
                .contact_me_img);
        mNameTextView = (TextView) view.findViewById(R.id.contact_me_name);
        updatePersonalUI();
//        mSearchContactList.addHeaderView(mHeadView);

        selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
//        newFriendsLayout.setOnClickListener(this);
//        publicServiceLayout.setOnClickListener(this);
        //设置右侧触摸监听
//        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                //该字母首次出现的位置
//                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
//                if (position != -1) {
//                    mListView.setSelection(position);
//                }
//
//            }
//        });
        mInflater = (LayoutInflater) getActivity().getSystemService(getActivity()
                .LAYOUT_INFLATER_SERVICE);
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
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Friend c = mList.get(position);
//                showTipsDialog(c);
                startFriendDetailsPage(c);
            }
        });

        mSearchContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Friend c = mSearchList.get(position);
//                showTipsDialog(c);
                startFriendDetailsPage(c);
            }
        });
//        mSearchView.setIconified(false);
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
                    mSearchContactList.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void initData() {
//        FriendListAdapter adapter = new FriendListAdapter(getActivity(), mFriendList);
//        mListView.setAdapter(adapter);
//        mFilteredFriendList = new ArrayList<>();
//        //实例化汉字转拼音类
//        mCharacterParser = CharacterParser.getInstance();
//        mPinyinComparator = PinyinComparator.getInstance();

        mAdapter = new MIContactAdapter(mInflater, mList, indexMap);
        mSearchAdapter = new MISearchContactAdapter(mInflater, mSearchList);

        mContactList.setAdapter(mAdapter);
        mSearchContactList.setAdapter(mSearchAdapter);

        loadUsersData();
    }

    private void query(String newText) {
        currentSearchTip = newText;
        if (TextUtils.isEmpty(newText)) {
            ToastUtil.show(getActivity(), R.string.search_not_null, Toast.LENGTH_SHORT);
        } else {
            search();
            mSearchAdapter.notifyDataSetChanged();
            mContactList.setVisibility(View.INVISIBLE);
            mSearchContactList.setVisibility(View.VISIBLE);
        }
    }

    private void search() {
        mSearchList.clear();
        for (Friend contact : mList) {
            if (contact.getDisplayName().contains(currentSearchTip)) {
                mSearchList.add(contact);
            }
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (mDialogTextView != null) {
//            mDialogTextView.setVisibility(View.INVISIBLE);
//        }
    }

//    /**
//     * 根据输入框中的值来过滤数据并更新ListView
//     *
//     * @param filterStr 需要过滤的 String
//     */
//    private void filterData(String filterStr) {
//        List<Friend> filterDateList = new ArrayList<>();
//
//        try {
//            if (TextUtils.isEmpty(filterStr)) {
//                filterDateList = mFriendList;
//            } else {
//                filterDateList.clear();
//                for (Friend friendModel : mFriendList) {
//                    String name = friendModel.getName();
//                    String displayName = friendModel.getDisplayName();
//                    if (!TextUtils.isEmpty(displayName)) {
//                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name)
//                                .startsWith(filterStr) || displayName.contains(filterStr) ||
//                                mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
//                            filterDateList.add(friendModel);
//                        }
//                    } else {
//                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name)
//                                .startsWith(filterStr)) {
//                            filterDateList.add(friendModel);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 根据a-z进行排序
//        Collections.sort(filterDateList, mPinyinComparator);
//        mFilteredFriendList = filterDateList;
//        mFriendListAdapter.updateListView(filterDateList);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.re_newfriends:
//                mUnreadTextView.setVisibility(View.GONE);
//                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
//                startActivityForResult(intent, 20);
//                break;
            case R.id.re_chatroom:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
//            case R.id.publicservice:
//                Intent intentPublic = new Intent(getActivity(), PublicServiceActivity.class);
//                startActivity(intentPublic);
//                break;
            case R.id.contact_me_item:
                RongIM.getInstance().startPrivateChat(getActivity(), mId, mCacheName);
                break;
        }
    }

//    private void refreshUIListener() {
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_FRIEND, new
//                BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String command = intent.getAction();
//                if (!TextUtils.isEmpty(command)) {
//                    updateUI();
//                }
//            }
//        });

    //好友新增红圈提示
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_RED_DOT, new
//                BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String command = intent.getAction();
//                if (!TextUtils.isEmpty(command)) {
////                    mUnreadTextView.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new
//                BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                updatePersonalUI();
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        try {
//            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_FRIEND);
//            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_RED_DOT);
//            BroadcastManager.getInstance(getActivity()).destroy(SealConst.CHANGEINFO);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
    }

//    private void updateUI() {
//        SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager
//                .ResultCallback<List<Friend>>() {
//            @Override
//            public void onSuccess(List<Friend> friendsList) {
//                updateFriendsList(friendsList);
//            }
//
//            @Override
//            public void onError(String errString) {
//                updateFriendsList(null);
//            }
//        });
//    }

//    private void updateFriendsList(List<Friend> friendsList) {
//        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
//        boolean isReloadList = false;
//        if (mFriendList != null && mFriendList.size() > 0) {
//            mFriendList.clear();
//            isReloadList = true;
//        }
//        mFriendList = friendsList;
//        if (mFriendList != null && mFriendList.size() > 0) {
//            handleFriendDataForSort();
//            mNoFriends.setVisibility(View.GONE);
//        } else {
//            mNoFriends.setVisibility(View.VISIBLE);
//        }
//        if (mFriendList != null) {
//            // 根据a-z进行排序源数据
//            Collections.sort(mFriendList, mPinyinComparator);
//        }
//        if (isReloadList) {
////            mSidBar.setVisibility(View.VISIBLE);
//            mFriendListAdapter.updateListView(mFriendList);
//        } else {
////            mSidBar.setVisibility(View.VISIBLE);
//            mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendList);
//
//            mListView.setAdapter(mFriendListAdapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if (mListView.getHeaderViewsCount() > 0) {
//                        startFriendDetailsPage(mFriendList.get(position - 1));
//                    } else {
//                        startFriendDetailsPage(mFilteredFriendList.get(position));
//                    }
//                }
//            });
//
//
//            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, final int
//                        position, long id) {
//                    Friend bean = mFriendList.get(position - 1);
//                    startFriendDetailsPage(bean);
//                    return true;
//                }
//            });
//            mSearchEditText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//                    filterData(s.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (s.length() != 0) {
//                        if (mListView.getHeaderViewsCount() > 0) {
//                            mListView.removeHeaderView(mHeadView);
//                        }
//                    } else {
//                        if (mListView.getHeaderViewsCount() == 0) {
//                            mListView.addHeaderView(mHeadView);
//                        }
//                    }
//                }
//            });
//        }
//    }

    private void updatePersonalUI() {
        mId = user.getUserId();
        mCacheName = user.getRealName();
        final String header = ConstantUtil.FILE_DOWNLOAD_URL + user.getPicture();
        mNameTextView.setText(mCacheName);
        if (!TextUtils.isEmpty(mId)) {
            UserInfo userInfo = new UserInfo(mId, mCacheName, Uri.parse(header));
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
            ImageLoader.getInstance().displayImage(portraitUri, mSelectableRoundedImageView,
                    BaseApplication.getOptions());
        }
    }

//    private void handleFriendDataForSort() {
//        for (Friend friend : mFriendList) {
//            if (friend.isExitsDisplayName()) {
//                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling
//                        ());
//                friend.setLetters(letters);
//            } else {
//                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
//                friend.setLetters(letters);
//            }
//        }
//    }

//    private String replaceFirstCharacterWithUppercase(String spelling) {
//        if (!TextUtils.isEmpty(spelling)) {
//            char first = spelling.charAt(0);
//            char newFirst = first;
//            if (first >= 'a' && first <= 'z') {
//                newFirst -= 32;
//            }
//            return spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
//        } else {
//            return "#";
//        }
//    }

    private void loadUsersData() {
//        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
//                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/queryUserByIsMI";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "queryUserByIsMI", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
//                SuccinctProgress.dismiss();
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
                        mList.addAll(users);
                        if (mList.size() > 0) {
                            processData();
                            mAdapter.notifyDataSetChanged();
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(getActivity());
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
//                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryUserByIsMI");
    }
}
