package com.xzz.hxjdglpt.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import android.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.CscxglActivity;
import com.xzz.hxjdglpt.activity.R;

import java.lang.reflect.Field;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.woozzu.android.widget.IndexableListView;

import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SectionIndexer;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xzz.hxjdglpt.customview.CircleNetImageView;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Contacts;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

/**
 * 通讯录
 * Created by dbz on 2017/5/15.
 */

@ContentView(R.layout.fragment_connect)
public class ConnectFragment extends Fragment {
    @ViewInject(R.id.hx_title_)
    private TextView tvTitle;

    private LayoutInflater mInflater;
    @ViewInject(R.id.lv_contact)
    private IndexableListView mContactList;

    @ViewInject(R.id.lv_search_contact)
    private ListView mSearchContactList;

    @ViewInject(R.id.tv_contacts_num)
    private TextView mTvContactsNum;

    private List<Contacts> mList = new ArrayList<Contacts>();

    private List<Contacts> mSearchList = new ArrayList<Contacts>();

    private Map<String, Integer> indexMap = new HashMap<String, Integer>();

    private ContactAdapter mAdapter;

    private SearchContactAdapter mSearchAdapter;

    private ImageLoader mImageLoader;

    @ViewInject(R.id.search_view)
    private SearchView mSearchView;

    private String currentSearchTip;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        tvTitle.setText(getString(R.string.address_book));
        initView();
        initData();
        return view;
    }


    public void initView() {
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
        mContactList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Contacts c = mList.get(position);
                showTipsDialog(c);
            }
        });

        mSearchContactList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Contacts c = mSearchList.get(position);
                showTipsDialog(c);
            }
        });
//        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(new OnCloseListener() {

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

    public void initData() {
        BaseApplication app = (BaseApplication) getActivity().getApplication();
        mImageLoader = app.getImageLoader();

        mAdapter = new ContactAdapter();
        mSearchAdapter = new SearchContactAdapter();

        mContactList.setAdapter(mAdapter);
        mSearchContactList.setAdapter(mSearchAdapter);
        BaseApplication application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        loadMoreData();
    }


    private void query(String newText) {
        currentSearchTip = newText;
        if (TextUtils.isEmpty(newText)) {
            ToastUtil.show(getActivity(), R.string.search_not_null, Toast.LENGTH_SHORT);
        } else {
            search();
            mSearchAdapter.notifyDataSetChanged();
            mContactList.setVisibility(View.GONE);
            mSearchContactList.setVisibility(View.VISIBLE);
        }
    }

    private void search() {
        mSearchList.clear();
        for (Contacts contact : mList) {
            if (contact.getName().contains(currentSearchTip)) {
                mSearchList.add(contact);
            }
        }
    }

    private void processData() {
        Collections.sort(mList, new Contacts.ContactsComparactor());

        int size = mList.size();
        for (int i = 0; i < size; i++) {
            Contacts contact = mList.get(i);
            String firstChar = contact.getTag() + "";
            if (!indexMap.containsKey(firstChar)) {
                indexMap.put(firstChar, i);
            }
        }

        mTvContactsNum.setText(String.format(getString(R.string.contacts_num), mList.size()));
    }

    private void loadMoreData() {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/queryUsers";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "user_queryList", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

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
                        List<User> users = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<User>>() {
                        }.getType());
                        for (User u : users) {
                            Contacts contact = new Contacts();
                            contact.setName(u.getRealName());
                            contact.setPicName(u.getPicture());
                            contact.setTelephone(u.getPhone());
                            contact.setEmail(u.getEmail());
                            contact.setQq(u.getQqcode());
                            mList.add(contact);
                        }
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
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("user_queryList");
    }


    private class ContactAdapter extends BaseAdapter implements SectionIndexer {
        private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public ContactAdapter() {
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view != null) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.lv_item_contact, null);
                viewHolder.contactFirstChar = (TextView) view.findViewById(R.id.contact_first_char);
                viewHolder.contactHead = (NetworkImageView) view.findViewById(R.id.contact_head);
                viewHolder.contactName = (TextView) view.findViewById(R.id.contact_name);
                view.setTag(viewHolder);
            }
            viewHolder.contactFirstChar.setVisibility(View.GONE);
            if (position == 0 || (position > 0 && (mList.get(position - 1).getTag() != mList.get
                    (position).getTag()))) {
                viewHolder.contactFirstChar.setText(String.valueOf(mList.get(position).getTag()));
                viewHolder.contactFirstChar.setVisibility(View.VISIBLE);
            }
            viewHolder.contactHead.setDefaultImageResId(R.mipmap.user_icon);
            viewHolder.contactHead.setErrorImageResId(R.mipmap.user_icon);
            viewHolder.contactHead.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + mList.get
                    (position).getPicName(), mImageLoader);
            viewHolder.contactName.setText(mList.get(position).getName());
            return view;
        }

        private class ViewHolder {
            TextView contactFirstChar;
            NetworkImageView contactHead;
            TextView contactName;
        }

        @Override
        public int getPositionForSection(int section) {
            String c = String.valueOf(mSections.charAt(section));
            for (int i = section; i >= 0; i--) {
                if (indexMap.containsKey(c)) {
                    return indexMap.get(c);
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sections[i] = String.valueOf(mSections.charAt(i));
            return sections;
        }
    }

    private class SearchContactAdapter extends BaseAdapter {

        public SearchContactAdapter() {
        }

        @Override
        public int getCount() {
            return mSearchList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view != null) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.lv_item_contact, null);
                viewHolder.contactHead = (CircleNetImageView) view.findViewById(R.id.contact_head);
                viewHolder.contactName = (TextView) view.findViewById(R.id.contact_name);
                view.setTag(viewHolder);
            }
            viewHolder.contactHead.setDefaultImageResId(R.mipmap.user_icon);
            viewHolder.contactHead.setErrorImageResId(R.mipmap.user_icon);
            viewHolder.contactHead.setImageUrl(ConstantUtil.FILE_DOWNLOAD_URL + mSearchList.get
                    (position).getPicName(), mImageLoader);
            viewHolder.contactName.setText(mSearchList.get(position).getName());
            return view;
        }

        private class ViewHolder {
            CircleNetImageView contactHead;
            TextView contactName;
        }
    }


    private void showTipsDialog(final Contacts c) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.connect_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.connect_dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        TextView tvName = (TextView) view.findViewById(R.id.connect_name);
        TextView tvPhone = (TextView) view.findViewById(R.id.connect_phone);
        TextView tvEmail = (TextView) view.findViewById(R.id.connect_email);
        TextView tvQQ = (TextView) view.findViewById(R.id.connect_qq);
        tvName.setText(c.getName());
        tvPhone.setText(c.getTelephone());
        if (c.getEmail().contains("@")) {
            tvEmail.setText(c.getEmail());
        }
        tvQQ.setText(c.getQq());
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c != null && c.getTelephone() != null && !TextUtils.isEmpty(c.getTelephone())) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c
                            .getTelephone().trim()));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                            .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                } else {
                    ToastUtil.show(getActivity(), R.string.no_phone);
                }

            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels - 100;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
