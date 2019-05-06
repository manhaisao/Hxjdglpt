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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.CustomSearchDialog;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.message.PlotEvent;
import com.xzz.hxjdglpt.model.Plot;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
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
import java.util.List;
import java.util.Map;


/**
 * 小区信息
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_plot)
public class PlotActivity extends BaseActivity implements View.OnTouchListener {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.plot_commit)
    private Button btnCommit;
    @ViewInject(R.id.plot_name)
    private EditText edtName;//小区名称
    @ViewInject(R.id.plot_area)
    private EditText edtArea;//面积
    @ViewInject(R.id.plot_num)
    private EditText edtNum;//楼房幢数
    @ViewInject(R.id.plot_hnum)
    private EditText edtHNum;//户数
    @ViewInject(R.id.plot_wgnum)
    private EditText edtWgNum;//物管人员人数
    @ViewInject(R.id.plot_bjnum)
    private EditText edtBjNum;//保洁人员人数
    @ViewInject(R.id.plot_gs)
    private EditText edtGs;//物业公司
    @ViewInject(R.id.plot_wyphone)
    private EditText edtWyPhone;//物业公司联系方式
    @ViewInject(R.id.plot_zjl)
    private EditText edtZjl;//物业总经理
    @ViewInject(R.id.plot_zjlphone)
    private EditText edtZjlPhone;//物业总经理电话
    @ViewInject(R.id.plot_wyfzr)
    private EditText edtWyfzr;//物业责任人
    @ViewInject(R.id.plot_wyfzrphone)
    private EditText edtWyfzrPhone;//物业责任人电话
    @ViewInject(R.id.plot_zrr)
    private EditText edtZrr;//责任人
    @ViewInject(R.id.plot_zrrphone)
    private EditText edtZrrPhone;//责任人电话
    @ViewInject(R.id.plot_bz)
    private EditText edtBz;//备注
    @ViewInject(R.id.plot_ba)
    private TextView tvBaNum;//保安数量
    @ViewInject(R.id.plot_zrr1)
    private TextView tvZrr;//

    //新增
    @ViewInject(R.id.plot_jzarea)
    private EditText plot_jzarea;//建筑面积
    @ViewInject(R.id.plot_dynum)
    private EditText plot_dynum;//单元数
    @ViewInject(R.id.plot_wgz)
    private EditText plot_wgz;//网格长
    @ViewInject(R.id.plot_wgzphone)
    private EditText plot_wgzphone;//网格长联系电话
    @ViewInject(R.id.plot_jdwgy)
    private EditText plot_jdwgy;//街道网格员
    @ViewInject(R.id.plot_cjwgy)
    private EditText plot_cjwgy;//村居网格员
    @ViewInject(R.id.plot_jdwgyphone)
    private EditText plot_jdwgyphone;
    @ViewInject(R.id.plot_cjwgyphone)
    private EditText plot_cjwgyphone;

    @ViewInject(R.id.plot_add_jyh)
    private ImageView plot_add_jyh;
    @ViewInject(R.id.plot_jyh)
    private TextView plot_jyh;//经营户

    @ViewInject(R.id.rl_ba)
    private RelativeLayout rl_ba;
    @ViewInject(R.id.rl_jyh)
    private RelativeLayout rl_jyh;
    private User user;
    private BaseApplication application;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private LayoutInflater mInflater;

    private String gridId;

    private boolean isAdd;

    private Plot plot;

    private String isFrom;


    private int jyNum = 0;

    private List<User> userList = new ArrayList<>();
    private ArrayList<User> receivors = new ArrayList<>();

    private CustomSearchDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.xqgsq));
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        application = (BaseApplication) getApplication();
        gridId = getIntent().getStringExtra("gridId");
        isFrom = getIntent().getStringExtra("isFrom");
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        if (user == null) {
            user = application.getUser();
        }
        edtBz.setOnTouchListener(this);
        userDialog = new CustomSearchDialog(this, userList);
        EventBus.getDefault().register(this);
        initData();
        initView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.plot_bz && canVerticalScroll(edtBz))) {
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
                    ToastUtil.show(PlotActivity.this, "图片数已至上限");
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //选择图片
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
//                            .Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, IMAGE_OPEN);

                    requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                            .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                    //通过onResume()刷新数据
                } else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第" + (position + 1) + " 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

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
            simpleAdapter.notifyDataSetChanged();
        }
    };

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlotActivity.this);
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
        SuccinctProgress.showSuccinctProgress(PlotActivity.this, "图片上传中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
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
                        DialogUtil.showTipsDialog(PlotActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(PlotActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            plot = getIntent().getParcelableExtra("plot");
            edtName.setText(plot.getName());//小区名称
            edtZjl.setText(plot.getWyzjl());//物业公司总经理
            edtWyPhone.setText(plot.getWyphone());//物业公司电话
            edtGs.setText(plot.getWygs());//物业公司
            edtArea.setText(plot.getArea());//面积
            edtBjNum.setText(String.valueOf(plot.getbNum()));//保洁员人数
            edtHNum.setText(String.valueOf(plot.gethNum()));//户数
            edtNum.setText(String.valueOf(plot.getpNum()));//房子栋数
            edtWgNum.setText(String.valueOf(plot.getwNum()));//物管人员数量
            edtZrr.setText(plot.getvMan());//小区责任人
            edtZrrPhone.setText(plot.getvManPhone());//小区责任人电话
            edtBz.setText(plot.getBz());//备注
            edtZjlPhone.setText(plot.getWyzjlphone());
            edtWyfzr.setText(plot.getWyfzr());
            edtWyfzrPhone.setText(plot.getWyfzrphone());
            tvBaNum.setText(String.valueOf(plot.getBaNum()));
            plot_jzarea.setText(plot.getJzmj());
            plot_dynum.setText(String.valueOf(plot.getDyNum()));
            plot_wgz.setText(plot.getWgz());
            plot_wgzphone.setText(plot.getWgzphone());
            plot_jdwgy.setText(plot.getJdwgy() != null ? plot.getJdwgy() : "");
            plot_jdwgyphone.setText(plot.getJdwagphone());
            plot_cjwgy.setText(plot.getCjwgy());
            plot_cjwgyphone.setText(plot.getCjwgyphone());
            plot_jyh.setText(String.valueOf(plot.getJyhNum()));
            jyNum = plot.getJyhNum();
            if (!TextUtils.isEmpty(plot.getZrrid())) {
                getNamesByIds();
            }
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
            rl_ba.setVisibility(View.GONE);
            rl_jyh.setVisibility(View.GONE);
        }
        loadGridUser();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateJYH(PlotEvent plotEvent) {
        if (plotEvent.isAdd()) {
            jyNum = jyNum + 1;
        } else {
            jyNum = jyNum - 1;
        }
        plot_jyh.setText(String.valueOf(jyNum));
    }


    @Event(value = {R.id.iv_back, R.id.plot_commit, R.id.plot_add_ba, R.id.plot_zrr1, R.id.plot_add_jyh}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.plot_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.plot_add_ba:
                if (!isAdd) {
                    Intent intent = new Intent();
                    intent.putExtra("pid", plot.getId());
                    intent.putExtra("plotName", plot.getName());
                    intent.putExtra("isFrom", isFrom);
                    intent.setClass(PlotActivity.this, BaryListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.plot_zrr1:
                receivors.clear();
                showMutilAlertDialog("请选择责任人");
                break;
            case R.id.plot_add_jyh:
                Intent intent = new Intent();
                intent.putExtra("pid", plot.getId());
                intent.setClass(PlotActivity.this, JyhListActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/plot/commitPlot";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtArea.getText().toString())) {
            params.put("area", edtArea.getText().toString());
        } else {
            params.put("area", "0");
        }
        if (!TextUtils.isEmpty(edtNum.getText().toString())) {
            params.put("pNum", edtNum.getText().toString());
        } else {
            params.put("pNum", "0");
        }
        if (!TextUtils.isEmpty(edtHNum.getText().toString())) {
            params.put("hNum", edtHNum.getText().toString());
        } else {
            params.put("hNum", "0");
        }
        if (!TextUtils.isEmpty(edtWgNum.getText().toString())) {
            params.put("wNum", edtWgNum.getText().toString());
        } else {
            params.put("wNum", "0");
        }
        if (!TextUtils.isEmpty(edtBjNum.getText().toString())) {
            params.put("bNum", edtBjNum.getText().toString());
        } else {
            params.put("bNum", "0");
        }
        if (!TextUtils.isEmpty(edtGs.getText().toString())) {
            params.put("wygs", edtGs.getText().toString());
        } else {
            params.put("wygs", "");
        }
        if (!TextUtils.isEmpty(edtWyPhone.getText().toString())) {
            params.put("wyphone", edtWyPhone.getText().toString());
        } else {
            params.put("wyphone", "");
        }
        if (!TextUtils.isEmpty(edtZjl.getText().toString())) {
            params.put("wyzjl", edtZjl.getText().toString());
        } else {
            params.put("wyzjl", "");
        }
        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("vMan", edtZrr.getText().toString());
        } else {
            params.put("vMan", "");
        }

        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtZrrPhone.getText().toString())) {
            if (edtZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("vManPhone", edtZrrPhone.getText().toString());
        } else {
            params.put("vManPhone", "");
        }
        if (!TextUtils.isEmpty(edtZjlPhone.getText().toString())) {
            if (edtZjlPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wyzjlphone", edtZjlPhone.getText().toString());
        } else {
            params.put("wyzjlphone", "");
        }
        if (!TextUtils.isEmpty(edtWyfzr.getText().toString())) {
            params.put("wyfzr", edtWyfzr.getText().toString());
        } else {
            params.put("wyfzr", "");
        }

        if (!TextUtils.isEmpty(plot_jzarea.getText().toString())) {
            params.put("jzmj", plot_jzarea.getText().toString());
        } else {
            params.put("jzmj", "");
        }

        if (!TextUtils.isEmpty(plot_wgz.getText().toString())) {
            params.put("wgz", plot_wgz.getText().toString());
        } else {
            params.put("wgz", "");
        }
        if (!TextUtils.isEmpty(plot_wgzphone.getText().toString())) {
            if (plot_wgzphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wgzphone", plot_wgzphone.getText().toString());
        } else {
            params.put("wgzphone", "");
        }


        if (!TextUtils.isEmpty(plot_jdwgy.getText().toString())) {
            params.put("jdwgy", plot_jdwgy.getText().toString());
        } else {
            params.put("jdwgy", "");
        }
        if (!TextUtils.isEmpty(plot_jdwgyphone.getText().toString())) {
            if (plot_jdwgyphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("jdwagphone", plot_jdwgyphone.getText().toString());
        } else {
            params.put("jdwagphone", "");
        }

        if (!TextUtils.isEmpty(plot_cjwgy.getText().toString())) {
            params.put("cjwgy", plot_cjwgy.getText().toString());
        } else {
            params.put("cjwgy", "");
        }
        if (!TextUtils.isEmpty(plot_cjwgyphone.getText().toString())) {
            if (plot_cjwgyphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("cjwgyphone", plot_cjwgyphone.getText().toString());
        } else {
            params.put("cjwgyphone", "");
        }

        if (!TextUtils.isEmpty(plot_dynum.getText().toString())) {
            params.put("dyNum", plot_dynum.getText().toString());
        } else {
            params.put("dyNum", "");
        }

        if (!TextUtils.isEmpty(edtWyfzrPhone.getText().toString())) {
            if (edtWyfzrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wyfzrphone", edtWyfzrPhone.getText().toString());
        } else {
            params.put("wyfzrphone", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picpath", sb.toString());
        params.put("zrrid", userId.toString());
        SuccinctProgress.showSuccinctProgress(PlotActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "plot_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(PlotActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PlotActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotActivity.this, R.string.commit_fail);
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

    private void update() {
        String url = ConstantUtil.BASE_URL + "/plot/updatePlot";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(plot.getId()));
        params.put("gridId", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtArea.getText().toString())) {
            params.put("area", edtArea.getText().toString());
        } else {
            params.put("area", "0");
        }
        if (!TextUtils.isEmpty(edtNum.getText().toString())) {
            params.put("pNum", edtNum.getText().toString());
        } else {
            params.put("pNum", "0");
        }
        if (!TextUtils.isEmpty(edtHNum.getText().toString())) {
            params.put("hNum", edtHNum.getText().toString());
        } else {
            params.put("hNum", "0");
        }
        if (!TextUtils.isEmpty(edtWgNum.getText().toString())) {
            params.put("wNum", edtWgNum.getText().toString());
        } else {
            params.put("wNum", "0");
        }
        if (!TextUtils.isEmpty(edtBjNum.getText().toString())) {
            params.put("bNum", edtBjNum.getText().toString());
        } else {
            params.put("bNum", "0");
        }
        if (!TextUtils.isEmpty(edtGs.getText().toString())) {
            params.put("wygs", edtGs.getText().toString());
        } else {
            params.put("wygs", "");
        }
        if (!TextUtils.isEmpty(edtWyPhone.getText().toString())) {
            params.put("wyphone", edtWyPhone.getText().toString());
        } else {
            params.put("wyphone", "");
        }
        if (!TextUtils.isEmpty(edtZjl.getText().toString())) {
            params.put("wyzjl", edtZjl.getText().toString());
        } else {
            params.put("wyzjl", "");
        }
        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("vMan", edtZrr.getText().toString());
        } else {
            params.put("vMan", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        if (!TextUtils.isEmpty(edtZrrPhone.getText().toString())) {
            if (edtZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("vManPhone", edtZrrPhone.getText().toString());
        } else {
            params.put("vManPhone", "");
        }
        if (!TextUtils.isEmpty(edtZjlPhone.getText().toString())) {
            if (edtZjlPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wyzjlphone", edtZjlPhone.getText().toString());
        } else {
            params.put("wyzjlphone", "");
        }
        if (!TextUtils.isEmpty(edtWyfzr.getText().toString())) {
            params.put("wyfzr", edtWyfzr.getText().toString());
        } else {
            params.put("wyfzr", "");
        }

        if (!TextUtils.isEmpty(plot_wgz.getText().toString())) {
            params.put("wgz", plot_wgz.getText().toString());
        } else {
            params.put("wgz", "");
        }
        if (!TextUtils.isEmpty(plot_wgzphone.getText().toString())) {
            if (plot_wgzphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wgzphone", plot_wgzphone.getText().toString());
        } else {
            params.put("wgzphone", "");
        }


        if (!TextUtils.isEmpty(plot_jdwgy.getText().toString())) {
            params.put("jdwgy", plot_jdwgy.getText().toString());
        } else {
            params.put("jdwgy", "");
        }
        if (!TextUtils.isEmpty(plot_jdwgyphone.getText().toString())) {
            if (plot_jdwgyphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("jdwagphone", plot_jdwgyphone.getText().toString());
        } else {
            params.put("jdwagphone", "");
        }

        if (!TextUtils.isEmpty(plot_cjwgy.getText().toString())) {
            params.put("cjwgy", plot_cjwgy.getText().toString());
        } else {
            params.put("cjwgy", "");
        }
        if (!TextUtils.isEmpty(plot_cjwgyphone.getText().toString())) {
            if (plot_cjwgyphone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("cjwgyphone", plot_cjwgyphone.getText().toString());
        } else {
            params.put("cjwgyphone", "");
        }

        if (!TextUtils.isEmpty(plot_dynum.getText().toString())) {
            params.put("dyNum", plot_dynum.getText().toString());
        } else {
            params.put("dyNum", "");
        }
        if (!TextUtils.isEmpty(plot_jzarea.getText().toString())) {
            params.put("jzmj", plot_jzarea.getText().toString());
        } else {
            params.put("jzmj", "");
        }
        if (!TextUtils.isEmpty(edtWyfzrPhone.getText().toString())) {
            if (edtWyfzrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("wyfzrphone", edtWyfzrPhone.getText().toString());
        } else {
            params.put("wyfzrphone", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picpath", sb.toString());
        params.put("zrrid", userId.toString());
        SuccinctProgress.showSuccinctProgress(PlotActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "updatePlot", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(PlotActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PlotActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PlotActivity.this, R.string.modify_fail);
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
        BaseApplication.getRequestQueue().cancelAll("plot_commit");
        BaseApplication.getRequestQueue().cancelAll("queryUsers");
    }


    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showMutilAlertDialog(String title) {
        userDialog.showView();
        userDialog.setTitle(title);
        userDialog.setClicklistener(new CustomSearchDialog.ClickListenerInterface() {

            @Override
            public void doConfirm() {
                userDialog.dismiss();
                setName();
            }

            @Override
            public void doCancel() {
                userDialog.dismiss();
            }
        });
        userDialog.setItemClicklistener(new CustomSearchDialog.ItemClickListenerInterface() {
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
                User us = userDialog.mSearchList.get(position);
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
            tvZrr.setText(names.toString());
        } else {
            tvZrr.setText("请选择责任人");
        }
    }

    private void loadGridUser() {
        String url = ConstantUtil.BASE_URL + "/user/queryUsers";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "queryUsers", params, new
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
                                if (userDialog.isShowing()) {
                                    userDialog.notifyDataSetChanged();
                                }
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PlotActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PlotActivity.this, R.string.load_fail);
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

    private void getNamesByIds() {
        String url = ConstantUtil.BASE_URL + "/user/queryNameByIds";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("ids", plot.getZrrid());
        VolleyRequest.RequestPost(this, url, "queryNameByIds", params, new
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
                                LogUtil.i("1111111111111");
                                LogUtil.i(result.getString("data"));
                                tvZrr.setText(result.getString("data"));
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(PlotActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(PlotActivity.this, R.string.load_fail);
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
}
