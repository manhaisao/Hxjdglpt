package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.hitomi.universalloader.UniversalImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xzz.hxjdglpt.customview.SuccinctProgress;
import com.xzz.hxjdglpt.model.Qfhx;
import com.xzz.hxjdglpt.model.Role;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * 清风河下详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_qfhx_detail)
public class QfhxDtDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.qfhx_detail_label)
    private TextView tvLabel;
    @ViewInject(R.id.qfhx_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.qfhx_detail_content)
    private WebView tvContent;
    @ViewInject(R.id.hx_btn_right)
    private TextView tvRight;
    @ViewInject(R.id.qfhx_ad)
    protected GridView gvImages;
    @ViewInject(R.id.qfhx_video)
    private JCVideoPlayer qfhx_video;

    private Qfhx qfhx;

    private List<Role> roles;
    private DisplayImageOptions options;
    protected Transferee transferee;

    protected List<String> sourceImageList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        qfhx = getIntent().getParcelableExtra("qfhx");
        sourceImageList = new ArrayList<>();
        transferee = Transferee.getDefault(this);
        if (qfhx.getType().equals("2")) {
            qfhx_video.setUp(ConstantUtil.FILE_DOWNLOAD_URL + qfhx.getVideoPath(), null, qfhx.getVideoName());
        } else {
            qfhx_video.setVisibility(View.GONE);
        }
        if (user == null) {
            user = application.getUser();
        }
        roles = application.getRolesList();
        setImage();
        initView();
        initData();
        testTransferee();
    }

    public void initView() {
        tvTitle.setText("清风河下动态");
        if (isContain()) {
            tvRight.setText("删除");
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
    }

    private void setImage() {
        if (!TextUtils.isEmpty(qfhx.getFilePath())) {
            String[] path = qfhx.getFilePath().split(",");
            String[] fileName = qfhx.getFileName().split(",");
            if (path.length == fileName.length) {
                for (int i = 0; i < path.length; i++) {
                    if (!TextUtils.isEmpty(fileName[i]) || !TextUtils.isEmpty(path[i])) {
                        final String url = ConstantUtil.FILE_DOWNLOAD_URL + path[i];
                        if (fileName[i].endsWith(".jpg") || fileName[i].endsWith(".png") ||
                                fileName[i].endsWith(".JPG") || fileName[i].endsWith(".PNG") ||
                                fileName[i].endsWith(".jpeg") || fileName[i].endsWith(".JPEG") ||
                                fileName[i].endsWith(".BMP") || fileName[i].endsWith(".bmp") ||
                                fileName[i].endsWith(".gif") || fileName[i].endsWith(".GIF")) {
                            sourceImageList.add(url);
                        }
                    }
                }
            }
        }
    }

    /**
     * 包装缩略图 ImageView 集合
     * <p>
     * 注意：此方法只是为了收集 Activity 列表中所有可见 ImageView 好传递给 transferee。
     * 如果你添加了一些图片路径，扩展了列表图片个数，让列表超出屏幕，导致一些 ImageViwe 不
     * 可见，那么有可能这个方法会报错。这种情况，可以自己根据实际情况，来设置 transferee 的
     * originImageList 属性值
     *
     * @return
     */
    @NonNull
    protected List<ImageView> wrapOriginImageViewList(int size) {
        List<ImageView> originImgList = new ArrayList<>();
        if (size > 3) {
            for (int i = 0; i < 3; i++) {
                ImageView thumImg = (ImageView) ((LinearLayout) gvImages.getChildAt(i))
                        .getChildAt(0);
                originImgList.add(thumImg);
            }
        } else {
            for (int i = 0; i < size; i++) {
                ImageView thumImg = (ImageView) ((LinearLayout) gvImages.getChildAt(i))
                        .getChildAt(0);

                originImgList.add(thumImg);
            }
        }
        return originImgList;
    }

    public void initData() {
        if (qfhx.getTitle() != null && qfhx.getContent() != null) {
            String label = "发布人：" + qfhx.getRealName() + "     创建时间：" + qfhx.getCreateTime();
            tvLabel.setText(label);
            tvNoTitle.setText(qfhx.getTitle());
            tvContent.loadDataWithBaseURL(null, qfhx.getContent(), "text/html", "utf-8", null);
        }
    }

    private boolean isContain() {
        for (Role r : roles) {
            if ("4261".equals(r.getRoleId()) || qfhx.getRealName().equals(user.getRealName())) {
                return true;
            }
        }
        return false;
    }

    @Event(value = {R.id.iv_back, R.id.hx_btn_right}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.hx_btn_right:
                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final Dialog dialog = new Dialog(QfhxDtDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
                Button butOk = (Button) view.findViewById(R.id.dialog_ok);
                Button butCancle = (Button) view.findViewById(R.id.dialog_cancel);
                butOk.setText("确认");
                butCancle.setText("取消");
                butCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }
                });
                tvContent.setText("确认删除?");
                butOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        delData();
                    }
                });
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = dm.widthPixels - 50;
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                break;
        }
    }

    private void delData() {
        SuccinctProgress.showSuccinctProgress(QfhxDtDetailActivity.this, "数据提交中···",
                SuccinctProgress.THEME_ULTIMATE, false, true);
        String url = "";
        HashMap<String, String> params = new HashMap<>();
        url = ConstantUtil.BASE_URL + "/hxjdDynamic/deleteHxjdDynamic";
        params.put("id", String.valueOf(qfhx.getId()));
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        VolleyRequest.RequestPost(this, url, "deleteGzdt", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功添加 ；2：token不一致；3：添加失败
                    if ("1".equals(resultCode)) {
                        ToastUtil.show(QfhxDtDetailActivity.this, R.string.del_success);
                        finish();
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(QfhxDtDetailActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(QfhxDtDetailActivity.this, R.string.del_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                SuccinctProgress.dismiss();
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }

    private void testTransferee() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init
                (ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_empty_photo)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true).build();
        gvImages.setAdapter(new NineGridAdapter());
    }

    private class NineGridAdapter extends CommonAdapter<String> {

        public NineGridAdapter() {
            super(QfhxDtDetailActivity.this, R.layout.item_grid_image, sourceImageList);
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, final int position) {
            ImageView imageView = viewHolder.getView(R.id.image_view);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item,
                    imageView, options);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferConfig config = TransferConfig.build().setNowThumbnailIndex(position)
                            .setSourceImageList(sourceImageList).setMissPlaceHolder(R.mipmap
                                    .ic_empty_photo).setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                            .setOriginImageList(wrapOriginImageViewList(sourceImageList.size()))
                            .setProgressIndicator(new ProgressBarIndicator()).setIndexIndicator
                                    (new NumberIndexIndicator()).setImageLoader
                                    (UniversalImageLoader.with(getApplicationContext()))
                            .setJustLoadHitImage(true).create();
                    transferee.apply(config).show();
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        transferee.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
