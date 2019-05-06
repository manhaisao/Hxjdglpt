package com.xzz.hxjdglpt.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Dangxiaozu;
import com.xzz.hxjdglpt.model.Liangxin;
import com.xzz.hxjdglpt.model.Zddw;
import com.xzz.hxjdglpt.model.Zhibu;
import com.xzz.hxjdglpt.model.Zongzhi;

/**
 * Created by chen on 2018/7/9.
 */

public class DzzDialog extends Dialog {
    private TextView tlab1;
    private TextView lab1;
    private TextView tlab2;
    private TextView lab2;
    private TextView tlab3;
    private TextView lab3;
    private TextView tlab4;
    private TextView lab4;
    private TextView tlab5;
    private TextView lab5;
    private TextView tlab6;
    private TextView lab6;
    private TextView lab7;
    private TextView mGb;
    private TextView tlab8;
    private TextView lab8;

    private Object obj;

    private int type;//

//    private String vname;
//
//    public String getVname() {
//        return vname;
//    }
//
//    public void setVname(String vname) {
//        this.vname = vname;
//    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DzzDialog(Context context) {
        super(context, R.style.Custom_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dzz_info_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
    }

    private void initView() {
        tlab1 = (TextView) findViewById(R.id.dzz_t_lab1);
        lab1 = (TextView) findViewById(R.id.dzz_lab1);
        tlab2 = (TextView) findViewById(R.id.dzz_t_lab2);
        lab2 = (TextView) findViewById(R.id.dzz_lab2);
        tlab3 = (TextView) findViewById(R.id.dzz_t_lab3);
        lab3 = (TextView) findViewById(R.id.dzz_lab3);
        tlab4 = (TextView) findViewById(R.id.dzz_t_lab4);
        lab4 = (TextView) findViewById(R.id.dzz_lab4);
        tlab5 = (TextView) findViewById(R.id.dzz_t_lab5);
        lab5 = (TextView) findViewById(R.id.dzz_lab5);
        tlab6 = (TextView) findViewById(R.id.dzz_t_lab6);
        lab6 = (TextView) findViewById(R.id.dzz_lab6);
        lab7 = (TextView) findViewById(R.id.dzz_lab7);
        tlab8 = (TextView) findViewById(R.id.dzz_t_lab8);
        lab8 = (TextView) findViewById(R.id.dzz_lab8);
        mGb = (TextView) findViewById(R.id.gb);
        mGb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initData() {
        if (obj instanceof Zongzhi && type == 1) {
            Zongzhi d = (Zongzhi) obj;
            tlab1.setText("总支书记：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("总支副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("总支委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属村居：");
            lab6.setText(d.getVname());
            String dnum = d.getDangzhibunum() == null ? "0" : d.getDangzhibunum();
            String dynum = d.getDangyuannum() == null ? "0" : d.getDangyuannum();
            lab7.setText("党总支下设" + dnum + "个党支部，共有党员" + dynum + "名");
            tlab8.setVisibility(View.GONE);
            lab8.setVisibility(View.GONE);
        } else if (obj instanceof Zhibu && type == 1) {
            Zhibu d = (Zhibu) obj;
            tlab1.setText("支部书记：");
            lab1.setText(d.getZhibuname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZhifuphone());
            tlab3.setText("支部副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("支部委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属网格：");
            lab6.setText(d.getGridid());
            tlab8.setText("所属村居：");
            lab8.setText(d.getVname());
            String dnum = d.getXiaozunum() == null ? "0" : d.getXiaozunum();
            String dynum = d.getDangyuannum() == null ? "0" : d.getDangyuannum();
            lab7.setText("党支部下设" + dnum + "个党小组，共有党员" + dynum + "名");
        } else if (obj instanceof Zhibu && type == 2) {
            Zhibu d = (Zhibu) obj;
            tlab1.setText("支部书记：");
            lab1.setText(d.getZhibuname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZhifuphone());
            tlab3.setText("支部副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("支部委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属网格：");
            lab6.setText(d.getGridid());
            tlab8.setText("所属村居：");
            lab8.setText(d.getVname());
            String dnum = d.getXiaozunum() == null ? "0" : d.getXiaozunum();
            String dynum = d.getDangyuannum() == null ? "0" : d.getDangyuannum();
            lab7.setText("党支部下设" + dnum + "个党小组，共有党员" + dynum + "名");
        } else if (obj instanceof Dangxiaozu && type == 2) {
            Dangxiaozu d = (Dangxiaozu) obj;
            tlab1.setText("组长：");
            lab1.setText(d.getName());
            tlab2.setText("联系号码：");
            lab2.setText(d.getPhone());
            tlab3.setText("党员数：");
            lab3.setText(d.getDangyuannum());
            tlab4.setText("所属村居：");
            lab4.setText(d.getVname());
            tlab5.setText("所属网格：");
            lab5.setText(d.getGridid());
            tlab6.setVisibility(View.GONE);
            lab6.setVisibility(View.GONE);
            tlab8.setVisibility(View.GONE);
            lab8.setVisibility(View.GONE);
            lab7.setVisibility(View.GONE);
        } else if (obj instanceof Zddw && type == 1) {
            Zddw d = (Zddw) obj;
            tlab1.setText("党委书记：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("党委副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("党委委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属网格：");
            lab6.setText(d.getGridId());
            tlab8.setText("所属村居：");
            lab8.setText(d.getVname());
            lab7.setText("党委下设" + d.getDxznum() + "个党支部，共有党员" + d.getDynum() + "名");
        } else if (obj instanceof Zddw && type == 2) {
            Zddw d = (Zddw) obj;
            tlab1.setText("支部书记：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("支部副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("支部委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属网格：");
            lab6.setText(d.getGridId());
            tlab8.setText("所属村居：");
            lab8.setText(d.getVname());
            lab7.setText("支部下设" + d.getDxznum() + "个党小组，共有党员" + d.getDynum() + "名");
        } else if (obj instanceof Zddw && type == 3) {
            Zddw d = (Zddw) obj;
            tlab1.setText("组长：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("党员数：");
            lab3.setText(d.getDynum());
            tlab4.setText("所属村居：");
            lab4.setText(d.getVname());
            tlab5.setText("所属网格：");
            lab5.setText(d.getGridId());
            tlab6.setVisibility(View.GONE);
            lab6.setVisibility(View.GONE);
            tlab8.setVisibility(View.GONE);
            lab8.setVisibility(View.GONE);
            lab7.setVisibility(View.GONE);
        } else if (obj instanceof Liangxin && type == 2) {
            Liangxin d = (Liangxin) obj;
            tlab1.setText("支部书记：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("支部副书记：");
            lab3.setText(d.getFuname());
            tlab4.setText("联系号码：");
            lab4.setText(d.getFuphone());
            tlab5.setText("支部委员：");
            lab5.setText(d.getChengyuannum());
            tlab6.setText("所属网格：");
            lab6.setText(d.getGridid());
            tlab8.setText("所属村居：");
            lab8.setText(d.getVname());
            lab7.setText("支部下设" + d.getDxznum() + "个党小组，共有党员" + d.getDynum() + "名");
        } else if (obj instanceof Liangxin && type == 3) {
            Liangxin d = (Liangxin) obj;
            tlab1.setText("组长：");
            lab1.setText(d.getZongname());
            tlab2.setText("联系号码：");
            lab2.setText(d.getZongphone());
            tlab3.setText("党员数：");
            lab3.setText(d.getDynum());
            tlab4.setText("所属村居：");
            lab4.setText(d.getVname());
            tlab5.setText("所属网格：");
            lab5.setText(d.getGridid());
            tlab6.setVisibility(View.GONE);
            lab6.setVisibility(View.GONE);
            tlab8.setVisibility(View.GONE);
            lab8.setVisibility(View.GONE);
            lab7.setVisibility(View.GONE);
        }
//        else if (obj instanceof Zhibu && type == 4) {
//            Zhibu d = (Zhibu) obj;
//            tlab1.setText("支部书记：");
//            lab1.setText(d.getZhibuname());
//            tlab2.setText("联系号码：");
//            lab2.setText(d.getZhifuphone());
//            tlab3.setText("支部副书记：");
//            lab3.setText(d.getFuname());
//            tlab4.setText("联系号码：");
//            lab4.setText(d.getFuphone());
//            tlab5.setText("支部委员：");
//            lab5.setText(d.getChengyuannum());
//            tlab6.setText("所属网格：");
//            lab6.setText(d.getGridid());
//            lab7.setText("党员数：" + d.getDangyuannum());
//        } else if (obj instanceof Dangxiaozu && type == 4) {
//            Dangxiaozu d = (Dangxiaozu) obj;
//            tlab1.setText("组长：");
//            lab1.setText(d.getName());
//            tlab2.setText("联系号码：");
//            lab2.setText(d.getPhone());
//            tlab3.setText("党员数：");
//            lab3.setText(d.getDangyuannum());
//            tlab4.setText("所属网格：");
//            lab4.setText(d.getGridid());
//        }

    }
}
