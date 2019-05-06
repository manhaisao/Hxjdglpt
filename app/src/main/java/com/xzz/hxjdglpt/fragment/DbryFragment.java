package com.xzz.hxjdglpt.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 低保人员信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.fragment_dbry)
public class DbryFragment extends BaseFragment implements OnItemSelectedListener, View
        .OnFocusChangeListener, PermissionsResultListener {

    @ViewInject(R.id.dbry_v_name)
    private AppCompatSpinner spVillage;
    @ViewInject(R.id.dbry_grid_name)
    private AppCompatSpinner spGrid;
    @ViewInject(R.id.dbry_type)
    private AppCompatSpinner dbType;
    @ViewInject(R.id.dbry_commit)
    private Button btnCommit;

    @ViewInject(R.id.dbry_name)
    private EditText edtName;//姓名
    @ViewInject(R.id.dbry_birth)
    private EditText edtBirth;//出生日月
    @ViewInject(R.id.dbry_address)
    private EditText edtAddress;//住址
    @ViewInject(R.id.dbry_bz)
    private EditText edtBz;//备注

    private User user;
    private BaseApplication application;

    private List<Village> villages;
    private List<Grid> grids;

    private ArrayAdapter<String> arr_adapter;

    private ArrayAdapter<String> grid_adapter;

    //定义显示时间控件
    private Calendar calendar; //通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    public String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getActivity().getSystemService(getActivity()
                .LAYOUT_INFLATER_SERVICE);
        spVillage.setOnItemSelectedListener(this);
        spGrid.setOnItemSelectedListener(this);
        edtBirth.setOnFocusChangeListener(this);
        initView();
        initData();
        return view;
    }

    public void initView() {
        String[] types = getResources().getStringArray(R.array.dbtype);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_spinner_item, types);
        //设置样式
        typeAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        dbType.setAdapter(typeAd);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(getActivity(), imageItem, R.layout.griditem_addpic, new
                String[]{"itemImage"}, new int[]{R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0 && imageItem.size() == ConstantUtil.ALL_IMAGE_COUNT) { //第一张为默认图片
                    ToastUtil.show(getActivity(), "图片数已至上限");
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    performRequestPermissions("", new String[]{Manifest.permission
                            .WRITE_EXTERNAL_STORAGE}, 1, DbryFragment.this);
                    //通过onResume()刷新数据
                }
            }
        });

        gridView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                if (position != 0) {
                    dialog(position);
                }
                return true;
            }
        });
    }


    //刷新图片
    @Override
    public void onResume() {
        super.onResume();
//        if (!TextUtils.isEmpty(pathImage)) {
//            Bitmap addbmp = CompressImageUtil.getSmallBitmap(pathImage);
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("itemImage", addbmp);
//            imageItem.add(map);
//            simpleAdapter = new SimpleAdapter(getActivity(), imageItem, R.layout.griditem_addpic,
//                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
//            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//                @Override
//                public boolean setViewValue(View view, Object data, String textRepresentation) {
//                    // TODO Auto-generated method stub
//                    if (view instanceof ImageView && data instanceof Bitmap) {
//                        ImageView i = (ImageView) view;
//                        i.setImageBitmap((Bitmap) data);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            gridView1.setAdapter(simpleAdapter);
//            simpleAdapter.notifyDataSetChanged();
//            //刷新后释放防止手机休眠后自动添加
//            pathImage = null;
//        }
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(pathImage)) {
                Bitmap addbmp = CompressImageUtil.getSmallBitmap(pathImage);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", addbmp);
                imageItem.add(map);
                //刷新后释放防止手机休眠后自动添加
            }
            handler.sendEmptyMessage(0);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            simpleAdapter.notifyDataSetChanged();
        }
    };

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
                mPath.remove(position - 1);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public ArrayList<String> mPath = new ArrayList<>();


    @Override
    public void onPermissionGranted() {
        DialogUtil.createDialog(getActivity(), mInflater);
    }

    @Override
    public void onPermissionDenied() {

    }

    private void loadGrid(String vId) {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/grid/queryGridList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("vId", vId);
        VolleyRequest.RequestPost(getActivity(), url, "grid_queryList", params, new
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
                        grids = gson.fromJson(jsonArray.toString(), new TypeToken<List<Grid>>() {
                        }.getType());
                        ArrayList<String> names = new ArrayList<String>();
                        for (Grid grid : grids) {
                            names.add(grid.getId());
                        }
                        //适配器
                        grid_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                                .simple_spinner_item, names);
                        //设置样式
                        grid_adapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        //加载适配器
                        spGrid.setAdapter(grid_adapter);
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
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });

    }


    private void initData() {
        calendar = Calendar.getInstance();
        request();
    }


    private void request() {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/village/queryVillageList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "village_queryList", params, new
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
                        villages = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Village>>() {
                        }.getType());
                        ArrayList<String> names = new ArrayList<String>();
                        for (Village v : villages) {
                            names.add(v.getName());
                        }
                        //适配器
                        arr_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                                .simple_spinner_item, names);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        //加载适配器
                        spVillage.setAdapter(arr_adapter);
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
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }


    /**
     * 提交信息
     */
    private void commit() {
        SuccinctProgress.showSuccinctProgress(getActivity(), "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/dbry/commitDbry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (spGrid.getSelectedItem() != null && !TextUtils.isEmpty(spGrid.getSelectedItem()
                .toString())) {
            params.put("id", spGrid.getSelectedItem().toString());
            params.put("type", dbType.getSelectedItem().toString());
            if (!TextUtils.isEmpty(edtName.getText().toString())) {
                params.put("name", edtName.getText().toString());
            } else {
                params.put("name", "");
            }
            if (!TextUtils.isEmpty(edtBirth.getText().toString())) {
                params.put("birth", edtBirth.getText().toString());
            } else {
                params.put("birth", "");
            }
            if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
                params.put("address", edtAddress.getText().toString());
            } else {
                params.put("address", "");
            }
            if (!TextUtils.isEmpty(edtBz.getText().toString())) {
                params.put("bz", edtBz.getText().toString());
            } else {
                params.put("bz", "");
            }
            StringBuffer sb = new StringBuffer();
            for (String str : mPath) {
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str).append(",");
                }
            }
            params.put("picpath", sb.toString());
        }
        VolleyRequest.RequestPost(getActivity(), url, "dbry_commit", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.commit_success);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(getActivity());
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.commit_fail);
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

    @Event(value = {R.id.dbry_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.dbry_commit:
                commit();
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("village_queryList");
        BaseApplication.getRequestQueue().cancelAll("grid_queryList");
        BaseApplication.getRequestQueue().cancelAll("dbry_commit");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edtName.setText("");//姓名
        edtBirth.setText("");//出生日月
        edtAddress.setText("");//住址
        edtBz.setText("");//备注
        imageItem.clear();
        mPath.clear();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter.notifyDataSetChanged();
        switch (parent.getId()) {
            case R.id.dbry_v_name:
                loadGrid(villages.get(position).getId());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                mYear = year;
                mMonth = month;
                mDay = day;
                //更新EditText控件日期 小于10加0
                edtBirth.setText(new StringBuilder().append(mYear).append("-").append((mMonth +
                        1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append((mDay <
                        10) ? "0" + mDay : mDay));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
                .DAY_OF_MONTH)).show();
    }
}
