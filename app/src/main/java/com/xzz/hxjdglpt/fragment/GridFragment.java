package com.xzz.hxjdglpt.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.AppCompatSpinner;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.CscxglActivity;
import com.xzz.hxjdglpt.activity.MzActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.activity.ShowImageActivity;
import com.xzz.hxjdglpt.activity.TaskDetailNewActivity;
import com.xzz.hxjdglpt.customview.CustomSearchDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.BitmapUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 网格信息
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.fragment_grid)
public class GridFragment extends BaseFragment implements OnItemSelectedListener,
        PermissionsResultListener {

    @ViewInject(R.id.grid_v_name)
    private AppCompatSpinner spVillage;
    @ViewInject(R.id.grid_name)
    private AppCompatSpinner spGrid;
    @ViewInject(R.id.grid_commit)
    private Button btnCommit;
    @ViewInject(R.id.grid_area)
    private EditText edtArea;//面积
    @ViewInject(R.id.grid_pnum)
    private EditText edtPNum;//人口数量
    @ViewInject(R.id.grid_hnum)
    private EditText edtHNum;//户数
    @ViewInject(R.id.grid_wnum)
    private EditText edtWNum;//外来人口数

    @ViewInject(R.id.grid_zrr)
    private TextView edtZrr;//责任人

    private User user;
    private BaseApplication application;

    private List<Village> villages;
    private List<Grid> grids;

    private ArrayAdapter<String> arr_adapter;

    private ArrayAdapter<String> grid_adapter;

    private List<User> userList = new ArrayList<>();

    private ArrayList<User> receivors = new ArrayList<>();

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
        initView();
        initData();
        loadUser();
        return view;
    }

    public void initView() {
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
                            .WRITE_EXTERNAL_STORAGE}, 1, GridFragment.this);
                    //通过onResume()刷新数据
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("imagesName", picPath.split(","));
                    intent.putExtra("index", position - 1);
                    intent.setClass(getActivity(), ShowImageActivity.class);
                    startActivity(intent);
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

    public String getPathStr() {
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        return sb.toString();
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

    public Runnable runnable1 = new Runnable() {
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
                picPath = getPathStr();
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
        String url = ConstantUtil.BASE_URL + "/grid/commitGrid";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        if (spGrid.getSelectedItem() != null && !TextUtils.isEmpty(spGrid.getSelectedItem()
                .toString())) {
            params.put("id", spGrid.getSelectedItem().toString());
            if (!TextUtils.isEmpty(edtArea.getText().toString())) {
                params.put("area", edtArea.getText().toString());
            } else {
                params.put("area", "0");
            }
            if (!TextUtils.isEmpty(edtPNum.getText().toString())) {
                params.put("pnum", edtPNum.getText().toString());
            } else {
                params.put("pnum", "0");
            }
            if (!TextUtils.isEmpty(edtHNum.getText().toString())) {
                params.put("hnum", edtHNum.getText().toString());
            } else {
                params.put("hnum", "0");
            }
            if (!TextUtils.isEmpty(edtWNum.getText().toString())) {
                params.put("wnum", edtWNum.getText().toString());
            } else {
                params.put("wnum", "0");
            }
            if (!TextUtils.isEmpty(zrr.toString())) {
                params.put("zrr", zrr.toString());
            }
            params.put("picpath", getPathStr());
        }
        VolleyRequest.RequestPost(getActivity(), url, "grid_commit", params, new
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

    @Event(value = {R.id.grid_commit, R.id.grid_zrr}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_commit:
                commit();
                break;
            case R.id.grid_zrr:
                showMutilAlertDialog("请选择网格责任人");
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("village_queryList");
        BaseApplication.getRequestQueue().cancelAll("grid_queryList");
        BaseApplication.getRequestQueue().cancelAll("grid_commit");
        BaseApplication.getRequestQueue().cancelAll("queryGridById");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edtArea.setText("");//面积
        edtPNum.setText("");//人口数量
        edtHNum.setText("");//户数
        edtWNum.setText("");//外来人口数
        imageItem.clear();
        mPath.clear();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter.notifyDataSetChanged();
        switch (parent.getId()) {
            case R.id.grid_v_name:
                loadGrid(villages.get(position).getId());
                break;
            case R.id.grid_name:
                loadGridById(grids.get(position).getId());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String picPath = "";

    private void loadGridById(String id) {
        SuccinctProgress.showSuccinctProgress(getActivity(), "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/grid/queryGridById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", id);
        VolleyRequest.RequestPost(getActivity(), url, "queryGridById", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
                        SuccinctProgress.dismiss();
                        try {
                            String resultCode = result.getString("resultCode");
                            String zrr = result.getString("zrr");
                            if (zrr != null) {
                                edtZrr.setText(zrr);
                            }
                            // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                            if ("1".equals(resultCode)) {
                                Gson gson = new Gson();
                                Grid g = (Grid) gson.fromJson(result.getJSONObject("data").toString(),
                                        Grid.class);
                                edtArea.setText(g.getArea() == null ? "" : g.getArea());
                                edtPNum.setText(String.valueOf(g.getRkNum()));
                                edtHNum.setText(String.valueOf(g.gethNum()));
                                edtWNum.setText(String.valueOf(g.getWqNum()));
                                if (g.getPicpath() != null) {
                                    picPath = g.getPicpath();
                                }
                                new Thread(runnable).start();
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String[] ps = picPath.split(",");
            for (int i = 0; i < ps.length; i++) {
                if (!TextUtils.isEmpty(ps[i])) {
                    mPath.add(ps[i]);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", BitmapUtil.getNetBitmap(getActivity(), ConstantUtil
                            .FILE_DOWNLOAD_URL + ps[i]));
                    imageItem.add(map);
                }
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

    private void loadUser() {
        String url = ConstantUtil.BASE_URL + "/user/queryGridZrr";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(getActivity(), url, "queryUsers", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        LogUtil.i("onMySuccess");
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
                        // 返回失败的原因
                        LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                    }
                });
    }

    /**
     * 将list转为string数组
     */
    public String[] getRealNameList() {
        String[] names = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
            if (u.getRealName() == null) {
                names[i] = u.getUserName();
            } else {
                names[i] = u.getRealName();
            }
        }
        return names;
    }

    public void showMutilAlertDialog(String title) {
        final CustomSearchDialog dialog = new CustomSearchDialog(getActivity(), userList);
        dialog.show();
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
                LogUtil.i("isChecked=" + isChecked);
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


    private StringBuffer zrrnames = new StringBuffer();//展示姓名
    private StringBuffer zrr = new StringBuffer();//USERID拼接，作为参数传到后台

    private void setName() {
        zrrnames.delete(0, zrrnames.length());
        zrr.delete(0, zrr.length());
        for (User u : receivors) {
            zrrnames.append(u.getRealName()).append(",");
            zrr.append(u.getUserId()).append(",");
        }
        if (receivors.size() > 0) {
            edtZrr.setText(zrrnames.toString());
        } else {
            edtZrr.setText(getText(R.string.choose_zrr));
        }
    }
}
