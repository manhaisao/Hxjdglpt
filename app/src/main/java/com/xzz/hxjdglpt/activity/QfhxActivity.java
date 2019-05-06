package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xzz.hxjdglpt.adapter.QfhxAdapter;
import com.xzz.hxjdglpt.adapter.QfhxDtAdapter;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView;
import com.xzz.hxjdglpt.customview.autolistview.AutoListView2;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019\3\7 0007.
 */
@ContentView(R.layout.activity_qfhx)
public class QfhxActivity  extends BaseActivity implements AutoListView2.OnRefreshListener,
        AutoListView2.OnLoadListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.hx_title)
    private TextView tvTitle;
    @ViewInject(R.id.qfhx_listview)
    private AutoListView2 listView;
    @ViewInject(R.id.qfhx_btn)
    private Button allBtn;

    @ViewInject(R.id.qfhx_content)
    private  TextView qfhx_content;

    private ImageLoader mImageLoader;
    private String content="河下街道纪工委在区纪委和街道党工委的正确领导下，认真协助区纪委第六监察室和街道党工委开展党风廉政建设工作，突出主业主责，强化监督执纪，为河下街道经济社会发展提供了坚强的政治和纪律保证。具体工作职责如下： \n" +
            "1、负责贯彻落实党中央和省、市、区委关于加强党风廉政建设的决定,维护党的章程和其他党内法规,检查党的路线、方针、政策和决议执行情况，负责对党风廉政建设责任制执行情况进行监督检查。\n" +
            "2、负责贯彻落实上级纪委相关工作的决定, 监督检查本街道各部门及其工作人员执行国家法律、法规、政策和街道党工委的决定、决议、命令情况；负责统一部署全街道纠正行业不正之风工作并进行监督、检查。\n" +
            "3、负责组织协调全街道党风廉政建设和反腐败工作,科学运用监督执纪“四种形态”。\n" +
            "4、负责调查处理本街道各部门及其工作人员违反党纪、国家法律、法规、政策的行为,并根据责任人所犯错误,按有关程序,作出党政纪处分。\n" +
            "5、受理群众对党员干部和监察对象违纪行为的检举、控告并进行调查处理。\n" +
            "6、制定落实关于加强党风廉政建设的办法及维护党纪的决定,党风廉政建设和党纪政纪教育计划;做好党的纪律检查工作和行政监察工作的方针、政策、法律、法规的宣传工作;对街道、村（居）党员和干部进行遵纪守法、廉洁从政的教育。";
    private User user;
    private BaseApplication application;
    private List<Role> roles;
    private QfhxAdapter rdzjDtAdapter;
    private List<Qfhx> list = new ArrayList<Qfhx>();
    private int pageIndex = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        application = (BaseApplication) getApplication();
        if (user == null) {
            user = application.getUser();
        }
        mImageLoader = application.getImageLoader();
        roles = application.getRolesList();
        qfhx_content.setText(content);
        initView();
        initData();
    }

    public void initView() {
        tvTitle.setText(getString(R.string.qfrx));
        rdzjDtAdapter = new QfhxAdapter(this, list,mImageLoader);
        listView.setAdapter(rdzjDtAdapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnItemClickListener(this);
    }
    public void initData() {
        loadData(AutoListView.REFRESH);
    }


    @Event(value = {R.id.iv_back,R.id.qfhx_ywkd,R.id.qfhx_lzjy,R.id.xfhx_jjdt_btn,R.id.xfhx_jcdt_btn,R.id.qfhx_btn,R.id.qfhx_dctb,R.id.qfhx_xfjd}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent  intent=new Intent(QfhxActivity.this,QfhxListActivity.class);

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.qfhx_ywkd:
                //要闻快递
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.qfhx_lzjy:
                //廉政教育
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.xfhx_jjdt_btn:
                //纪检动态
                intent.putExtra("type","3");
                startActivity(intent);
                break;
            case R.id.xfhx_jcdt_btn:
                //监察动态
                intent.putExtra("type","4");
                startActivity(intent);
                break;
            case R.id.qfhx_btn:
                if ("展开".equals(allBtn.getText())) {
                    allBtn.setText("收起");
                    Drawable drawable = getResources().getDrawable(R.mipmap.min_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    qfhx_content.setMaxLines(10000);
                } else if ("收起".equals(allBtn.getText())) {
                    allBtn.setText("展开");
                    Drawable drawable = getResources().getDrawable(R.mipmap.all_arrow);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable
                            .getMinimumHeight());
                    allBtn.setCompoundDrawables(null, null, drawable, null);
                    qfhx_content.setMaxLines(5);
                }

                break;
            case R.id.qfhx_dctb:
                Intent intent1=new Intent();
                intent1.setClass(QfhxActivity.this, XinfangActivity.class);
                intent1.putExtra("type","督查通报");
                startActivity(intent1);
                break;
            case R.id.qfhx_xfjd:
                Intent intent2=new Intent();
                intent2.setClass(QfhxActivity.this, XfjdActivity.class);
                startActivity(intent2);
                break;
        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Qfhx> result = (List<Qfhx>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            rdzjDtAdapter.notifyDataSetChanged();
        }
    };

    private void loadData(final int what) {
       String url = ConstantUtil.BASE_URL + "/m_qfhx/queryQfhxList";
         HashMap<String, String> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("token", user.getToken());
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("type","3");
        params.put("pageSize", String.valueOf(ConstantUtil.PAGE_SIZE));
        VolleyRequest.RequestPost(this, url, "queryQfhxList", params, new VolleyListenerInterface
                (this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) {
                LogUtil.i("onMySuccess");
                Log.e("insert",result.toString());
//                LogUtil.i(result.toString());
                try {
                    String resultCode = result.getString("resultCode");
                    // resultCode 1:成功查询 ；2：token不一致；3：查询失败
                    if ("1".equals(resultCode)) {
                        JSONArray jsonArray = result.getJSONArray("data");
                        LogUtil.i(jsonArray.toString());
                        Gson gson = new Gson();
                        List<Qfhx> newses = gson.fromJson(jsonArray.toString(), new
                                TypeToken<List<Qfhx>>() {
                                }.getType());
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = newses;
                        handler.sendMessage(msg);
                    } else if ("2".equals(resultCode)) {
                        DialogUtil.showTipsDialog(QfhxActivity.this);
                    } else if ("3".equals(resultCode)) {
                        ToastUtil.show(QfhxActivity.this, R.string.load_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                LogUtil.i("onMyError");
                // 返回失败的原因
                LogUtil.i(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getRequestQueue().cancelAll("queryQfhxList");
    }
    @Override
    public void onRefresh() {
        pageIndex = 0;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        pageIndex = pageIndex + ConstantUtil.PAGE_SIZE;
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i - 1 < list.size()) {
            Intent intent = new Intent();
            intent.setClass(QfhxActivity.this, QfhxDtDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("qfhx", list.get(i - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
