package com.xzz.hxjdglpt.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Zdry;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DensityUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.FileManageUtil;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信息录入
 * Created by dbz on 2017/5/27.
 */
@ContentView(R.layout.aty_zdry)
public class ZdryActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;


    @ViewInject(R.id.zdry_commit)
    private Button btnCommit;

    @ViewInject(R.id.zdry_name)
    private EditText edtName;//姓名

    @ViewInject(R.id.zdry_sfzh)
    private EditText edtCode;//身份证号

    @ViewInject(R.id.zdry_address)
    private EditText edtAddress;//住址

    @ViewInject(R.id.zdry_reason)
    private EditText edtReason;//反映问题

    @ViewInject(R.id.zdry_phone)
    private EditText edtPhone;//手机号

    @ViewInject(R.id.zdry_ld)
    private EditText edtLd;//包案领导
    @ViewInject(R.id.zdry_lddw)
    private EditText edtLddw;//单位
    @ViewInject(R.id.zdry_ldphone)
    private EditText edtLddh;//联系方式

    @ViewInject(R.id.zdry_zrr)
    private EditText edtZrr;//责任人
    @ViewInject(R.id.zdry_zrrdw)
    private EditText edtZrrdw;//单位
    @ViewInject(R.id.zdry_zrrphone)
    private EditText edtZrrdh;//联系方式

    @ViewInject(R.id.zdry_bar)
    private EditText edtBar;//包案人
    @ViewInject(R.id.zdry_bardw)
    private EditText edtBardw;//单位
    @ViewInject(R.id.zdry_barphone)
    private EditText edtBardh;//联系方式


    @ViewInject(R.id.zdry_mj)
    private EditText edtMj;//属地民警
    @ViewInject(R.id.zdry_mjdw)
    private EditText edtMjdw;//单位
    @ViewInject(R.id.zdry_mjphone)
    private EditText edtmjdh;//联系方式


    @ViewInject(R.id.zdry_bz)
    private EditText edtBz;//备注

    private User user;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private Zdry zdry;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    public String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private LayoutInflater mInflater;


    @ViewInject(R.id.zdry_jdhf)
    private LinearLayout tvFile_jd;
    private String pathStr_jd = "";
    private String filename_jd = "";

    @ViewInject(R.id.zdry_qjhf)
    private LinearLayout tvFile_qj;
    private String pathStr_qj = "";
    private String filename_qj = "";

    @ViewInject(R.id.zdry_sjhf)
    private LinearLayout tvFile_sj;
    private String pathStr_sj = "";
    private String filename_sj = "";

    private int flag = 0;//区分选择的是哪个回复：1：街道、2：区级、3市级

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
        edtReason.setOnTouchListener(this);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.zdry_reason && canVerticalScroll(edtReason))) {
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
        tvTitle.setText("信访诉求人员信息");

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
                    ToastUtil.show(ZdryActivity.this, "图片数已至上限");
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

        createDialog();
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
            handler1.sendEmptyMessage(0);
        }
    };

    Handler handler1 = new Handler() {
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
            case 0x0002:
                DialogUtil.createUploadDialog(this, mInflater);
                break;
        }
    }

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            zdry = getIntent().getParcelableExtra("zdry");
            edtName.setText(zdry.getName());//姓名
            edtAddress.setText(zdry.getAddress());//地址
            edtCode.setText(zdry.getSfzh());
            edtReason.setText(zdry.getQuestion());
            edtPhone.setText(zdry.getPhone());
            edtBar.setText(zdry.getBar());
            edtBardh.setText(zdry.getBar_phone());
            edtBardw.setText(zdry.getBar_zw());
            edtLd.setText(zdry.getBald());
            edtLddh.setText(zdry.getBald_phone());
            edtLddw.setText(zdry.getBald_zw());
            edtMj.setText(zdry.getMj());
            edtmjdh.setText(zdry.getMj_phone());
            edtMjdw.setText(zdry.getMj_zw());
            edtZrr.setText(zdry.getZrr());
            edtZrrdh.setText(zdry.getZrr_phone());
            edtZrrdw.setText(zdry.getZrr_zw());
//            edtBz.setText(zdry.getBz());//备注
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/zdry/commitZdry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
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
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("question", edtReason.getText().toString());
        } else {
            params.put("question", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
//            if (edtPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtMj.getText().toString())) {
            params.put("mj", edtMj.getText().toString());
        } else {
            params.put("mj", "");
        }
        if (!TextUtils.isEmpty(edtmjdh.getText().toString())) {
            params.put("mj_phone", edtmjdh.getText().toString());
        } else {
            params.put("mj_phone", "");
        }
        if (!TextUtils.isEmpty(edtMjdw.getText().toString())) {
            params.put("mj_zw", edtMjdw.getText().toString());
        } else {
            params.put("mj_zw", "");
        }

        if (!TextUtils.isEmpty(edtBar.getText().toString())) {
            params.put("bar", edtBar.getText().toString());
        } else {
            params.put("bar", "");
        }
        if (!TextUtils.isEmpty(edtBardw.getText().toString())) {
            params.put("bar_zw", edtBardw.getText().toString());
        } else {
            params.put("bar_zw", "");
        }
        if (!TextUtils.isEmpty(edtBardh.getText().toString())) {
            params.put("bar_phone", edtBardh.getText().toString());
        } else {
            params.put("bar_phone", "");
        }

        if (!TextUtils.isEmpty(edtLd.getText().toString())) {
            params.put("bald", edtLd.getText().toString());
        } else {
            params.put("bald", "");
        }
        if (!TextUtils.isEmpty(edtLddw.getText().toString())) {
            params.put("bald_zw", edtLddw.getText().toString());
        } else {
            params.put("bald_zw", "");
        }
        if (!TextUtils.isEmpty(edtLddh.getText().toString())) {
            params.put("bald_phone", edtLddh.getText().toString());
        } else {
            params.put("bald_phone", "");
        }

        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("zrr", edtZrr.getText().toString());
        } else {
            params.put("zrr", "");
        }
        if (!TextUtils.isEmpty(edtZrrdw.getText().toString())) {
            params.put("zrr_zw", edtZrrdw.getText().toString());
        } else {
            params.put("zrr_zw", "");
        }
        if (!TextUtils.isEmpty(edtZrrdh.getText().toString())) {
            params.put("zrr_phone", edtZrrdh.getText().toString());
        } else {
            params.put("zrr_phone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("jdhf_filename", filename_jd);
        params.put("jdhf_path", pathStr_jd);
        params.put("qjhf_filename", filename_qj);
        params.put("qjhf_path", pathStr_qj);
        params.put("sjhf_filename", filename_sj);
        params.put("sjhf_path", pathStr_sj);
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picture", sb.toString());
        SuccinctProgress.showSuccinctProgress(this, "数据提交中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "commitZdry", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(ZdryActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZdryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZdryActivity.this, R.string.commit_fail);
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

    @Event(value = {R.id.zdry_commit, R.id.iv_back, R.id.zdry_jdhf_add, R.id.zdry_qjhf_add, R.id
            .zdry_sjhf_add}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.zdry_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.zdry_jdhf_add:
                flag = 1;
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
                break;
            case R.id.zdry_qjhf_add:
                flag = 2;
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
                break;
            case R.id.zdry_sjhf_add:
                flag = 3;
                requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("commitZdry");
        BaseApplication.getRequestQueue().cancelAll("updateZdry");
    }


    private void update() {
        String url = ConstantUtil.BASE_URL + "/zdry/updateZdry";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(zdry.getId()));
        params.put("gridId", gridId);
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
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("question", edtReason.getText().toString());
        } else {
            params.put("question", "");
        }
        if (!TextUtils.isEmpty(edtPhone.getText().toString())) {
//            if (edtPhone.getText().toString().length() != 11) {
//                ToastUtil.show(this, "请输入11位手机号");
//                return;
//            }
            params.put("phone", edtPhone.getText().toString());
        } else {
            params.put("phone", "");
        }
        if (!TextUtils.isEmpty(edtMj.getText().toString())) {
            params.put("mj", edtMj.getText().toString());
        } else {
            params.put("mj", "");
        }
        if (!TextUtils.isEmpty(edtmjdh.getText().toString())) {
            params.put("mj_phone", edtmjdh.getText().toString());
        } else {
            params.put("mj_phone", "");
        }
        if (!TextUtils.isEmpty(edtMjdw.getText().toString())) {
            params.put("mj_zw", edtMjdw.getText().toString());
        } else {
            params.put("mj_zw", "");
        }

        if (!TextUtils.isEmpty(edtBar.getText().toString())) {
            params.put("bar", edtBar.getText().toString());
        } else {
            params.put("bar", "");
        }
        if (!TextUtils.isEmpty(edtBardw.getText().toString())) {
            params.put("bar_zw", edtBardw.getText().toString());
        } else {
            params.put("bar_zw", "");
        }
        if (!TextUtils.isEmpty(edtBardh.getText().toString())) {
            params.put("bar_phone", edtBardh.getText().toString());
        } else {
            params.put("bar_phone", "");
        }

        if (!TextUtils.isEmpty(edtLd.getText().toString())) {
            params.put("bald", edtLd.getText().toString());
        } else {
            params.put("bald", "");
        }
        if (!TextUtils.isEmpty(edtLddw.getText().toString())) {
            params.put("bald_zw", edtLddw.getText().toString());
        } else {
            params.put("bald_zw", "");
        }
        if (!TextUtils.isEmpty(edtLddh.getText().toString())) {
            params.put("bald_phone", edtLddh.getText().toString());
        } else {
            params.put("bald_phone", "");
        }
        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("zrr", edtZrr.getText().toString());
        } else {
            params.put("zrr", "");
        }
        if (!TextUtils.isEmpty(edtZrrdw.getText().toString())) {
            params.put("zrr_zw", edtZrrdw.getText().toString());
        } else {
            params.put("zrr_zw", "");
        }
        if (!TextUtils.isEmpty(edtZrrdh.getText().toString())) {
            params.put("zrr_phone", edtZrrdh.getText().toString());
        } else {
            params.put("zrr_phone", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("jdhf_filename", filename_jd);
        params.put("jdhf_path", pathStr_jd);
        params.put("qjhf_filename", filename_qj);
        params.put("qjhf_path", pathStr_qj);
        params.put("sjhf_filename", filename_sj);
        params.put("sjhf_path", pathStr_sj);
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picture", sb.toString());
        SuccinctProgress.showSuccinctProgress(this, "数据修改中···", SuccinctProgress.THEME_ULTIMATE,
                false, true);
        VolleyRequest.RequestPost(this, url, "updateZdry", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(ZdryActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(ZdryActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(ZdryActivity.this, R.string.modify_fail);
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

    private String path;//选择的文件本地路径


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantUtil.TAKE_PHOTO_WITH_DATA:
                path = CameraUtil.tempPath;
                Bitmap bm = CompressImageUtil.getSmallBitmap(path);
                Bitmap realBm = BitmapUtil.addTimeFlag(bm);
                File f = CompressImageUtil.putImage(realBm, this);
                if (f.exists()) {
                    path = f.getAbsolutePath();
                    uploadFile(f.getAbsolutePath());
                }
                break;
            case 1:
                if (data == null) {
                    ToastUtil.show(this, "没有文件，请重新选择");
                    return;
                }
                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                    uploadFile(path);
                    return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = FileManageUtil.getPath(this, uri);
                    uploadFile(path);
                } else {//4.4以下下系统调用方法
//                    path = FileManageUtil.getRealPathFromURI(uri);
                    path = FileManageUtil.getPath(this, uri);
                    uploadFile(path);
                }
                break;
        }
    }

    private ProgressDialog dialog = null;

    /**
     * 上传进度框
     */
    private void createDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("上传进度提示");
        dialog.setMax(100);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OkHttpManager.DOWNLOAD_SUCCESS:
                    ToastUtil.show(ZdryActivity.this, R.string.upload_image_success);
                    times = 0;
                    dialog.dismiss();
                    if (msg.obj != null) {
                        String obj = String.valueOf(msg.obj);
                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(obj, new TypeToken<Map<String,
                                String>>() {
                        }.getType());
                        String resultCode = map.get("resultCode");
                        // resultCode 1:上传成功 ；2：token不一致；3：上传失败
                        if ("1".equals(resultCode)) {
                            String picPath = map.get("attachFilePath");
                            String[] paths = path.split("/");
                            String[] pss = null;
                            String[] psn = null;
                            switch (flag) {
                                case 1:
                                    filename_jd = filename_jd + "," + paths[paths.length - 1];
                                    pathStr_jd = pathStr_jd + "," + picPath;
                                    tvFile_jd.removeAllViews();
                                    pss = pathStr_jd.split(",");
                                    psn = filename_jd.split(",");
                                    break;
                                case 2:
                                    filename_qj = filename_qj + "," + paths[paths.length - 1];
                                    pathStr_qj = pathStr_qj + "," + picPath;
                                    tvFile_qj.removeAllViews();
                                    pss = pathStr_qj.split(",");
                                    psn = filename_qj.split(",");
                                    break;
                                case 3:
                                    filename_sj = filename_sj + "," + paths[paths.length - 1];
                                    pathStr_sj = pathStr_sj + "," + picPath;
                                    tvFile_sj.removeAllViews();
                                    pss = pathStr_sj.split(",");
                                    psn = filename_sj.split(",");
                                    break;
                            }

                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < pss.length; i++) {
                                if (pss[i] != null && !TextUtils.isEmpty(pss[i])) {
                                    if (psn[i].endsWith(".jpg") || psn[i].endsWith(".png") ||
                                            psn[i].endsWith(".JPG") || psn[i].endsWith("" + "" +
                                            ".PNG") || psn[i].endsWith(".jpeg") || psn[i]
                                            .endsWith(".JPEG") || psn[i].endsWith("" + ".BMP") ||
                                            psn[i].endsWith(".bmp") || psn[i].endsWith("" + "" +
                                            ".gif") || psn[i].endsWith("" + ".GIF")) {
                                        list.add(pss[i]);
                                    }
                                }
                            }
                            final String[] values = (String[]) list.toArray(new String[list.size
                                    ()]);
                            if (pss.length == psn.length) {
                                for (int i = 0; i < pss.length; i++) {
                                    final String val = pss[i];
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        TextView tv = new TextView(ZdryActivity.this);
                                        LinearLayout.LayoutParams lp = new LinearLayout
                                                .LayoutParams(LinearLayout.LayoutParams
                                                .WRAP_CONTENT, LinearLayout.LayoutParams
                                                .WRAP_CONTENT);
                                        tv.setLayoutParams(lp);
                                        int pxValue = DensityUtil.dip2px(ZdryActivity.this, 5);
                                        tv.setPadding(pxValue, pxValue, pxValue, pxValue);
                                        tv.setTextColor(getResources().getColor(R.color.title_bg));
                                        tv.setText(psn[i]);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (val.endsWith(".jpg") || val.endsWith(".png")
                                                        || val.endsWith(".JPG") || val.endsWith
                                                        ("" + ".PNG") || val.endsWith(".jpeg") ||
                                                        val.endsWith(".JPEG") || val.endsWith(""
                                                        + "" + ".BMP") || val.endsWith(".bmp") ||
                                                        val.endsWith(".gif") || val.endsWith("" +
                                                        ".GIF")) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("imagesName", values);
                                                    intent.putExtra("index", 0);
                                                    intent.setClass(ZdryActivity.this,
                                                            ShowImageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    ToastUtil.show(ZdryActivity.this, R.string
                                                            .file_tip);
                                                }
                                            }
                                        });
                                        switch (flag) {
                                            case 1:
                                                tvFile_jd.addView(tv);
                                                break;
                                            case 2:
                                                tvFile_qj.addView(tv);
                                                break;
                                            case 3:
                                                tvFile_sj.addView(tv);
                                                break;
                                        }
                                    }
                                }
                            }
                            ToastUtil.show(ZdryActivity.this, R.string.upload_image_success);
                        } else if ("2".equals(resultCode)) {
                            DialogUtil.showTipsDialog(ZdryActivity.this);
                        } else if ("3".equals(resultCode)) {
                            ToastUtil.show(ZdryActivity.this, R.string.upload_image_fail);
                        }
                    }
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(ZdryActivity.this, R.string.upload_image_fail);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(ZdryActivity.this, R.string.net_unuser);
                    times = 0;
                    dialog.dismiss();
                    break;
                default:
                    dialog.setProgress(msg.what);
                    break;
            }
        }
    };

    private int times = 0;

    private void uploadFile(String path) {
        dialog.show();
        File file = new File(path);
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        OkHttpManager.upload(url, new File[]{file}, new String[]{"file"}, params, new
                OkHttpManager.ProgressListener() {  //在这里监听上传进度
            @Override
            public void onProgress(long totalSize, long currSize, boolean done, int id) {
                int p = (int) (((float) currSize / (float) totalSize) * 100);
                if (times >= 64 || p == 100) {
                    times = 0;
                    Message msg = Message.obtain();
                    msg.what = p;
                    handler.sendMessage(msg);
                }
                times++;
            }
        }, new OkHttpManager.ResultCallback() {    //在这里显示回调信息
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state == OkHttpManager.State.SUCCESS) {
                    Message msg = Message.obtain();
                    msg.what = OkHttpManager.DOWNLOAD_SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else if (state == OkHttpManager.State.FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.DOWNLOAD_FAIL);
                } else if (state == OkHttpManager.State.NETWORK_FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.NETWORK_FAILURE);
                }
            }
        });
    }

}
