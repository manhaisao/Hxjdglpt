package com.xzz.hxjdglpt.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.CustomSearchDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代办事项转发
 * Created by dbz on 2017/5/26.
 */
@ContentView(R.layout.aty_schedule_zf)
public class ScheduleZfActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.schedulezf_commit)
    private Button btnCommit;
    @ViewInject(R.id.schedulezf_des)
    private EditText edtDes;
    @ViewInject(R.id.schedulezf_receivor)
    private TextView spreceivor;

    private User user;

    private LayoutInflater mInflater;
    private List<User> userList = new ArrayList<>();
    private ArrayList<User> receivors = new ArrayList<>();

    private String taskId = "";

    private CustomSearchDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        taskId = getIntent().getStringExtra("taskId");
        edtDes.setOnTouchListener(this);
        dialog = new CustomSearchDialog(this, userList);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.schedulezf_des && canVerticalScroll(edtDes))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向能否够滚动
     *
     * @param editText 须要推断的EditText
     * @return true：能够滚动   false：不能够滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText
                .getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    public void initView() {
        tvTitle.setText(R.string.zf_1);
    }


    public void initData() {
        loadGridUser();
    }

    @Event(value = {R.id.iv_back, R.id.schedulezf_commit, R.id.schedulezf_receivor}, type = View
            .OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.schedulezf_commit:
                commit();
                break;
            case R.id.schedulezf_receivor:
                receivors.clear();
                showMutilAlertDialog("请选择接收人");
                break;
        }
    }

    private void loadGridUser() {
        String url = ConstantUtil.BASE_URL + "/user/queryUserQuativeAndJunior";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryUserQuativeAndJunior", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        LogUtil.i(result.toString());
                        try {
                            String resultCode = result.getString("resultCode");
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                JSONArray jsonArray = result.getJSONArray("data");
                                LogUtil.i(jsonArray.toString());
                                Gson gson = new Gson();
                                List<User> list = gson.fromJson(jsonArray.toString(), new
                                        TypeToken<List<User>>() {
                                        }.getType());
                                userList.addAll(list);
                                if (dialog.isShowing()) {
                                    dialog.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(ScheduleZfActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(ScheduleZfActivity.this, R.string.load_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    private void commit() {
        SuccinctProgress.showSuccinctProgress(ScheduleZfActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/m_zf/commitZf";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("zfsm", edtDes.getText().toString());
        params.put("receiveuser", userId.toString());
        params.put("taskId", taskId);
        params.put("zfrName", user.getRealName());
        params.put("jsrName", names.toString());
        VolleyRequest.RequestPost(this, url, "commitZf", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(ScheduleZfActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ScheduleZfActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ScheduleZfActivity.this, R.string.commit_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    public void showMutilAlertDialog(String title) {
        dialog.showView();
        dialog.setTitle(title);
        dialog.setClicklistener(new CustomSearchDialog.ClickListenerInterface() {

            @Override
            public void doConfirm() {
                dialog.dismiss();
                setName();
            }

            @Override
            public void doCancel() {
                dialog.dismiss();
            }
        });
        dialog.setItemClicklistener(new CustomSearchDialog.ItemClickListenerInterface() {
            @Override
            public void contactClick(AdapterView<?> parent, View view, int position, long id,
                                     boolean isChecked) {
                User us = userList.get(position);
                if (isChecked) {
                    // 选中
                    receivors.add(us);
                } else {
                    // 取消选中
                    receivors.remove(us);
                }
            }

            @Override
            public void searchClick(AdapterView<?> parent, View view, int position, long id,
                                    boolean isChecked) {
                User us = dialog.mSearchList.get(position);
                if (isChecked) {
                    // 选中
                    receivors.add(us);
                } else {
                    // 取消选中
                    receivors.remove(us);
                }
            }
        });
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
            spreceivor.setText(names.toString());
        } else {
            spreceivor.setText(getText(R.string.choose_user));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryUserQuativeAndJunior");
        BaseApplication.getRequestQueue().cancelAll("commitZf");
    }
}
