package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Jzgd;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.CameraUtil;
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
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 建筑工地
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_jzgd)
public class JzgdActivity extends BaseActivity implements View.OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.magement_commit)
    private Button btnCommit;

    @ViewInject(R.id.c_lxr)
    private EditText edtLxr;//法人
    @ViewInject(R.id.c_lxr_phone)
    private EditText edtLxrPhone;//联系号码
    @ViewInject(R.id.m_bz)
    private EditText edtBz;//备注
    @ViewInject(R.id.m_name)
    private EditText edtName;//商户名称
    @ViewInject(R.id.m_name_u)
    private EditText edtName_;//建筑工地名称
    @ViewInject(R.id.m_sgf)
    private EditText edtSgf;//施工方
    @ViewInject(R.id.c_aqzrr)
    private EditText edtAqzrr;//安全责任人
    @ViewInject(R.id.c_aqzrrphone)
    private EditText edtAqzrrDh;//安全责任人电话
    @ViewInject(R.id.c_aqy)
    private EditText edtAqy;//安全责任人
    @ViewInject(R.id.c_aqyphone)
    private EditText edtAqyDh;//安全员电话

    @ViewInject(R.id.c_lx)
    private AppCompatSpinner spQylx;//应急器具配备情况
    @ViewInject(R.id.c_zczb)
    private EditText edtZczb;//注册资本

    @ViewInject(R.id.m_sddw)
    private EditText edtSsdw;//属地单位
    @ViewInject(R.id.c_wgzrr)
    private EditText edtWgzrr;//网格责任人
    @ViewInject(R.id.c_wgzrr_phone)
    private EditText edtWgzrrdh;//网格责任人电话
    @ViewInject(R.id.c_hszrr)
    private EditText edtHsr;//护税责任人
    @ViewInject(R.id.c_hszrr_phone)
    private EditText edtHsrDh;//护税责任人

    private User user;
    private BaseApplication application;

    @ViewInject(R.id.m_yyzz)
    private GridView gridView1;                   //网格显示缩略图
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private LayoutInflater mInflater;

    private String gridId;//网格ID

    private boolean isAdd;//判断是否新增

    private Jzgd jzgd;

    private String[] types;//类型

    private String picPath = "";

    @ViewInject(R.id.jzgd_kptj)
    private RelativeLayout mLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText("建筑工地");
        application = (BaseApplication) getApplication();
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (user == null) {
            user = application.getUser();
        }
        gridId = getIntent().getStringExtra("gridId");
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        edtSsdw.setOnTouchListener(this);
        edtBz.setOnTouchListener(this);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.m_sddw && canVerticalScroll(edtSsdw))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.m_bz && canVerticalScroll(edtBz))) {
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


    @Event(value = {R.id.iv_back, R.id.magement_commit, R.id.jd1, R.id.jd2, R.id.jd3, R.id.jd4}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.magement_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.jd1:
                if (jzgd != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, KpqkActivity.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "1");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JzgdActivity.this, "暂无开票情况");
                }
                break;
            case R.id.jd2:
                if (jzgd != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, KpqkActivity.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "2");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JzgdActivity.this, "暂无开票情况");
                }
                break;
            case R.id.jd3:
                if (jzgd != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, KpqkActivity.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "3");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JzgdActivity.this, "暂无开票情况");
                }
                break;
            case R.id.jd4:
                if (jzgd != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, KpqkActivity.class);
                    intent.putExtra("comId", String.valueOf(jzgd.getId()));
                    intent.putExtra("type", "4");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JzgdActivity.this, "暂无开票情况");
                }
                break;
        }
    }

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            jzgd = getIntent().getParcelableExtra("jzgd");
            if (types[0].equals(jzgd.getPbqk())) {
                spQylx.setSelection(0);
            } else if (types[1].equals(jzgd.getPbqk())) {
                spQylx.setSelection(1);
            }
            edtName_.setText(jzgd.getName());
            edtName.setText(jzgd.getYzname());//姓名
            edtLxr.setText(jzgd.getFr());
            edtLxrPhone.setText(jzgd.getFrdh());//
            edtSgf.setText(jzgd.getSgf());
            edtAqzrr.setText(jzgd.getAqzrr());
            edtAqzrrDh.setText(jzgd.getAqzrrdh());
            edtZczb.setText(jzgd.getZczb());
            edtSsdw.setText(jzgd.getSddw());
            edtWgzrr.setText(jzgd.getWgzrr());
            edtWgzrrdh.setText(jzgd.getWgzrrdh());
            edtHsr.setText(jzgd.getHszrr());
            edtHsrDh.setText(jzgd.getHszrrdh());
            edtBz.setText(jzgd.getBz());//备注
            edtAqy.setText(jzgd.getAqy());
            edtAqyDh.setText(jzgd.getAqydh());
            if (jzgd.getPicpath() != null) {
                picPath = jzgd.getPicpath();
                SuccinctProgress.showSuccinctProgress(JzgdActivity.this, "图片努力加载中···",
                        SuccinctProgress.THEME_ULTIMATE, false, true);
                new Thread(runnable1).start();
            }
            mLay.setVisibility(View.VISIBLE);
        } else {
            mLay.setVisibility(View.GONE);
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }

    public void initView() {
        types = getResources().getStringArray(R.array.pbqk);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, types);
        //设置样式
        typeAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spQylx.setAdapter(typeAd);

        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.yyzz_add_pic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.yyzz_add_pic, new
                String[]{"itemImage"}, new int[]{R.id.yyzz_add_pc});
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
                    ToastUtil.show(JzgdActivity.this, "图片数已至上限");
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //选择图片
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
//                            .Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, IMAGE_OPEN);

                    requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                            .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
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

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String[] ps = picPath.split(",");
            for (int i = 0; i < ps.length; i++) {
                if (!TextUtils.isEmpty(ps[i])) {
                    mPath.add(ps[i]);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", BitmapUtil.getNetBitmap(JzgdActivity.this,
                            ConstantUtil.FILE_DOWNLOAD_URL + ps[i]));
                    imageItem.add(map);
                }
            }
            handler.sendEmptyMessage(0);
        }
    };

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ConstantUtil.TAKE_PHOTO_WITH_DATA:
                pathImage = CameraUtil.tempPath;
                File f = new File(pathImage);
                if (f.exists()) {
                    uploadFile(pathImage);
                }
                break;
            case ConstantUtil.RESULT_LOAD_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (null != data) {
                            Uri uri = data.getData();
                            if (!TextUtils.isEmpty(uri.getAuthority())) {
                                //查询选择图片
                                Cursor cursor = getContentResolver().query(uri, new
                                        String[]{MediaStore.Images.Media.DATA}, null, null, null);
                                //返回 没找到选择图片
                                if (null == cursor) {
                                    return;
                                }
                                //光标移动至开头 获取图片路径
                                cursor.moveToFirst();
                                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore
                                        .Images.Media.DATA));
                                File f1 = new File(pathImage);
                                if (f1.exists()) {
                                    uploadFile(pathImage);
                                }
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    Runnable runnable = new Runnable() {
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
            SuccinctProgress.dismiss();
            simpleAdapter.notifyDataSetChanged();
        }
    };

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(JzgdActivity.this);
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

    private ArrayList<String> mPath = new ArrayList<>();

    private Callback.Cancelable post;

    private void uploadFile(final String path) {
        SuccinctProgress.showSuccinctProgress(JzgdActivity.this, "图片上传中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(path));
        params.addParameter("userId", user.getUserId());
        params.addParameter("token", user.getToken());
        post = x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:上传成功 ；2：token不一致；3：上传失败
                    if ("1".equals(resultCode)) {
                        String picPath = result.getString("attachFilePath");
                        mPath.add(picPath);
                        new Thread(runnable).start();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JzgdActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JzgdActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(JzgdActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/jzgd/commitJzgd";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gId", gridId);
        params.put("pbqk", spQylx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtLxr.getText().toString())) {
            params.put("fr", edtLxr.getText().toString());
        } else {
            params.put("fr", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone.getText().toString())) {
            if (edtLxrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("frdh", edtLxrPhone.getText().toString());
        } else {
            params.put("frdh", "");
        }
        if (!TextUtils.isEmpty(edtAqzrr.getText().toString())) {
            params.put("aqzrr", edtAqzrr.getText().toString());
        } else {
            params.put("aqzrr", "");
        }
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("yzname", edtName.getText().toString());
        } else {
            params.put("yzname", "");
        }
        if (!TextUtils.isEmpty(edtName_.getText().toString())) {
            params.put("name", edtName_.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtAqzrrDh.getText().toString())) {
            params.put("aqzrrdh", edtAqzrrDh.getText().toString());
        } else {
            params.put("aqzrrdh", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picpath", sb.toString());
        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyDh.getText().toString())) {
            params.put("aqydh", edtAqyDh.getText().toString());
        } else {
            params.put("aqydh", "");
        }
        if (!TextUtils.isEmpty(edtSsdw.getText().toString())) {
            params.put("sddw", edtSsdw.getText().toString());
        } else {
            params.put("sddw", "");
        }
        if (!TextUtils.isEmpty(edtWgzrr.getText().toString())) {
            params.put("wgzrr", edtWgzrr.getText().toString());
        } else {
            params.put("wgzrr", "");
        }
        if (!TextUtils.isEmpty(edtWgzrrdh.getText().toString())) {
            params.put("wgzrrdh", edtWgzrrdh.getText().toString());
        } else {
            params.put("wgzrrdh", "");
        }
        if (!TextUtils.isEmpty(edtSgf.getText().toString())) {
            params.put("sgf", edtSgf.getText().toString());
        } else {
            params.put("sgf", "");
        }
        if (!TextUtils.isEmpty(edtZczb.getText().toString())) {
            params.put("zczb", edtZczb.getText().toString());
        } else {
            params.put("zczb", "");
        }
        if (!TextUtils.isEmpty(edtHsr.getText().toString())) {
            params.put("hszrr", edtHsr.getText().toString());
        } else {
            params.put("hszrr", "");
        }
        if (!TextUtils.isEmpty(edtHsrDh.getText().toString())) {
            params.put("hszrrdh", edtHsrDh.getText().toString());
        } else {
            params.put("hszrrdh", "");
        }
        SuccinctProgress.showSuccinctProgress(JzgdActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "management_commit", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            if ("1".equals(resultCode)) {
                                ToastUtil.show(JzgdActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(JzgdActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(JzgdActivity.this, R.string.commit_fail);
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
    private void update() {
        String url = ConstantUtil.BASE_URL + "/jzgd/updateJzgd";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(jzgd.getId()));
        params.put("gId", gridId);
        params.put("pbqk", spQylx.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtLxr.getText().toString())) {
            params.put("fr", edtLxr.getText().toString());
        } else {
            params.put("fr", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone.getText().toString())) {
            if (edtLxrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("frdh", edtLxrPhone.getText().toString());
        } else {
            params.put("frdh", "");
        }
        if (!TextUtils.isEmpty(edtAqzrr.getText().toString())) {
            params.put("aqzrr", edtAqzrr.getText().toString());
        } else {
            params.put("aqzrr", "");
        }
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("yzname", edtName.getText().toString());
        } else {
            params.put("yzname", "");
        }
        if (!TextUtils.isEmpty(edtName_.getText().toString())) {
            params.put("name", edtName_.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtAqzrrDh.getText().toString())) {
            params.put("aqzrrdh", edtAqzrrDh.getText().toString());
        } else {
            params.put("aqzrrdh", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picpath", sb.toString());
        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyDh.getText().toString())) {
            params.put("aqydh", edtAqyDh.getText().toString());
        } else {
            params.put("aqydh", "");
        }
        if (!TextUtils.isEmpty(edtSsdw.getText().toString())) {
            params.put("sddw", edtSsdw.getText().toString());
        } else {
            params.put("sddw", "");
        }
        if (!TextUtils.isEmpty(edtWgzrr.getText().toString())) {
            params.put("wgzrr", edtWgzrr.getText().toString());
        } else {
            params.put("wgzrr", "");
        }
        if (!TextUtils.isEmpty(edtWgzrrdh.getText().toString())) {
            params.put("wgzrrdh", edtWgzrrdh.getText().toString());
        } else {
            params.put("wgzrrdh", "");
        }
        if (!TextUtils.isEmpty(edtSgf.getText().toString())) {
            params.put("sgf", edtSgf.getText().toString());
        } else {
            params.put("sgf", "");
        }
        if (!TextUtils.isEmpty(edtZczb.getText().toString())) {
            params.put("zczb", edtZczb.getText().toString());
        } else {
            params.put("zczb", "");
        }
        if (!TextUtils.isEmpty(edtHsr.getText().toString())) {
            params.put("hszrr", edtHsr.getText().toString());
        } else {
            params.put("hszrr", "");
        }
        if (!TextUtils.isEmpty(edtHsrDh.getText().toString())) {
            params.put("hszrrdh", edtHsrDh.getText().toString());
        } else {
            params.put("hszrrdh", "");
        }
        SuccinctProgress.showSuccinctProgress(JzgdActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "updateManagement", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            if ("1".equals(resultCode)) {
                                ToastUtil.show(JzgdActivity.this, R.string.modify_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(JzgdActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(JzgdActivity.this, R.string.modify_fail);
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
        BaseApplication.getRequestQueue().cancelAll("management_commit");
        BaseApplication.getRequestQueue().cancelAll("updateManagement");
    }


    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        super.onDestroy();
    }

}
