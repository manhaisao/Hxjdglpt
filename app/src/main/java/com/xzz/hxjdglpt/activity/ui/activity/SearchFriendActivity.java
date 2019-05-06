package com.xzz.hxjdglpt.activity.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzz.hxjdglpt.SealConst;
import com.xzz.hxjdglpt.SealUserInfoManager;
import com.xzz.hxjdglpt.activity.BaseActivity;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.db.Friend;
import com.xzz.hxjdglpt.server.network.async.AsyncTaskManager;
import com.xzz.hxjdglpt.server.network.http.HttpException;
import com.xzz.hxjdglpt.server.response.FriendInvitationResponse;
import com.xzz.hxjdglpt.server.response.GetUserInfoByPhoneResponse;
import com.xzz.hxjdglpt.server.utils.AMUtils;
import com.xzz.hxjdglpt.server.utils.CommonUtils;
import com.xzz.hxjdglpt.server.utils.NToast;
import com.xzz.hxjdglpt.server.widget.DialogWithYesOrNoUtils;
import com.xzz.hxjdglpt.server.widget.LoadDialog;
import com.xzz.hxjdglpt.server.widget.SelectableRoundedImageView;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imlib.model.UserInfo;

public class SearchFriendActivity extends BaseActivity {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;
    private EditText mEtSearch;
    private LinearLayout searchItem;
    private TextView searchName;
    private SelectableRoundedImageView searchImage;
    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;

    private Friend mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle((R.string.search_friend));

        mEtSearch = (EditText) findViewById(R.id.search_edit);
        searchItem = (LinearLayout) findViewById(R.id.search_result);
        searchName = (TextView) findViewById(R.id.search_name);
        searchImage = (SelectableRoundedImageView) findViewById(R.id.search_header);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    mPhone = s.toString().trim();
                    if (!AMUtils.isMobile(mPhone)) {
                        NToast.shortToast(SearchFriendActivity.this, "非法手机号");
                        return;
                    }
                    hintKbTwo();
                    LoadDialog.show(SearchFriendActivity.this);
//                    request(SEARCH_PHONE, true);
                } else {
                    searchItem.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//    @Override
//    public Object doInBackground(int requestCode, String id) throws HttpException {
//        switch (requestCode) {
//            case SEARCH_PHONE:
//                return action.getUserInfoFromPhone("86", mPhone);
//            case ADD_FRIEND:
//                return action.sendFriendInvitation(mFriendId, addFriendMessage);
//        }
//        return super.doInBackground(requestCode, id);
//    }

//    @Override
//    public void onSuccess(int requestCode, Object result) {
//        if (result != null) {
//            switch (requestCode) {
//                case SEARCH_PHONE:
//                    final GetUserInfoByPhoneResponse userInfoByPhoneResponse =
// (GetUserInfoByPhoneResponse) result;
//                    if (userInfoByPhoneResponse.getCode() == 200) {
//                        LoadDialog.dismiss(SearchFriendActivity.this);
//                        NToast.shortToast(SearchFriendActivity.this, "success");
//                        mFriendId = userInfoByPhoneResponse.getResult().getId();
//                        searchItem.setVisibility(View.VISIBLE);
//                        String portraitUri = null;
//                        if (userInfoByPhoneResponse.getResult() != null) {
//                            GetUserInfoByPhoneResponse.ResultEntity
// userInfoByPhoneResponseResult = userInfoByPhoneResponse.getResult();
//                            UserInfo userInfo = new UserInfo(userInfoByPhoneResponseResult
// .getId(),
//                                    userInfoByPhoneResponseResult.getNickname(),
//                                    Uri.parse(userInfoByPhoneResponseResult.getPortraitUri()));
//                            portraitUri = SealUserInfoManager.getInstance().getPortraitUri
// (userInfo);
//                        }
//                        ImageLoader.getInstance().displayImage(portraitUri, searchImage,
// BaseApplication.getOptions());
//                        searchName.setText(userInfoByPhoneResponse.getResult().getNickname());
//                        searchItem.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (isFriendOrSelf(mFriendId)) {
////                                    Intent intent = new Intent(SearchFriendActivity.this,
/// UserDetailActivity.class);
////                                    intent.putExtra("friend", mFriend);
////                                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
////                                    startActivity(intent);
////                                    SealAppContext.getInstance().pushActivity
/// (SearchFriendActivity.this);
//                                    return;
//                                }
//                                DialogWithYesOrNoUtils.getInstance().showEditDialog
// (SearchFriendActivity.this, getString(R.string.add_text), getString(R.string.add_friend), new
// DialogWithYesOrNoUtils.DialogCallBack() {
//                                    @Override
//                                    public void executeEvent() {
//
//                                    }
//
//                                    @Override
//                                    public void updatePassword(String oldPassword, String
// newPassword) {
//
//                                    }
//
//                                    @Override
//                                    public void executeEditEvent(String editText) {
//                                        if (!CommonUtils.isNetworkConnected
// (SearchFriendActivity.this)) {
//                                            NToast.shortToast(SearchFriendActivity.this, R
// .string.network_not_available);
//                                            return;
//                                        }
//                                        addFriendMessage = editText;
//                                        if (TextUtils.isEmpty(editText)) {
//                                            addFriendMessage = "我是" + getSharedPreferences
// ("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
//                                        }
//                                        if (!TextUtils.isEmpty(mFriendId)) {
//                                            LoadDialog.show(SearchFriendActivity.this);
//                                            request(ADD_FRIEND);
//                                        } else {
//                                            NToast.shortToast(SearchFriendActivity.this, "id is
// null");
//                                        }
//                                    }
//                                });
//                            }
//                        });
//
//                    }
//                    break;
//                case ADD_FRIEND:
//                    FriendInvitationResponse fres = (FriendInvitationResponse) result;
//                    if (fres.getCode() == 200) {
//                        NToast.shortToast(SearchFriendActivity.this, getString(R.string
// .request_success));
//                        LoadDialog.dismiss(SearchFriendActivity.this);
//                    } else {
//                        NToast.shortToast(SearchFriendActivity.this, "请求失败 错误码:" + fres.getCode
// ());
//                        LoadDialog.dismiss(SearchFriendActivity.this);
//                    }
//                    break;
//            }
//        }
//    }

//    @Override
//    public void onFailure(int requestCode, int state, Object result) {
//        switch (requestCode) {
//            case ADD_FRIEND:
//                NToast.shortToast(SearchFriendActivity.this, "你们已经是好友");
//                LoadDialog.dismiss(SearchFriendActivity.this);
//                break;
//            case SEARCH_PHONE:
//                if (state == AsyncTaskManager.HTTP_ERROR_CODE || state == AsyncTaskManager
// .HTTP_NULL_CODE) {
//                    super.onFailure(requestCode, state, result);
//                } else {
//                    NToast.shortToast(SearchFriendActivity.this, "用户不存在");
//                }
//                LoadDialog.dismiss(SearchFriendActivity.this);
//                break;
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
