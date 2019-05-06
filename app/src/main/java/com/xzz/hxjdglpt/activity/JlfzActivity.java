package com.xzz.hxjdglpt.activity;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jlfz;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Yfdx;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 奖励扶助信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_jlfz)
public class JlfzActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.jlfz_commit)
    private Button btnCommit;

    @ViewInject(R.id.jlfz_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.jlfz_lx)
    private AppCompatSpinner spYflx;//优抚类型

    @ViewInject(R.id.jlfz_ldlx)
    private AppCompatSpinner spldlx;//流动类型

    @ViewInject(R.id.jlfz_sfzh)
    private EditText edtCode;//身份证号

    @ViewInject(R.id.jlfz_address)
    private EditText edtAddress;//住址

    @ViewInject(R.id.jlfz_phone)
    private EditText edtPhone;//电话

    @ViewInject(R.id.jlfz_status)
    private TextView tvRzzt;//

    @ViewInject(R.id.jlfz_bz)
    private EditText edtBz;//备注

    private User user;

    private boolean isAdd;//判断是否新增残疾人

    private String gridId;//网格ID

    private Jlfz jlfz;

    private String[] cjType;

    private String[] ldType;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    public String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private LayoutInflater mInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        if (user == null) {
            user = application.getUser();
        }
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText("奖励扶助信息");
        edtBz.setOnTouchListener(this);
        cjType = getResources().getStringArray(R.array.jlfz_type_list);
        ldType = getResources().getStringArray(R.array.ldlx_list);

        //适配器
        ArrayAdapter cjTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, cjType);
        //设置样式
        cjTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spYflx.setAdapter(cjTypeAdpate);

        //适配器
        ArrayAdapter ldTypeAdpate = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, ldType);
        //设置样式
        ldTypeAdpate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spldlx.setAdapter(ldTypeAdpate);


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
                    ToastUtil.show(JlfzActivity.this, "图片数已至上限");
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
        if ((view.getId() == R.id.jlfz_bz && canVerticalScroll(edtBz))) {
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
            jlfz = getIntent().getParcelableExtra("jlfz");
            for (int i = 0; i < cjType.length; i++) {
                if (cjType[i].equals(jlfz.getType())) {
                    spYflx.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < ldType.length; i++) {
                if (ldType[i].equals(jlfz.getLdlx())) {
                    spldlx.setSelection(i);
                    break;
                }
            }
            edtName.setText(jlfz.getName());//姓名
            edtAddress.setText(jlfz.getAddress());//地址
            edtCode.setText(jlfz.getSfzh());
            edtPhone.setText(jlfz.getPhone());
            edtBz.setText(jlfz.getBz());//备注
            if (jlfz.getIsRz() == 0) {
                tvRzzt.setText("未认证");
            } else if (jlfz.getIsRz() == 1) {
                tvRzzt.setText("已认证");
            } else if (jlfz.getIsRz() == 2) {
                tvRzzt.setText("认证中");
            }
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/jlfz/commitJlfz";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        params.put("type", spYflx.getSelectedItem().toString());
        params.put("ldlx", spldlx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            if (edtPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
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
        params.put("picture", sb.toString());
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitJlfz", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JlfzActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlfzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlfzActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.jlfz_commit, R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.jlfz_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitJlfz");
        BaseApplication.getRequestQueue().cancelAll("updateJlfz");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/jlfz/updateJlfz";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(jlfz.getId()));
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        params.put("type", spYflx.getSelectedItem().toString());
        params.put("ldlx", spldlx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtCode.getText().toString())) {
            params.put("sfzh", edtCode.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
            if (edtPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
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
        params.put("picture", sb.toString());
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateJlfz", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JlfzActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JlfzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JlfzActivity.this, R.string.modify_fail);
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

}
