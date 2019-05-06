package com.xzz.hxjdglpt.activity;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.Minzheng;
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
 * 民政信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_mz)
public class MzActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.mz_commit)
    private Button btnCommit;

    @ViewInject(R.id.mz_zrr)
    private EditText edtZrr;
    @ViewInject(R.id.mz_zrrphone)
    private EditText edtZrrPhone;
    @ViewInject(R.id.mz_ncdb)
    private TextView edtNcdb;//农村低保
    @ViewInject(R.id.mz_csdb)
    private TextView edtCsdb;//城市低保
    @ViewInject(R.id.mz_cjr)
    private TextView tvCjr;//残疾人
    @ViewInject(R.id.mz_lset)
    private TextView tvLsrt;//留守儿童
    @ViewInject(R.id.mz_yfdx)
    private TextView tvYfdx;//优抚对象
    @ViewInject(R.id.mz_cjr_1)
    private TextView tvCjr_1;//持证残疾人
    @ViewInject(R.id.mz_cjr_2)
    private TextView tvCjr_2;//重残生活补贴
    @ViewInject(R.id.mz_cjr_3)
    private TextView tvCjr_3;//其他类生活补贴
    @ViewInject(R.id.mz_cjr_4)
    private TextView tvCjr_4;//一级护理补贴
    @ViewInject(R.id.mz_cjr_5)
    private TextView tvCjr_5;//二级护理补贴
    @ViewInject(R.id.mz_tkgy)
    private TextView tvTkgy;//特困(五保)供养
    @ViewInject(R.id.mz_tkgy_1)
    private TextView tvTkgy_1;//集中供养人员
    @ViewInject(R.id.mz_tkgy_2)
    private TextView tvTkgy_2;//分散供养人员
    @ViewInject(R.id.mz_tkgy_3)
    private TextView tvTkgy_3;//城市三无人员
    @ViewInject(R.id.mz_tkgy_4)
    private TextView tvTkgy_4;//孤儿及困境儿童
    @ViewInject(R.id.mz_zlj)
    private TextView tvZlj;//80岁以上尊老金
    @ViewInject(R.id.mz_zlj_1)
    private TextView tvZlj_1;//80-89周岁人员
    @ViewInject(R.id.mz_zlj_2)
    private TextView tvZlj_2;//90-99周岁人员
    @ViewInject(R.id.mz_zlj_3)
    private TextView tvZlj_3;//100周岁以上

    @ViewInject(R.id.mz_bz)
    private EditText edtBz;
    @ViewInject(R.id.mz_add_ncdb)
    private ImageView ncNcdbAdd;
    @ViewInject(R.id.mz_add_csdb)
    private ImageView ncCsdbAdd;
    @ViewInject(R.id.mz_add_cj)
    private ImageView ncCjAdd;
    @ViewInject(R.id.mz_add_et)
    private ImageView ncEtAdd;
    @ViewInject(R.id.mz_add_yfdx)
    private ImageView ncYfdxAdd;

    private User user;
    private BaseApplication application;

    @ViewInject(R.id.gridView1)
    private GridView gridView1;                   //网格显示缩略图
    private String pathImage;                       //选择图片路径
    private Bitmap bmp;                               //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器

    private LayoutInflater mInflater;

    private Grid grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tvTitle.setText(getText(R.string.mz));
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        grid = getIntent().getParcelableExtra("grid");
        edtBz.setOnTouchListener(this);
        initView();
    }

    public void initData() {
        loadDataByGid();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText而且当前EditText能够滚动则将事件交给EditText处理。否则将事件交由其父类处理
        if ((view.getId() == R.id.mz_bz && canVerticalScroll(edtBz))) {
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
                    ToastUtil.show(MzActivity.this, "图片数已至上限");
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
                    intent.setClass(MzActivity.this, ShowImageActivity.class);
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

    private String getPathStr() {
        StringBuffer sb = new StringBuffer();
        for (String str : mPath) {
            if (!TextUtils.isEmpty(str)) {
                sb.append(str).append(",");
            }
        }
        return sb.toString();
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
        initData();
    }

    Runnable runnable1 = new Runnable() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MzActivity.this);
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
        SuccinctProgress.showSuccinctProgress(MzActivity.this, "图片上传中···", SuccinctProgress
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
                        String pPath = result.getString("attachFilePath");
                        mPath.add(pPath);
                        picPath = getPathStr();
                        new Thread(runnable1).start();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(MzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(MzActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(MzActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Event(value = {R.id.iv_back, R.id.mz_commit, R.id.mz_add_ncdb, R.id.mz_add_csdb, R.id
            .mz_add_cj, R.id.mz_add_et, R.id.mz_add_yfdx, R.id.mz_add_cj_1, R.id.mz_add_cj_2, R
            .id.mz_add_cj_3, R.id.mz_add_cj_4, R.id.mz_add_cj_5, R.id.mz_add_tkgy_1, R.id
            .mz_add_tkgy_2, R.id.mz_add_tkgy_3, R.id.mz_add_tkgy_4, R.id.mz_add_zlj_1, R.id
            .mz_add_zlj_2, R.id.mz_add_zlj_3}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("gridId", grid.getId());
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.mz_commit:
                commit();
                break;
            case R.id.mz_add_ncdb:
                intent.putExtra("type", 2);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_csdb:
                intent.putExtra("type", 1);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj:
                intent.putExtra("type", 6);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_et:
                intent.putExtra("type", 13);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_yfdx:
                intent.putExtra("type", 14);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj_1:
                intent.putExtra("type", 8);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj_2:
                intent.putExtra("type", 9);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj_3:
                intent.putExtra("type", 10);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj_4:
                intent.putExtra("type", 11);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_cj_5:
                intent.putExtra("type", 12);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_tkgy_1:
                intent.putExtra("type", 33);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_tkgy_2:
                intent.putExtra("type", 34);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_tkgy_3:
                intent.putExtra("type", 35);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_tkgy_4:
                intent.putExtra("type", 36);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_zlj_1:
                intent.putExtra("type", 37);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_zlj_2:
                intent.putExtra("type", 38);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.mz_add_zlj_3:
                intent.putExtra("type", 39);
                intent.setClass(MzActivity.this, ListActivity.class);
                startActivity(intent);
                break;
        }

    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/mz/commitMz";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
        if (!TextUtils.isEmpty(edtZrr.getText().toString())) {
            params.put("zrr", edtZrr.getText().toString());
        }
        if (!TextUtils.isEmpty(edtZrrPhone.getText().toString())) {
            if (edtZrrPhone.getText().toString().length() != 11) {
                ToastUtil.show(this, "请输入11位手机号");
                return;
            }
            params.put("zrrphone", edtZrrPhone.getText().toString());
        } else {
            params.put("zrrphone", "");
        }
//        if (!TextUtils.isEmpty(edtCsdb.getText().toString())) {
//            params.put("csdbhs", edtCsdb.getText().toString());
//        } else {
//            params.put("csdbhs", "0");
//        }
//        if (!TextUtils.isEmpty(edtNcdb.getText().toString())) {
//            params.put("ncdbhs", edtNcdb.getText().toString());
//        } else {
//            params.put("ncdbhs", "0");
//        }
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("picpath", getPathStr());
        SuccinctProgress.showSuccinctProgress(MzActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "mz_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(MzActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(MzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(MzActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("mz_commit");
        BaseApplication.getRequestQueue().cancelAll("queryMzByGId");
    }


    private String picPath = "";

    private void loadDataByGid() {
        SuccinctProgress.showSuccinctProgress(MzActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/mz/queryMzByGId";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
        VolleyRequest.RequestPost(this, url, "queryMzByGId", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        Minzheng sp = (Minzheng) gson.fromJson(result.getJSONObject("data")
                                .toString(), Minzheng.class);
                        edtZrr.setText(sp.getZrr() == null ? "" : sp.getZrr());
                        edtZrrPhone.setText(sp.getZrrphone() == null ? "" : sp.getZrrphone());
                        edtNcdb.setText(sp.getNcdbhs() == null ? "" : String.valueOf(sp.getNcdbhs
                                ()));
                        edtCsdb.setText(sp.getCsdbhs() == null ? "" : String.valueOf(sp.getCsdbhs
                                ()));
                        tvCjr.setText(sp.getCjrnum() == null ? "" : String.valueOf(sp.getCjrnum()));
                        tvLsrt.setText(sp.getLsrtnum() == null ? "" : String.valueOf(sp
                                .getLsrtnum()));
                        tvYfdx.setText(sp.getYfdx() == null ? "" : String.valueOf(sp.getYfdx()));
                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                        tvCjr_1.setText(String.valueOf(sp.getCzCjrNum()));
                        tvCjr_2.setText(String.valueOf(sp.getZcCjrNum()));
                        tvCjr_3.setText(String.valueOf(sp.getQtCjrNum()));
                        tvCjr_4.setText(String.valueOf(sp.getYjCjrNum()));
                        tvCjr_5.setText(String.valueOf(sp.getRjCjrNum()));
                        tvTkgy.setText(String.valueOf(sp.getTkgyNum()));
                        tvTkgy_1.setText(String.valueOf(sp.getTkgyNum_1()));
                        tvTkgy_2.setText(String.valueOf(sp.getTkgyNum_2()));
                        tvTkgy_3.setText(String.valueOf(sp.getTkgyNum_3()));
                        tvTkgy_4.setText(String.valueOf(sp.getTkgyNum_4()));
                        tvZlj.setText(String.valueOf(sp.getZljNum()));
                        tvZlj_1.setText(String.valueOf(sp.getZljNum_1()));
                        tvZlj_2.setText(String.valueOf(sp.getZljNum_2()));
                        tvZlj_3.setText(String.valueOf(sp.getZljNum_3()));
                        if (sp.getPicpath() != null) {
                            picPath = sp.getPicpath();
                        }
//                        SuccinctProgress.showSuccinctProgress(MzActivity.this, "图片努力加载中···",
//                                SuccinctProgress.THEME_ULTIMATE, false, true);
//                        new Thread(runnable).start();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(MzActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(MzActivity.this, R.string.load_fail);
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
                    map.put("itemImage", BitmapUtil.getNetBitmap(MzActivity.this, ConstantUtil
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
            SuccinctProgress.dismiss();
            simpleAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        if (post != null) {
            post.cancel();
        }
        super.onDestroy();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(MzActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
