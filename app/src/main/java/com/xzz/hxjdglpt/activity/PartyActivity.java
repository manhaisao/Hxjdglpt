package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.PartyMember;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Village;
import com.xzz.hxjdglpt.utils.CameraUtil;
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
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 党员信息
 * Created by dbz on 2017/5/10.
 */
@ContentView(R.layout.aty_party)
public class PartyActivity extends BaseActivity implements View.OnFocusChangeListener, View
        .OnTouchListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.party_ldtype)
    private AppCompatSpinner spLdlx;
    @ViewInject(R.id.party_xl)
    private AppCompatSpinner spXl;
    @ViewInject(R.id.party_type)
    private AppCompatSpinner spType;
    @ViewInject(R.id.party_commit)
    private Button btnCommit;
    @ViewInject(R.id.party_name)
    private EditText edtName;//党员姓名
    @ViewInject(R.id.party_reason)
    private EditText edtReason;//困难原因
    @ViewInject(R.id.party_sex)
    private AppCompatSpinner sex;
    @ViewInject(R.id.party_mz)
    private EditText edtMz;//民族
    @ViewInject(R.id.party_time)
    private EditText edtTime;//入党时间
    @ViewInject(R.id.party_birth)
    private EditText edtBirth;//出生年月
    @ViewInject(R.id.party_sfzh)
    private EditText edtSfzh;//身份证
    @ViewInject(R.id.party_dw)
    private EditText edtDw;//工作单位及职务
    @ViewInject(R.id.party_ssdzb)
    private EditText edtSsdzb;//所属党支部
    @ViewInject(R.id.party_phone)
    private EditText edtPhone;//联系电话
    @ViewInject(R.id.party_bz)
    private EditText edtBz;//备注
    @ViewInject(R.id.party_reason_lay)
    private RelativeLayout reasonLay;//


    private User user;
    private BaseApplication application;

    //定义显示时间控件
    private Calendar calendar; //通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private LayoutInflater mInflater;

    private boolean isAdd;//判断是否新增党员

    private String gridId;//网格ID

    private String[] dytypes;//党员类型

    private String[] dyldtypes;//流动类型

    private String[] types;//学历

    private PartyMember partyMember;

    private String[] sexs;//性别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.party_info_));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        calendar = Calendar.getInstance();
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        gridId = getIntent().getStringExtra("gridId");
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        edtTime.setOnFocusChangeListener(this);
        edtBirth.setOnFocusChangeListener(this);
        edtBz.setOnTouchListener(this);
        initView();
        initData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.party_bz && canVerticalScroll(edtBz))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else if ((view.getId() == R.id.party_reason && canVerticalScroll(edtReason))) {
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

    public void initData() {
        if (!isAdd) {
            btnCommit.setText(getString(R.string.update_space_1));
            partyMember = getIntent().getParcelableExtra("PartyMember");
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(partyMember.getXl())) {
                    spXl.setSelection(i);
                    break;
                }
            }

            if ("1".equals(partyMember.getIsliuru())) {
                spLdlx.setSelection(0);
            } else if ("2".equals(partyMember.getIsliuru())) {
                spLdlx.setSelection(1);
            } else if ("3".equals(partyMember.getIsliuru())) {
                spLdlx.setSelection(2);
            }

            if ("01".equals(partyMember.getType())) {
                spType.setSelection(0);
            } else if ("02".equals(partyMember.getType())) {
                spType.setSelection(1);
            }
            if (sexs[0].equals(partyMember.getXb())) {
                sex.setSelection(0);
            } else if (sexs[1].equals(partyMember.getXb())) {
                sex.setSelection(1);
            }
            edtName.setText(partyMember.getName());//党员姓名
            edtTime.setText(partyMember.getStime());//入党时间
            edtSsdzb.setText(partyMember.getDepart());//所属党支部
            edtPhone.setText(partyMember.getPhone());//联系电话
            edtBz.setText(partyMember.getBz());//备注
            edtBirth.setText(partyMember.getBirth());
            edtDw.setText(partyMember.getGzdwjzw());
            edtSfzh.setText(partyMember.getNo());
            edtMz.setText(partyMember.getMz());
            if ("02".equals(partyMember.getType())) {
                reasonLay.setVisibility(View.GONE);
                edtReason.setText(partyMember.getReason());
            }
        } else {
            btnCommit.setText(getString(R.string.comfirm_space_1));
        }
    }

    public void initView() {
        types = getResources().getStringArray(R.array.xl_list);
        sexs = getResources().getStringArray(R.array.sex_list);
        //适配器
        ArrayAdapter xlAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                types);
        //设置样式
        xlAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spXl.setAdapter(xlAd);

        //适配器
        ArrayAdapter sexAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                sexs);
        //设置样式
        sexAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sex.setAdapter(sexAd);

        //党员类型
        dytypes = getResources().getStringArray(R.array.dy_type_list);
        //适配器
        ArrayAdapter dyAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                dytypes);
        //设置样式
        dyAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spType.setAdapter(dyAd);


        //流动类型
        dyldtypes = getResources().getStringArray(R.array.dy_ldtype_list);
        //适配器
        ArrayAdapter dyldAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                dyldtypes);
        //设置样式
        dyldAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spLdlx.setAdapter(dyldAd);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    reasonLay.setVisibility(View.VISIBLE);
                } else {
                    reasonLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                    ToastUtil.show(PartyActivity.this, "图片数已至上限");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PartyActivity.this);
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
        SuccinctProgress.showSuccinctProgress(PartyActivity.this, "图片上传中···", SuccinctProgress
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
                        DialogUtil.showTipsDialog(PartyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PartyActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(PartyActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(value = {R.id.iv_back, R.id.party_commit}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.party_commit:
                if (isAdd) {
                    commit();
                } else {
                    update();
                }
                break;

        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/party/commitParty";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", gridId);
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        if (!TextUtils.isEmpty(edtSsdzb.getText().toString())) {
            params.put("ssdzb", edtSsdzb.getText().toString());
        } else {
            params.put("ssdzb", "");
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
        if (!TextUtils.isEmpty(spXl.getSelectedItem().toString())) {
            params.put("xl", spXl.getSelectedItem().toString());
        } else {
            params.put("xl", "");
        }
        if (dyldtypes[0].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "1");
        } else if (dyldtypes[1].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "2");
        } else if (dyldtypes[2].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "3");
        }
        if (dytypes[0].equals(spType.getSelectedItem().toString())) {
            params.put("dyType", "01");
        } else if (dytypes[1].equals(spType.getSelectedItem().toString())) {
            params.put("dyType", "02");
        }
        if (!TextUtils.isEmpty(sex.getSelectedItem().toString())) {
            params.put("xb", sex.getSelectedItem().toString());
        } else {
            params.put("xb", "");
        }
        if (!TextUtils.isEmpty(edtMz.getText().toString())) {
            params.put("mz", edtMz.getText().toString());
        } else {
            params.put("mz", "");
        }
        if (!TextUtils.isEmpty(edtDw.getText().toString())) {
            params.put("gzdwjzw", edtDw.getText().toString());
        } else {
            params.put("gzdwjzw", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("reason", edtReason.getText().toString());
        } else {
            params.put("reason", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("sfzh", edtSfzh.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtBirth.getText().toString())) {
            params.put("birth", edtBirth.getText().toString());
        } else {
            params.put("birth", "");
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
        SuccinctProgress.showSuccinctProgress(PartyActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "party_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(PartyActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PartyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PartyActivity.this, R.string.commit_fail);
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
        String url = ConstantUtil.BASE_URL + "/party/updateParty";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("gridId", gridId);
        params.put("id", partyMember.getId());
        if (!TextUtils.isEmpty(edtName.getText().toString())) {
            params.put("name", edtName.getText().toString());
        } else {
            params.put("name", "");
        }
        if (!TextUtils.isEmpty(edtTime.getText().toString())) {
            params.put("time", edtTime.getText().toString());
        } else {
            params.put("time", "");
        }
        if (!TextUtils.isEmpty(edtReason.getText().toString())) {
            params.put("reason", edtReason.getText().toString());
        } else {
            params.put("reason", "");
        }
        if (dyldtypes[0].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "1");
        } else if (dyldtypes[1].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "2");
        } else if (dyldtypes[2].equals(spLdlx.getSelectedItem().toString())) {
            params.put("isliuru", "3");
        }
        if (!TextUtils.isEmpty(edtSsdzb.getText().toString())) {
            params.put("ssdzb", edtSsdzb.getText().toString());
        } else {
            params.put("ssdzb", "");
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
        if (!TextUtils.isEmpty(spXl.getSelectedItem().toString())) {
            params.put("xl", spXl.getSelectedItem().toString());
        } else {
            params.put("xl", "");
        }
        if (dytypes[0].equals(spType.getSelectedItem().toString())) {
            params.put("dyType", "01");
        } else if (dytypes[1].equals(spType.getSelectedItem().toString())) {
            params.put("dyType", "02");
        }
        if (!TextUtils.isEmpty(sex.getSelectedItem().toString())) {
            params.put("xb", sex.getSelectedItem().toString());
        } else {
            params.put("xb", "");
        }
        if (!TextUtils.isEmpty(edtMz.getText().toString())) {
            params.put("mz", edtMz.getText().toString());
        } else {
            params.put("mz", "");
        }
        if (!TextUtils.isEmpty(edtDw.getText().toString())) {
            params.put("gzdwjzw", edtDw.getText().toString());
        } else {
            params.put("gzdwjzw", "");
        }
        if (!TextUtils.isEmpty(edtSfzh.getText().toString())) {
            params.put("sfzh", edtSfzh.getText().toString());
        } else {
            params.put("sfzh", "");
        }
        if (!TextUtils.isEmpty(edtBirth.getText().toString())) {
            params.put("birth", edtBirth.getText().toString());
        } else {
            params.put("birth", "");
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
        SuccinctProgress.showSuccinctProgress(PartyActivity.this, "数据修改中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "updateParty", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(PartyActivity.this, R.string.modify_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(PartyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(PartyActivity.this, R.string.modify_fail);
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
        BaseApplication.getRequestQueue().cancelAll("party_commit");
        BaseApplication.getRequestQueue().cancelAll("updateParty");
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.party_time:
                if (hasFocus) new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        //更新EditText控件日期 小于10加0
                        edtTime.setText(new StringBuilder().append(mYear).append("-").append(
                                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append
                                ("-").append((mDay < 10) ? "0" + mDay : mDay));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.party_birth:
                if (hasFocus) new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        //更新EditText控件日期 小于10加0
                        edtBirth.setText(new StringBuilder().append(mYear).append("-").append(
                                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append
                                ("-").append((mDay < 10) ? "0" + mDay : mDay));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH)).show();
                break;
        }

    }

    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        super.onDestroy();
    }

}
