package com.xzz.hxjdglpt.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.customview.dateview.JudgeDate;
import com.xzz.hxjdglpt.customview.dateview.ScreenInfo;
import com.xzz.hxjdglpt.customview.dateview.WheelMain;
import com.xzz.hxjdglpt.model.Company;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.CameraUtil;
import com.xzz.hxjdglpt.utils.CompressImageUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DateUtil;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 企业信息
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_company)
public class CompanyActivity extends BaseActivity implements View.OnTouchListener {


    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.c_lb)
    private AppCompatSpinner spQylb;//企业类别
    @ViewInject(R.id.c_lx)
    private AppCompatSpinner spQylx;//企业类别
    @ViewInject(R.id.c_isns)
    private AppCompatSpinner spIsns;//是否纳税
    @ViewInject(R.id.c_jy_area)
    private AppCompatSpinner spJyArea;//经营范围

    @ViewInject(R.id.c_commit)
    private Button btnCommit;

    @ViewInject(R.id.c_name)
    private EditText edtName;//企业名称
    @ViewInject(R.id.c_lxr)
    private EditText edtLxr;//法人
    @ViewInject(R.id.c_lxr_phone)
    private EditText edtLxrPhone;//联系人号码
    @ViewInject(R.id.c_aqzrr)
    private EditText edtAqzrr;//安全责任人
    @ViewInject(R.id.c_aqzrrphone)
    private EditText edtAqzrrPhone;//安全责任人号码
    @ViewInject(R.id.c_aqy)
    private EditText edtAqy;//安全员
    @ViewInject(R.id.c_aqyphone)
    private EditText edtAqyPhone;//安全员号码
    @ViewInject(R.id.c_zczb)
    private EditText edtZczb;//注册资本
    @ViewInject(R.id.c_zcsj)
    private TextView tvZcsj;//注册时间
    @ViewInject(R.id.c_address)
    private EditText edtAddress;//经营地址
    @ViewInject(R.id.c_zg_num)
    private EditText edtZgnum;//员工数
    @ViewInject(R.id.c_kpqk)
    private EditText edtKpqk;//开票情况

    @ViewInject(R.id.c_xydm)
    private EditText edtDm;//统一社会信用代码
    @ViewInject(R.id.c_ssdw)
    private EditText edtSsdw;//属地单位
    @ViewInject(R.id.c_wgzrr)
    private EditText edtWgzrr;//网格责任人
    @ViewInject(R.id.c_wgzrr_phone)
    private EditText edtWgzrrdh;//网格责任人电话

    @ViewInject(R.id.c_rjgz)
    private EditText edtRjgz;//人均工资
    @ViewInject(R.id.c_jy_nsqk)
    private EditText edtNsqk;//纳税情况
    @ViewInject(R.id.c_bz)
    private EditText edtBz;//备注


    @ViewInject(R.id.c_jb)
    private AppCompatSpinner spQyjb;//应急器具配备情况
    @ViewInject(R.id.c_xfs_num)
    private EditText edtXfsNum;//消防栓数量
    @ViewInject(R.id.c_mhq_num)
    private EditText edtMhqNum;//灭火器数量

    private User user;
    private BaseApplication application;

    @ViewInject(R.id.c_yyzz)
    private GridView gridView1;                   //营业执照
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private LayoutInflater mInflater;

    private boolean isAdd;//判断是否新增

    private String gridId;//网格ID

    private Company company;

    private String[] types;//类别

    private String[] jbqk;//类别

    private String[] lx;//类型

    private String[] isNs;//是否纳税

    private String[] jyArea;//经营范围

    private String picPath = "";

    @ViewInject(R.id.com_natj)
    private RelativeLayout mLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.company_info_));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        initView();
        initData();
    }

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            company = getIntent().getParcelableExtra("company");
            if (types[0].equals(company.getType())) {
                spQylb.setSelection(0);
            } else if (types[1].equals(company.getType())) {
                spQylb.setSelection(1);
            } else if (types[2].equals(company.getType())) {
                spQylb.setSelection(2);
            } else if (types[3].equals(company.getType())) {
                spQylb.setSelection(3);
            }
            if (jbqk[0].equals(company.getQingk())) {
                spQyjb.setSelection(0);
            } else if (jbqk[1].equals(company.getQingk())) {
                spQyjb.setSelection(1);
            }
            if (isNs[0].equals(company.getIsnas())) {
                spIsns.setSelection(0);
            } else if (isNs[1].equals(company.getIsnas())) {
                spIsns.setSelection(1);
            }
            mLay.setVisibility(View.VISIBLE);
            if ("01".equals(company.getComtype())) {
                spQylx.setSelection(0);
            } else if ("02".equals(company.getComtype())) {
                spQylx.setSelection(1);
            } else if ("03".equals(company.getComtype())) {
                spQylx.setSelection(2);
            } else if ("04".equals(company.getComtype())) {
                spQylx.setSelection(3);
            }
//            else if ("05".equals(company.getComtype())) {
//                spQylx.setSelection(3);
//            }

            if (jyArea[0].equals(company.getArea())) {
                spJyArea.setSelection(0);
            } else if (jyArea[1].equals(company.getArea())) {
                spJyArea.setSelection(1);
            } else if (jyArea[2].equals(company.getArea())) {
                spJyArea.setSelection(2);
            } else if (jyArea[3].equals(company.getArea())) {
                spJyArea.setSelection(3);
            } else if (jyArea[4].equals(company.getArea())) {
                spJyArea.setSelection(4);
            } else if (jyArea[5].equals(company.getArea())) {
                spJyArea.setSelection(5);
            }
//            else if (jyArea[6].equals(company.getArea())) {
//                spJyArea.setSelection(6);
//            } else if (jyArea[7].equals(company.getArea())) {
//                spJyArea.setSelection(7);
//            }

            edtName.setText(company.getName());//姓名
            edtZczb.setText(company.getZczb());//
            edtNsqk.setText(company.getNsqk());
            edtKpqk.setText(company.getKpqk());
            edtRjgz.setText(company.getGongzi());//
            edtAddress.setText(company.getAddress());
            edtLxr.setText(company.getLinkman());
            edtLxrPhone.setText(company.getLinkmanPhone());
            edtZgnum.setText(String.valueOf(company.getpNum()));
            tvZcsj.setText(company.getZcsj());
            edtBz.setText(company.getBz());//备注
            edtAqzrr.setText(company.getAqzrr());
            edtAqzrrPhone.setText(company.getAqzrrPhone());
            edtAqy.setText(company.getAqy());
            edtAqyPhone.setText(company.getAqyPhone());
            edtDm.setText(company.getXinyong());
            edtSsdw.setText(company.getDanwie());
            edtWgzrr.setText(company.getGridzrr());
            edtWgzrrdh.setText(company.getGridzrrdh());
            edtMhqNum.setText(company.getMhqnum());
            edtXfsNum.setText(company.getXfqnum());
            if (company.getPicpath() != null) {
                picPath = company.getPicpath();
                SuccinctProgress.showSuccinctProgress(CompanyActivity.this, "图片努力加载中···",
                        SuccinctProgress.THEME_ULTIMATE, false, true);
                new Thread(runnable1).start();
            }
        } else {
            mLay.setVisibility(View.GONE);
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }

    public void initView() {
        types = getResources().getStringArray(R.array.qytype_list);
        isNs = getResources().getStringArray(R.array.is);
        lx = getResources().getStringArray(R.array.qylx_list);
        jyArea = getResources().getStringArray(R.array.qyarea_list);
        jbqk = getResources().getStringArray(R.array.pbqk);

        //适配器
        ArrayAdapter jbAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, jbqk);
        //设置样式
        jbAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spQyjb.setAdapter(jbAd);

        edtAddress.setOnTouchListener(this);
        edtKpqk.setOnTouchListener(this);
        edtSsdw.setOnTouchListener(this);
        edtBz.setOnTouchListener(this);
        //适配器
        ArrayAdapter typeAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, types);
        //设置样式
        typeAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spQylb.setAdapter(typeAd);

        //适配器
        ArrayAdapter isNsAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, isNs);
        //设置样式
        isNsAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spIsns.setAdapter(isNsAd);

        //适配器
        ArrayAdapter lxAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                lx);
        //设置样式
        lxAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spQylx.setAdapter(lxAd);

        //适配器
        ArrayAdapter areaAd = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, jyArea);
        //设置样式
        areaAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spJyArea.setAdapter(areaAd);


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
                if (position == 0 && imageItem.size() == 3) { //第一张为默认图片
                    ToastUtil.show(CompanyActivity.this, "图片数已至上限");
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //选择图片
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
//                            .Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, IMAGE_OPEN);

                    requestPermission(new String[]{android.Manifest.permission.CAMERA, android
                            .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
                    //通过onResume()刷新数据
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("imagesName", picPath.split(","));
                    intent.putExtra("index", position - 1);
                    intent.setClass(CompanyActivity.this, ShowImageActivity.class);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.c_address && canVerticalScroll(edtAddress))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.c_ssdw && canVerticalScroll(edtSsdw))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.c_bz && canVerticalScroll(edtBz))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.c_jy_nsqk && canVerticalScroll(edtNsqk))) {
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

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String[] ps = picPath.split(",");
            for (int i = 0; i < ps.length; i++) {
                if (!TextUtils.isEmpty(ps[i])) {
                    mPath.add(ps[i]);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", BitmapUtil.getNetBitmap(CompanyActivity.this,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
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
        SuccinctProgress.showSuccinctProgress(CompanyActivity.this, "图片上传中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/upload/uploadFile";
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("file", CompressImageUtil.putImage(CompressImageUtil
                .getSmallBitmap(path), this));
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
                        DialogUtil.showTipsDialog(CompanyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CompanyActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(CompanyActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(value = {R.id.iv_back, R.id.c_commit, R.id.c_zcsj, R.id.jd1, R.id.jd2, R.id.jd3, R.id.jd4}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.c_commit:
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    ToastUtil.show(CompanyActivity.this, "请输入企业名称！");
                    return;
                }
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;
            case R.id.c_zcsj:
                showBottoPopupWindow();
                break;
            case R.id.jd1:
                if (company != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, NsqkActivity.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "1");
                    startActivity(intent);
                } else {
                    ToastUtil.show(CompanyActivity.this, "暂无纳税情况");
                }
                break;
            case R.id.jd2:
                if (company != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, NsqkActivity.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "2");
                    startActivity(intent);
                } else {
                    ToastUtil.show(CompanyActivity.this, "暂无纳税情况");
                }
                break;
            case R.id.jd3:
                if (company != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, NsqkActivity.class);
                    intent.putExtra("comType", company.getArea());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "3");
                    startActivity(intent);
                } else {
                    ToastUtil.show(CompanyActivity.this, "暂无纳税情况");
                }
                break;
            case R.id.jd4:
                if (company != null) {
                    Intent intent = new Intent();
                    intent.setClass(this, NsqkActivity.class);
                    intent.putExtra("comType", company.getComtype());
                    intent.putExtra("comId", String.valueOf(company.getId()));
                    intent.putExtra("type", "4");
                    startActivity(intent);
                } else {
                    ToastUtil.show(CompanyActivity.this, "暂无纳税情况");
                }
                break;
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/company/commitCompany";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        if (!TextUtils.isEmpty(edtRjgz.getText().toString())) {
            params.put("gongzi", edtRjgz.getText().toString());
        } else {
            params.put("gongzi", "");
        }
        if (!TextUtils.isEmpty(edtZgnum.getText().toString())) {
            params.put("zgnum", edtZgnum.getText().toString());
        } else {
            params.put("zgnum", "0");
        }
        if (!TextUtils.isEmpty(edtLxr.getText().toString())) {
            params.put("lxr", edtLxr.getText().toString());
        } else {
            params.put("lxr", "");
        }
        if (getIntent().getStringExtra("isYjfw") != null) {
            params.put("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
        if (!TextUtils.isEmpty(edtLxrPhone.getText().toString())) {
            if (edtLxrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("lxrphone", edtLxrPhone.getText().toString());
        } else {
            params.put("lxrphone", "");
        }
        params.put("jyarea", spJyArea.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("isnas", spIsns.getSelectedItem().toString());
        params.put("type", spQylb.getSelectedItem().toString());
        if (lx[0].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "01");
        } else if (lx[1].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "02");
        } else if (lx[2].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "03");
        } else if (lx[3].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "04");
        }
//        else if (lx[3].equals(spQylx.getSelectedItem().toString())) {
//            params.put("comtype", "05");
//        }
        if (!TextUtils.isEmpty(edtKpqk.getText().toString())) {
            params.put("kpqk", edtKpqk.getText().toString());
        } else {
            params.put("kpqk", "");
        }
        if (!TextUtils.isEmpty(edtNsqk.getText().toString())) {
            params.put("nsqk", edtNsqk.getText().toString());
        } else {
            params.put("nsqk", "");
        }
        if (!TextUtils.isEmpty(edtZczb.getText().toString())) {
            params.put("zczb", edtZczb.getText().toString());
        } else {
            params.put("zczb", "");
        }
        if (!TextUtils.isEmpty(tvZcsj.getText().toString())) {
            params.put("zcsj", tvZcsj.getText().toString());
        } else {
            params.put("zcsj", "");
        }
        if (!TextUtils.isEmpty(edtAqzrr.getText().toString())) {
            params.put("aqzrr", edtAqzrr.getText().toString());
        } else {
            params.put("aqzrr", "");
        }
        if (!TextUtils.isEmpty(edtAqzrrPhone.getText().toString())) {
            params.put("aqzrrPhone", edtAqzrrPhone.getText().toString());
        } else {
            params.put("aqzrrPhone", "");
        }
        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyPhone.getText().toString())) {
            params.put("aqyPhone", edtAqyPhone.getText().toString());
        } else {
            params.put("aqyPhone", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        if (!TextUtils.isEmpty(edtDm.getText().toString())) {
            params.put("xinyong", edtDm.getText().toString());
        } else {
            params.put("xinyong", "");
        }
        if (!TextUtils.isEmpty(edtSsdw.getText().toString())) {
            params.put("danwie", edtSsdw.getText().toString());
        } else {
            params.put("danwie", "");
        }
        if (!TextUtils.isEmpty(edtWgzrr.getText().toString())) {
            params.put("gridzrr", edtWgzrr.getText().toString());
        } else {
            params.put("gridzrr", "");
        }
        if (!TextUtils.isEmpty(edtWgzrrdh.getText().toString())) {
            params.put("gridzrrdh", edtWgzrrdh.getText().toString());
        } else {
            params.put("gridzrrdh", "");
        }
        params.put("picpath", sb.toString());

        if (!TextUtils.isEmpty(edtXfsNum.getText().toString())) {
            params.put("xfqnum", edtXfsNum.getText().toString());
        } else {
            params.put("xfqnum", "0");
        }

        if (!TextUtils.isEmpty(edtMhqNum.getText().toString())) {
            params.put("mhqnum", edtMhqNum.getText().toString());
        } else {
            params.put("mhqnum", "0");
        }
        params.put("qingk", spQyjb.getSelectedItem().toString());
        SuccinctProgress.showSuccinctProgress(CompanyActivity.this, "请求提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "company_commit", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        SuccinctProgress.dismiss();
                        LogUtil.i("onMySuccess");
                        try {
                            String resultCode = result.getString("resultCode");
                            if ("1".equals(resultCode)) {
                                ToastUtil.show(CompanyActivity.this, R.string.commit_success);
                                finish();
                            } else if ("2".equals(resultCode)) {
                                DialogUtil.showTipsDialog(CompanyActivity.this);
                            } else if ("3".equals(resultCode)) {
                                ToastUtil.show(CompanyActivity.this, R.string.commit_fail);
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
        String url = ConstantUtil.BASE_URL + "/company/updateCompany";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", String.valueOf(company.getId()));
        params.put("gId", gridId);
        if (!TextUtils.isEmpty(edtRjgz.getText().toString())) {
            params.put("gongzi", edtRjgz.getText().toString());
        } else {
            params.put("gongzi", "");
        }
        if (!TextUtils.isEmpty(edtZgnum.getText().toString())) {
            params.put("zgnum", edtZgnum.getText().toString());
        } else {
            params.put("zgnum", "0");
        }
        if (!TextUtils.isEmpty(edtLxr.getText().toString())) {
            params.put("lxr", edtLxr.getText().toString());
        } else {
            params.put("lxr", "");
        }
        if (!TextUtils.isEmpty(edtLxrPhone.getText().toString())) {
            if (edtLxrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("lxrphone", edtLxrPhone.getText().toString());
        } else {
            params.put("lxrphone", "");
        }
        params.put("jyarea", spJyArea.getSelectedItem().toString());
        if (!TextUtils.isEmpty(edtAddress.getText().toString())) {
            params.put("address", edtAddress.getText().toString());
        } else {
            params.put("address", "");
        }
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("isnas", spIsns.getSelectedItem().toString());
        params.put("type", spQylb.getSelectedItem().toString());
        if (lx[0].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "01");
        } else if (lx[1].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "02");
        } else if (lx[2].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "03");
        } else if (lx[3].equals(spQylx.getSelectedItem().toString())) {
            params.put("comtype", "04");
        }
//        else if (lx[3].equals(spQylx.getSelectedItem().toString())) {
//            params.put("comtype", "05");
//        }
        if (!TextUtils.isEmpty(edtKpqk.getText().toString())) {
            params.put("kpqk", edtKpqk.getText().toString());
        } else {
            params.put("kpqk", "");
        }
        if (!TextUtils.isEmpty(edtNsqk.getText().toString())) {
            params.put("nsqk", edtNsqk.getText().toString());
        } else {
            params.put("nsqk", "");
        }
        if (!TextUtils.isEmpty(edtZczb.getText().toString())) {
            params.put("zczb", edtZczb.getText().toString());
        } else {
            params.put("zczb", "");
        }
        if (!TextUtils.isEmpty(tvZcsj.getText().toString())) {
            params.put("zcsj", tvZcsj.getText().toString());
        } else {
            params.put("zcsj", "");
        }
        if (!TextUtils.isEmpty(edtAqzrr.getText().toString())) {
            params.put("aqzrr", edtAqzrr.getText().toString());
        } else {
            params.put("aqzrr", "");
        }
        if (!TextUtils.isEmpty(edtAqzrrPhone.getText().toString())) {
            params.put("aqzrrPhone", edtAqzrrPhone.getText().toString());
        } else {
            params.put("aqzrrPhone", "");
        }
        if (!TextUtils.isEmpty(edtAqy.getText().toString())) {
            params.put("aqy", edtAqy.getText().toString());
        } else {
            params.put("aqy", "");
        }
        if (!TextUtils.isEmpty(edtAqyPhone.getText().toString())) {
            params.put("aqyPhone", edtAqyPhone.getText().toString());
        } else {
            params.put("aqyPhone", "");
        }
        if (!TextUtils.isEmpty(edtDm.getText().toString())) {
            params.put("xinyong", edtDm.getText().toString());
        } else {
            params.put("xinyong", "");
        }
        if (!TextUtils.isEmpty(edtSsdw.getText().toString())) {
            params.put("danwie", edtSsdw.getText().toString());
        } else {
            params.put("danwie", "");
        }
        if (!TextUtils.isEmpty(edtWgzrr.getText().toString())) {
            params.put("gridzrr", edtWgzrr.getText().toString());
        } else {
            params.put("gridzrr", "");
        }
        if (!TextUtils.isEmpty(edtWgzrrdh.getText().toString())) {
            params.put("gridzrrdh", edtWgzrrdh.getText().toString());
        } else {
            params.put("gridzrrdh", "");
        }
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        params.put("picpath", sb.toString());
        if (!TextUtils.isEmpty(edtXfsNum.getText().toString())) {
            params.put("xfqnum", edtXfsNum.getText().toString());
        } else {
            params.put("xfqnum", "0");
        }

        if (!TextUtils.isEmpty(edtMhqNum.getText().toString())) {
            params.put("mhqnum", edtMhqNum.getText().toString());
        } else {
            params.put("mhqnum", "0");
        }
        params.put("qingk", spQyjb.getSelectedItem().toString());
        if (getIntent().getStringExtra("isYjfw") != null) {
            params.put("isYjfw", getIntent().getStringExtra("isYjfw"));
        }
        SuccinctProgress.showSuccinctProgress(CompanyActivity.this, "请求提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "updateCompany", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(CompanyActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(CompanyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(CompanyActivity.this, R.string.modify_fail);
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
        BaseApplication.getRequestQueue().cancelAll("updateCompany");
        BaseApplication.getRequestQueue().cancelAll("company_commit");
    }


    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        super.onDestroy();
    }

    private WheelMain wheelMainDate;
    private String beginTime;

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void showBottoPopupWindow() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_date_popup_window, null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int) (width * 0.8), ActionBar
                .LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelMainDate = new WheelMain(menuView, true);
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtil.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day);
        final String currentTime = wheelMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tvZcsj, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择成立日期");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelMainDate.getTime().toString();
                tvZcsj.setText(beginTime.substring(0, 10));
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
