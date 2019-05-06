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
import com.xzz.hxjdglpt.model.FamilyPlanning;
import com.xzz.hxjdglpt.model.Grid;
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
 * 卫生健康信息录入
 * Created by dbz on 2017/6/2.
 */
@ContentView(R.layout.aty_jhsy)
public class JhsyActivity extends BaseActivity implements View.OnTouchListener {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.jhsy_commit)
    private Button btnCommit;

    @ViewInject(R.id.jhsy_zrr)
    private TextView edtZrr;
    @ViewInject(R.id.jhsy_zrrphone)
    private EditText edtZrrPhone;

    @ViewInject(R.id.jhsy_ylfns)
    private TextView tvYlfn;//育龄妇女
    @ViewInject(R.id.jhsy_dszn)
    private TextView tvDszn;//独生子女
    @ViewInject(R.id.jhsy_jlfz)
    private TextView tvJlfz;//省扶
    @ViewInject(R.id.jhsy_jlfz_sf)
    private TextView tvJlfz_sf;//市扶
    @ViewInject(R.id.jhsy_jlfz_sd)
    private TextView tvJlfz_sd;//失独
    @ViewInject(R.id.jhsy_jlfz_sc)
    private TextView tvJlfz_sc;//伤残
    @ViewInject(R.id.jhsy_ldrk)
    private TextView tvLdrk;//流动人口
    @ViewInject(R.id.jhsy_jskn)
    private TextView tvJskn;//计生困难
    @ViewInject(R.id.jhsy_hospital)
    private TextView tvHospital;//计生困难

    @ViewInject(R.id.jhsy_bz)
    private EditText edtBz;


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
        tvTitle.setText(getText(R.string.jhsy));
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
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
        if ((view.getId() == R.id.jhsy_bz && canVerticalScroll(edtBz))) {
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
                    ToastUtil.show(JhsyActivity.this, "图片数已至上限");
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
//                        intent.putExtra("isLocal", false);
                    intent.putExtra("index", position - 1);
                    intent.setClass(JhsyActivity.this, ShowImageActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(JhsyActivity.this);
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
        SuccinctProgress.showSuccinctProgress(JhsyActivity.this, "图片上传中···", SuccinctProgress
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
                        String pPath = result.getString("attachFilePath");
                        mPath.add(pPath);
                        picPath = getPathStr();
                        new Thread(runnable1).start();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JhsyActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SuccinctProgress.dismiss();
                ToastUtil.show(JhsyActivity.this, R.string.upload_image_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(value = {R.id.iv_back, R.id.jhsy_commit, R.id.jhsy_add_ylfns, R.id.jhsy_add_dszn, R.id
            .jhsy_add_jlfz, R.id.jhsy_add_jlfz_sf, R.id.jhsy_add_jlfz_sd, R.id.jhsy_add_jlfz_sc,
            R.id.jhsy_add_ldrk, R.id.jhsy_add_jskn, R.id.jhsy_add_hospital}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("gridId", grid.getId());
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.jhsy_commit:
                commit();
                break;
            case R.id.jhsy_add_ylfns:
                intent.putExtra("type", 3);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_dszn:
                intent.putExtra("type", 26);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_jlfz:
                intent.putExtra("type", 27);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_jlfz_sf:
                intent.putExtra("type", 28);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_jlfz_sd:
                intent.putExtra("type", 29);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_jlfz_sc:
                intent.putExtra("type", 30);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_ldrk:
                intent.putExtra("type", 31);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_jskn:
                intent.putExtra("type", 32);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.jhsy_add_hospital:
                intent.putExtra("type", 64);
                intent.setClass(JhsyActivity.this, ListActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 提交信息
     */
    private void commit() {
        String url = ConstantUtil.BASE_URL + "/jhsy/commitJhsy";
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
        if (!TextUtils.isEmpty(edtBz.getText().toString())) {
            params.put("bz", edtBz.getText().toString());
        } else {
            params.put("bz", "");
        }
        params.put("picpath", getPathStr());
        SuccinctProgress.showSuccinctProgress(JhsyActivity.this, "数据提交中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        VolleyRequest.RequestPost(this, url, "jhsy_commit", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(JhsyActivity.this, R.string.commit_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JhsyActivity.this, R.string.commit_fail);
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
        BaseApplication.getRequestQueue().cancelAll("jhsy_commit");
        BaseApplication.getRequestQueue().cancelAll("queryJhsyById");
    }


    private String picPath = "";

    private void loadDataByGid() {
        SuccinctProgress.showSuccinctProgress(JhsyActivity.this, "请求数据中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/jhsy/queryJhsyById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("id", grid.getId());
        VolleyRequest.RequestPost(this, url, "queryJhsyById", params, new VolleyListenerInterface
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
                        FamilyPlanning sp = (FamilyPlanning) gson.fromJson(result.getJSONObject
                                ("data").toString(), FamilyPlanning.class);
                        edtZrr.setText(sp.getHead() == null ? "" : sp.getHead());
                        edtZrrPhone.setText(sp.getHeadphone() == null ? "" : sp.getHeadphone());
                        tvYlfn.setText(String.valueOf(sp.getYlfnnum()));//育龄妇女
                        tvDszn.setText(String.valueOf(sp.getOnechildfamilynum()));//独生子女
                        tvJlfz.setText(String.valueOf(sp.getShengfNum()));//省扶
                        tvJlfz_sf.setText(String.valueOf(sp.getShifNum()));//市扶
                        tvJlfz_sd.setText(String.valueOf(sp.getSdNum()));//失独
                        tvJlfz_sc.setText(String.valueOf(sp.getScNum()));//伤残
                        tvLdrk.setText(String.valueOf(sp.getLdrknum()));//流动人口
                        tvJskn.setText(String.valueOf(sp.getJsknnum()));//计生困难
                        tvHospital.setText(String.valueOf(sp.getYyNum()));//医院
                        edtBz.setText(sp.getBz() == null ? "" : sp.getBz());
                        if (sp.getPicpath() != null) {
                            picPath = sp.getPicpath();
                        }
//                        SuccinctProgress.showSuccinctProgress(JhsyActivity.this, "图片努力加载中···",
//                                SuccinctProgress.THEME_ULTIMATE, false, true);
//                        new Thread(runnable).start();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(JhsyActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(JhsyActivity.this, R.string.load_fail);
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
                    map.put("itemImage", BitmapUtil.getNetBitmap(JhsyActivity.this, ConstantUtil
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
                .hideSoftInputFromWindow(JhsyActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
