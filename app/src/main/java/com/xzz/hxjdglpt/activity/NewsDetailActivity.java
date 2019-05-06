package com.xzz.hxjdglpt.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xzz.hxjdglpt.model.News;
import com.xzz.hxjdglpt.utils.ConstantUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import com.hitomi.universalloader.UniversalImageLoader;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

/**
 * 最新通知详情
 * Created by dbz on 2017/5/16.
 */
@ContentView(R.layout.aty_news_detail)
public class NewsDetailActivity extends BaseActivity {
    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.news_detail_label)
    private TextView tvLabel;
    @ViewInject(R.id.news_detail_title)
    private TextView tvNoTitle;
    @ViewInject(R.id.news_detail_content)
    private WebView tvContent;
    @ViewInject(R.id.news_ad)
    protected GridView gvImages;
    private News news;


    private DisplayImageOptions options;
    protected Transferee transferee;

    protected List<String> sourceImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        news = getIntent().getParcelableExtra("news");
        sourceImageList = new ArrayList<>();
        transferee = Transferee.getDefault(this);
        setImage();
        initView();
        initData();
        testTransferee();
    }

    public void initView() {
        tvTitle.setText(getText(R.string.xwdt_detail));
    }

    private void setImage() {
        if (!TextUtils.isEmpty(news.getFilePath())) {
            String[] path = news.getFilePath().split(",");
            String[] fileName = news.getFileName().split(",");
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
        if (news.getTitle() != null && news.getContent() != null) {
            String label = "发布人：" + news.getRealName() + "     创建时间：" + news.getCreateTime();
            tvLabel.setText(label);
            tvNoTitle.setText(news.getTitle());
            tvContent.loadDataWithBaseURL(null, news.getContent(), "text/html", "utf-8", null);
            tvContent.getSettings().setAllowFileAccess(true);
//            tvContent.getSettings().setUseWideViewPort(true); //自适应屏幕
            tvContent.getSettings().setLoadWithOverviewMode(true);
        }
    }

    @Event(value = {R.id.iv_back}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
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
            super(NewsDetailActivity.this, R.layout.item_grid_image, sourceImageList);
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
}
