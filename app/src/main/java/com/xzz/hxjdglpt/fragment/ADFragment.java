package com.xzz.hxjdglpt.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.NewsDetailActivity;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.News;
import com.xzz.hxjdglpt.model.User;
import com.xzz.hxjdglpt.utils.BitmapUtil;
import com.xzz.hxjdglpt.utils.ConstantUtil;
import com.xzz.hxjdglpt.utils.DialogUtil;
import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;
import com.xzz.hxjdglpt.volley.VolleyErrorHelper;
import com.xzz.hxjdglpt.volley.VolleyImageLoader;
import com.xzz.hxjdglpt.volley.VolleyListenerInterface;
import com.xzz.hxjdglpt.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbz on 2017/5/11.
 */
public class ADFragment extends Fragment {

    protected LayoutInflater mInflater;

    private View rootView;

    private RelativeLayout rl;


    private ViewPager mViewPager;

    private View[] items;

    private ImageView[] mImageViews = new ImageView[5];

    private Handler mHandler = new Handler();

    private int currentIndex;
    private User user;
    private BaseApplication application;
    private ImageLoader mImageLoader;

    private ImagePagerAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        application = (BaseApplication) getActivity().getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_ad, container, false);
            mInflater = inflater;
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    private void initView(View view) {
        rl = (RelativeLayout) view.findViewById(R.id.ad_parent);
        mImageViews[0] = (ImageView) view.findViewById(R.id.point1);
        mImageViews[1] = (ImageView) view.findViewById(R.id.point2);
        mImageViews[2] = (ImageView) view.findViewById(R.id.point3);
        mImageViews[3] = (ImageView) view.findViewById(R.id.point4);
        mImageViews[4] = (ImageView) view.findViewById(R.id.point5);

        mViewPager = (ViewPager) view.findViewById(R.id.vp);

        adapter = new ImagePagerAdapter(getImages(mImageViews.length));
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                for (int i = 0; i < items.length; i++) {
                    if (i == position) {
                        mImageViews[i].setBackgroundResource(R.mipmap.selected_point);
                    } else {
                        mImageViews[i].setBackgroundResource(R.mipmap.unselected_point);
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    private void setViewPagerParams() {
        int[] size = getResources().getIntArray(R.array.ad_default_size);
        BaseApplication application = (BaseApplication) getActivity().getApplicationContext();
        double scale = application.getScreenWidth() * 1.0 / size[0];
        int desireHeight = (int) (scale * size[1]);
        int desireWidth = application.getScreenWidth();
        LogUtil.i("rl.scale = " + scale);
        LogUtil.i("rl.getLayoutParams() = " + rl.getLayoutParams());
        LogUtil.i("rl.desireWidth = " + desireWidth + "desireHeight = " + desireHeight);
        rl.getLayoutParams().width = desireWidth;
        rl.getLayoutParams().height = desireHeight;
    }

    @Override
    public void onResume() {
        super.onResume();
        setViewPagerParams();
        mHandler.postDelayed(task, 5000);

        String url = ConstantUtil.BASE_URL + "/m_news/queryNewsList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        //查找前5个
        params.put("pageIndex", String.valueOf(0));
        params.put("pageSize", String.valueOf(5));
        request(url, params, items);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(task);
    }

    private Runnable task = new Runnable() {

        @Override
        public void run() {
            if (++currentIndex >= mImageViews.length) {
                currentIndex = 0;
            }

            mViewPager.setCurrentItem(currentIndex);
            mHandler.postDelayed(task, 5000);

        }
    };

    private View[] getImages(int count) {
        items = new View[count];
        for (int i = 0; i < items.length; i++) {
            items[i] = mInflater.inflate(R.layout.ad_item_image, mViewPager, false);
        }
        return items;
    }

    /**
     * 广告图片获取请求
     */
    private void request(String url, Map<String, String> params, final View[] views) {
        VolleyRequest.RequestPost(getActivity(), url, "ad_request", params, new
                VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {

            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<News> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<News>>() {
                        }.getType());
                        for (int i = 0; i < newses.size(); i++) {
                            News news = newses.get(i);
                            views[i].setTag(news);
                            if (!TextUtils.isEmpty(news.getFilePath())) {
                                String[] pathUrl = news.getFilePath().split(",");
                                for (String url : pathUrl) {
                                    if (url.endsWith(".jpg") || url.endsWith(".png") || url
                                            .endsWith(".JPG") || url.endsWith(".PNG") || url
                                            .endsWith(".jpeg") || url.endsWith(".JPEG") || url
                                            .endsWith(".BMP") || url.endsWith(".bmp") || url
                                            .endsWith(".gif") || url.endsWith(".GIF")) {
                                        VolleyImageLoader.setViewBgRequest(ConstantUtil
                                                .FILE_DOWNLOAD_URL + url, views[i]);
//                                        BitmapUtil.getNetBitmap(getActivity(),ConstantUtil
//                                                .FILE_DOWNLOAD_URL + url);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        }
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(getActivity());
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(getActivity(), R.string.load_fail);
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


    private class ImagePagerAdapter extends PagerAdapter {
        private View[] images;

        public ImagePagerAdapter(View[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(images[position]);
            images[position].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LogUtil.i("onclick");
                    if (images[position] != null && images[position].getTag() != null) {
                        Object tag = images[position].getTag();
                        News news = (News) tag;
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), NewsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("news", news);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
            return images[position];
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("ad_request");
    }


}
