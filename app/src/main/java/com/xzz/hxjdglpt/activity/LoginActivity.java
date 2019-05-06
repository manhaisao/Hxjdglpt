package com.xzz.hxjdglpt.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.maning.updatelibrary.InstallUtils;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Menu;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.model.Userandgrid;
import com.xzz.hxjdglpt.model.Version;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DeviceUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.utils.VersionInfoUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by dbz on 2017/5/11.
 */
@ContentView(R.layout.aty_login)
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    @ViewInject(R.id.account)
    private AutoCompleteTextView mEmailView;
    @ViewInject(R.id.pwd_login)
    private EditText mPasswordView;
    @ViewInject(R.id.login)
    private Button mEmailSignInButton;
    @ViewInject(R.id.to_register)
    private TextView tvRegister;

    private Version version;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        //检测是否需要版本更新
        checkVersion();
        //创建下载弹出框
        createDialog();
    }

    private void autoLogin() {
        String account = application.getUser().getUserName();
        String pwd = application.getUser().getPassword();
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", pwd);
        params.put("token", DeviceUtil.getDeviceID(this));
        requestLogin(params);
    }

    @Event(value = {R.id.login, R.id.to_register}, type = View.OnClickListener.class)
    private void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.login:
                attemptLogin();
                break;
            case R.id.to_register:
                LogUtil.i("go to register!");
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Event(value = {R.id.pwd_login}, type = TextView.OnEditorActionListener.class)
    private boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public void onClick(View v) {
                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                }
            });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * 登录验证
     */
    private void attemptLogin() {
        //跳转设置IP页面
        if ("config".equalsIgnoreCase(mEmailView.getText().toString().trim())) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, IpAddressActivity.class);
            startActivity(intent);
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_email_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String account = mEmailView.getText().toString();
            String pwd = mPasswordView.getText().toString();
            HashMap<String, String> params = new HashMap<>();
            params.put("account", account);
            params.put("password", pwd);
            params.put("token", DeviceUtil.getDeviceID(this));
            requestLogin(params);
        }
    }


    /**
     * 登录请求
     */
    private void requestLogin(HashMap<String, String> params) {
        SuccinctProgress.showSuccinctProgress(LoginActivity.this, "登录中···", SuccinctProgress
                .THEME_ULTIMATE, false, true);
        String url = ConstantUtil.BASE_URL + "/user/app_login";
        VolleyRequest.RequestPost(this, url, "login_request", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");

                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    LogUtil.i(resultCode);
                    Log.e("insert",result.toString()+"===="+resultCode);
                    // resultCode 1:存在用户 ；2：不存在；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
//                                if (result.getJSONObject("role") != null && !TextUtils.isEmpty
// (result.getJSONObject("role").toString())) {
//                                    Role role = (Role) gson.fromJson(result.getJSONObject
// ("role").toString(), Role.class);
//                                    application.setRole(role);
//                                }
                        LogUtil.i(result.getJSONArray("menus").toString());
                        if (result.getJSONArray("menus") != null && !TextUtils.isEmpty(result
                                .getJSONArray("menus").toString())) {
                            List<Menu> menus = gson.fromJson(result.getJSONArray("menus")
                                    .toString(), new TypeToken<List<Menu>>() {
                            }.getType());
                            application.setMenus(menus);
                        }
                        if (result.getJSONArray("roles") != null && !TextUtils.isEmpty(result
                                .getJSONArray("roles").toString())) {
                            List<Role> roles = gson.fromJson(result.getJSONArray("roles")
                                    .toString(), new TypeToken<List<Role>>() {
                            }.getType());
                            application.setRolesList(roles);
                        }
                        if (result.getJSONArray("grids") != null && !TextUtils.isEmpty(result
                                .getJSONArray("grids").toString())) {
                            List<Userandgrid> grids = gson.fromJson(result.getJSONArray("grids")
                                    .toString(), new TypeToken<List<Userandgrid>>() {
                            }.getType());
                            List<String> gridids = new ArrayList<String>();
                            for (Userandgrid ug : grids) {
                                gridids.add(ug.getGridid());
                            }
                            application.setGridIds(gridids);
                        }
                        User user = (User) gson.fromJson(result.getJSONObject("user").toString(),
                                User.class);
                        saveUser(user);
                        application.setUser(user);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                        ToastUtil.show(LoginActivity.this, R.string.account_pwd_inc);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LoginActivity.this, R.string.login_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
                autoLogin();
            }
        });
    }


    /**
     * 保存user
     *
     * @param user
     */
    public void saveUser(User user) {
        SharedPreferences mySharedP = getSharedPreferences("hxjd_base64_user", Activity
                .MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            SharedPreferences.Editor editor = mySharedP.edit();
            String userBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("userInfo", userBase64);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract
                        .Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract
                .CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout
                .simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract
                .CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private Dialog dialogUpdate;

    /**
     * 版本更新
     */
    private void checkVersion() {
        String url = ConstantUtil.BASE_URL + "/m_version/queryNewVersion";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", "");
        params.put("token", "");
        VolleyRequest.RequestPost(this, url, "queryNewVersion", params, new
                VolleyListenerInterface(this, VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess" + result.toString());
                SuccinctProgress.dismiss();
                try {
                    String resultCode = result.getString("resultCode");
                    Log.e("resultCode",resultCode);
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        Gson gson = new Gson();
                        version = (Version) gson.fromJson(result.getJSONObject("data").toString()
                                , Version.class);
                        if (version != null) {
                            int code = VersionInfoUtil.getVersionCode(LoginActivity.this);
                            if (code != version.getCode()) {
//                                String path = ConstantUtil.BASE_PATH + version.getName();
                                String url = ConstantUtil.FILE_DOWNLOAD_URL + version.getPath();
                                downloadVersion(url);
                            } else {
//                                ToastUtil.show(LoginActivity.this, R.string.version_new);
                                //实现登录功能
                                String isLogin = getIntent().getStringExtra("isLogin");
                                if ("false".equals(isLogin)) {
                                    return;
                                }
                                if (application.getUser() == null) {
                                    populateAutoComplete();
                                } else {
                                    autoLogin();
                                }
                            }
                        }
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(LoginActivity.this, R.string.load_fail);
                    } else if ("4".equals(resultCode)) {
                        //实现登录功能
                        String isLogin = getIntent().getStringExtra("isLogin");
                        if ("false".equals(isLogin)) {
                            return;
                        }
                        if (application.getUser() == null) {
                            populateAutoComplete();
                        } else {
                            autoLogin();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
//                Log.e("resultCode",error.getMessage());
                SuccinctProgress.dismiss();
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryNewVersion");
        BaseApplication.getRequestQueue().cancelAll("login_request");
    }

    /**
     * 下载apk谈出框
     */
    private void downloadVersion(final String url) {
        View view = getLayoutInflater().inflate(R.layout.version_update_dialog, null);
        dialogUpdate = new Dialog(this);
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(view);
        Button butOk = (Button) view.findViewById(R.id.setting_update_version);
        Button butCancle = (Button) view.findViewById(R.id.setting_not_update);
        butCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogUpdate.dismiss();
                //实现登录功能
                String isLogin = getIntent().getStringExtra("isLogin");
                if ("false".equals(isLogin)) {
                    return;
                }
                if (application.getUser() == null) {
                    populateAutoComplete();
                } else {
                    autoLogin();
                }
            }
        });
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdate.dismiss();
                downloadAPK(url);
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = dialogUpdate.getWindow().getAttributes();
        lp.width = dm.widthPixels - 50;
        dialogUpdate.getWindow().setAttributes(lp);
        dialogUpdate.show();
    }

    private ProgressDialog dialog = null;

    private void createDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("下载进度提示");
        dialog.setMax(100);
    }


    private void downloadAPK(String apkUrl) {
        new InstallUtils(mContext, apkUrl, ConstantUtil.APK_NAME, new InstallUtils
                .DownloadCallBack() {
            @Override
            public void onStart() {
                dialog.show();
                dialog.setProgress(0);
            }

            @Override
            public void onComplete(String path) {

                /**
                 * 安装APK工具类
                 * @param context       上下文
                 * @param filePath      文件路径
                 * @param authorities   ---------Manifest中配置provider的authorities字段---------
                 * @param callBack      安装界面成功调起的回调
                 */
                InstallUtils.installAPK(mContext, path, getPackageName() + ".FileProvider", new
                        InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.show(mContext, "正在安装程序");
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtil.show(mContext, "安装失败:" + e.toString());
                    }
                });
                dialog.dismiss();
                dialog.setProgress(100);
            }

            @Override
            public void onLoading(long total, long current) {
                LogUtil.i("InstallUtils----onLoading:-----total:" + total + ",current:" + current);
                dialog.setProgress((int) (current * 100 / total));
            }

            @Override
            public void onFail(Exception e) {
                LogUtil.i("InstallUtils---onFail:" + e.getMessage());
                dialog.dismiss();
            }

        }).downloadAPK();
    }

}

