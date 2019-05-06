package com.xzz.hxjdglpt.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Dbry;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 低保人员信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_dbry)
public class DbryActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.dbry_type)
    private AppCompatSpinner dbType;
    @ViewInject(R.id.dbry_commit)
    private Button btnCommit;

    @ViewInject(R.id.dbry_name)
    private EditText edtName;//姓名
    @ViewInject(R.id.dbry_dbzh)
    private EditText edtDbzh;//低保证号
    @ViewInject(R.id.dbry_sfzh)
    private EditText edtSfzh;//身份证号
    @ViewInject(R.id.dbry_dbs)
    private EditText edtDbs;//低保人口数
    @ViewInject(R.id.dbry_wgy)
    private EditText edtWgy;//网格员
    @ViewInject(R.id.dbry_wgyphone)
    private EditText edtWgyphone;//联系电话
    //    @ViewInject(R.id.dbry_birth)
//    private EditText edtBirth;//出生日月
//    @ViewInject(R.id.dbry_address)
//    private EditText edtAddress;//住址
    @ViewInject(R.id.dbry_bz)
    private EditText edtBz;//备注
    @ViewInject(R.id.dbry_reason)
    private EditText edtReason;//低保原因

    private User user;

    private boolean isAdd;//判断是否新增残疾人

    private String gridId;//网格ID


    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    public String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private LayoutInflater mInflater;

    private Dbry dbry;

    private String[] types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (user == null) {
            user = application.getUser();
        }
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        edtBirth.setOnFocusChangeListener(this);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("低保人员信息");
        types = getResources().getStringArray(R.array.dbtype);
        edtBz.setOnTouchListener(this);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(this, android.R.layout
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
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.griditem_addpic, new
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
                    ToastUtil.show(DbryActivity.this, "图片数已至上限");
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                            .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.dbry_bz && canVerticalScroll(edtBz))) {
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

    @Override
    public void onResume() {
        super.onResume();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
                DialogUtil.createDialog(this, mInflater);
                break;
        }
    }


    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            dbry = getIntent().getParcelableExtra("dbry");
            if ("0".equals(dbry.getType())) {
                dbType.setSelection(0);
            } else if ("1".equals(dbry.getType())) {
                dbType.setSelection(1);
            }
            edtName.setText(dbry.getName());//姓名
            edtDbzh.setText(dbry.getDbCode());//残疾证号
            edtDbs.setText(dbry.getpNum());//低保人数
            edtBz.setText(dbry.getBz());//备注
            edtWgy.setText(dbry.getWgy());
            edtWgyphone.setText(dbry.getWgyphone());
//            if (!TextUtils.isEmpty(dbry.getSfzh())) {
//                String sfzh = dbry.getSfzh().substring(0, dbry.getSfzh().length() - 4);
//                edtSfzh.setText(sfzh + "****");
//            }
            edtSfzh.setText(dbry.getSfzh());
            edtReason.setText(dbry.getReason());
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/dbry/commitDbry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        if (types[0].equals(dbType.getSelectedItem().toString())) {
            params.put("type", "0");
        } else if (types[1].equals(dbType.getSelectedItem().toString())) {
            params.put("type", "1");
        }
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtDbs.getText().toString())) {
            params.put("dbs", edtDbs.getText().toString());
        } else {
            params.put("dbs", "0");
        }
        if (!TextUtils.isEmpty(edtDbzh.getText().toString())) {
            params.put("dbzh", edtDbzh.getText().toString());
        } else {
            params.put("dbzh", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("sfzh", edtSfzh.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtWgy.getText().toString())) {
            params.put("wgy", edtWgy.getText().toString());
        } else {
            params.put("wgy", "");
        }
        if (!TextUtils.isEmpty(edtWgyphone.getText().toString())) {
            params.put("wgyphone", edtWgyphone.getText().toString());
        } else {
            params.put("wgyphone", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("reason", edtReason.getText().toString());
        } else {
            params.put("reason", "");
        }
//        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
//            params.put("address", edtAddress.getText().toString());
//        } else {
//            params.put("address", "");
//        }
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
        VolleyRequest.RequestPost(this, url, "dbry_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(DbryActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DbryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DbryActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.dbry_commit, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.dbry_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void update() {
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        String url = ConstantUtil.BASE_URL + "/dbry/updateDbry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", dbry.getId());
        params.put("gId", gridId);
        if (types[0].equals(dbType.getSelectedItem().toString())) {
            params.put("type", "0");
        } else if (types[1].equals(dbType.getSelectedItem().toString())) {
            params.put("type", "1");
        }

        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtDbs.getText().toString())) {
            params.put("dbs", edtDbs.getText().toString());
        } else {
            params.put("dbs", "0");
        }
        if (!TextUtils.isEmpty(edtDbzh.getText().toString())) {
            params.put("dbzh", edtDbzh.getText().toString());
        } else {
            params.put("dbzh", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("sfzh", edtSfzh.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtWgy.getText().toString())) {
            params.put("wgy", edtWgy.getText().toString());
        } else {
            params.put("wgy", "");
        }
        if (!TextUtils.isEmpty(edtWgyphone.getText().toString())) {
            params.put("wgyphone", edtWgyphone.getText().toString());
        } else {
            params.put("wgyphone", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("reason", edtReason.getText().toString());
        } else {
            params.put("reason", "");
        }
//        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
//            params.put("address", edtAddress.getText().toString());
//        } else {
//            params.put("address", "");
//        }
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
        VolleyRequest.RequestPost(this, url, "updateDbry", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(DbryActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(DbryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(DbryActivity.this, R.string.modify_fail);
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

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("village_queryList");
        BaseApplication.getRequestQueue().cancelAll("grid_queryList");
        BaseApplication.getRequestQueue().cancelAll("dbry_commit");
        BaseApplication.getRequestQueue().cancelAll("updateDbry");
    }


}
