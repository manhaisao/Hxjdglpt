package com.xzz.hxjdglpt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.hitomi.universalloader.UniversalImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xzz.hxjdglpt.model.Gonggao;
import com.xzz.hxjdglpt.okhttp.OkHttpManager;
import com.xzz.hxjdglpt.superfileview.FileDisplayActivity;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_gg_detail)
public class GgDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.party_detail_label)
    private TextView tvLabel;
    @ViewInject(R.id.party_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.party_detail_content)
    private WebView tvContent;
    private Gonggao gonggao;
    private ArrayList<String> imglist;
    private ArrayList<String> otherlist;//路径
    private ArrayList<String> fileNlist;//文件名称
    @ViewInject(R.id.party_detail_fj)
    private LinearLayout lay_fj;
    @ViewInject(R.id.party_detail_fc)
    private TextView tvfj;

    @ViewInject(R.id.news_ad)
    protected GridView gvImages;
    private DisplayImageOptions options;
    protected Transferee transferee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
        initFj();
        testTransferee();
    }

    public void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        imglist = new ArrayList<>();
        otherlist = new ArrayList<>();
        fileNlist = new ArrayList<>();
        transferee = Transferee.getDefault(this);
        createDialog();
    }

    /**
     * 动态添加附件
     */
    private void initFj() {
        if (!TextUtils.isEmpty(gonggao.getFilepath())) {
            String[] path = gonggao.getFilepath().split(",");
            String[] fileName = gonggao.getFilename().split(",");
            if (path.length == fileName.length) {
                //去除非图片文件路径
//                List<String> list = new ArrayList<>();
                for (int i = 0; i < path.length; i++) {
                    if (path[i] != null && !TextUtils.isEmpty(path[i])) {
                        if (path[i].endsWith(".jpg") || path[i].endsWith(".png") || path[i]
                                .endsWith(".JPG") || path[i].endsWith("" + "" + ".PNG") ||
                                path[i].endsWith(".jpeg") || path[i].endsWith(".JPEG") || path[i]
                                .endsWith("" + ".BMP") || path[i].endsWith(".bmp") || path[i]
                                .endsWith("" + "" + ".gif") || path[i].endsWith("" + ".GIF")) {
//                            list.add(path[i]);
                            imglist.add(ConstantUtil.FILE_DOWNLOAD_URL + path[i]);
                        } else {
                            otherlist.add(path[i]);
                            fileNlist.add(fileName[i]);
                        }
                    }
                }
                if (otherlist.size() > 0) {
                    for (int i = 0; i < otherlist.size(); i++) {
                        final String n = fileNlist.get(i);
                        final String p = otherlist.get(i);
                        if (!TextUtils.isEmpty(n) && !TextUtils.isEmpty(p)) {
                            TextView tv = new TextView(this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tv.setLayoutParams(lp);
                            tv.setText(n);
                            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                            tv.setTextColor(getResources().getColor(R.color.title_bg));
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    File file = new File(ConstantUtil.BASE_PATH + n);
                                    if (file.exists()) {
                                        Intent intent = new Intent(GgDetailActivity.this,
                                                FileDisplayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("path", ConstantUtil.BASE_PATH +
                                                n);
                                        bundle.putSerializable("fileName", n);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final Dialog dialog = new Dialog(GgDetailActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.tip_dialog,
                                                null);

                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(view);
                                        //TextView tvTip = (TextView) view.findViewById(R.id.tip_title);
                                        TextView tvCon = (TextView) view.findViewById(R.id.tip_content);
                                        //tvTip.setText(NoticeDetailActivity.this.getText(R.string
                                        // .zxtz_detail_tip));
                                        tvCon.setText(GgDetailActivity.this.getText(R.string
                                                .zxtz_detail_content));
                                        Button butOk = (Button) view.findViewById(R.id.tip_ok);
                                        Button butCancle = (Button) view.findViewById(R.id.tip_cancel);
                                        butCancle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                dialog.dismiss();
                                            }
                                        });
                                        butOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                String url = ConstantUtil.FILE_DOWNLOAD_URL + p;
                                                downloadFile(url, n);
                                            }
                                        });
                                        DisplayMetrics dm = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        WindowManager.LayoutParams lp = dialog.getWindow()
                                                .getAttributes();
                                        lp.width = dm.widthPixels - 50;
                                        dialog.getWindow().setAttributes(lp);
                                        dialog.show();
                                    }
                                }
                            });
                            lay_fj.addView(tv);
                        }
                    }
                } else {
                    tvfj.setVisibility(View.GONE);
                }
            }
        }
    }

    public void initData() {
        gonggao = getIntent().getParcelableExtra("gg");
        if ("人事任免".equals(gonggao.getRenshi()))
            tvTitle.setText("人事任免");
        else {
            String type = getIntent().getStringExtra("type");
            if (type != null && type.equals("公示栏")) {
                tvTitle.setText("公示栏");
            } else {
                tvTitle.setText("督查通报");
            }
        }


        String label = "发布时间：" + gonggao.getCreatetime();
        tvLabel.setText(label);
        tvNoTitle.setText(gonggao.getTitle());
        tvContent.loadDataWithBaseURL(null, gonggao.getTongbao(), "text/html", "utf-8", null);
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
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

    private ProgressDialog dialog = null;

    private void createDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("下载进度提示");
        dialog.setMax(100);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OkHttpManager.DOWNLOAD_SUCCESS:
                    ToastUtil.show(GgDetailActivity.this, R.string.download_success);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.DOWNLOAD_FAIL:
                    ToastUtil.show(GgDetailActivity.this, R.string.download_failed);
                    times = 0;
                    dialog.dismiss();
                    break;
                case OkHttpManager.NETWORK_FAILURE:
                    ToastUtil.show(GgDetailActivity.this, R.string.net_unuser);
                    times = 0;
                    dialog.dismiss();
                    break;
                default:
                    dialog.setProgress(msg.what);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        transferee.destroy();
    }

    private int times = 0;

    private void downloadFile(final String url, String fileName) {
        dialog.show();
        OkHttpManager.download(url, ConstantUtil.BASE_PATH, fileName, new OkHttpManager
                .ProgressListener() {
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
        }, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state == OkHttpManager.State.SUCCESS) {
                    handler.sendEmptyMessage(OkHttpManager.DOWNLOAD_SUCCESS);
                } else if (state == OkHttpManager.State.FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.DOWNLOAD_FAIL);
                } else if (state == OkHttpManager.State.NETWORK_FAILURE) {
                    handler.sendEmptyMessage(OkHttpManager.NETWORK_FAILURE);
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
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
            super(GgDetailActivity.this, R.layout.item_grid_image, imglist);
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
                            .setSourceImageList(imglist).setMissPlaceHolder(R.mipmap
                                    .ic_empty_photo).setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                            .setOriginImageList(wrapOriginImageViewList(imglist.size()))
                            .setProgressIndicator(new ProgressBarIndicator()).setIndexIndicator
                                    (new NumberIndexIndicator()).setImageLoader
                                    (UniversalImageLoader.with(getApplicationContext()))
                            .setJustLoadHitImage(true).create();
                    transferee.apply(config).show();
                }
            });
        }
    }
}
