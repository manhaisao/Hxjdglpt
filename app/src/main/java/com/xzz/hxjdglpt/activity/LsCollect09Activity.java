package com.xzz.hxjdglpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzz.hxjdglpt.model.Grid;
import com.xzz.hxjdglpt.model.Village;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 村（城乡居民养老+医疗）
 */
@ContentView(R.layout.activity_ls_collect_09)
public class LsCollect09Activity extends BaseActivity{

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.hx_title)
    private TextView hXtitle;
    @ViewInject(R.id.hx_btn_right)
    private TextView hxBtnRight;

    @ViewInject(R.id.village_insuranced_add)
    private ImageView villageInsurancedAdd;
    @ViewInject(R.id.village_medical_add)
    private ImageView villageMedicalAdd;

    private int selectType;
    private Grid grid;
    private Village village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        selectType = getIntent().getIntExtra("type",0);
        village = getIntent().getParcelableExtra("village");
        grid = getIntent().getParcelableExtra("grid");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        hXtitle.setText("XXXX");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Event(value = {R.id.iv_back,R.id.village_insuranced_add,R.id.village_medical_add}, type = View.OnClickListener.class)
    private void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.village_insuranced_add:
                intent.setClass(this, ListActivity.class);
                intent.putExtra("type", 71);
                intent.putExtra("menuType",1);
                intent.putExtra("selectType",selectType);
                if(selectType == 1){
                    intent.putExtra("village",village);
                }else if(selectType == 2){
                    intent.putExtra("grid",grid);
                }
                startActivity(intent);
                break;
            case R.id.village_medical_add:
                intent.setClass(this, ListActivity.class);
                intent.putExtra("type", 72);
                intent.putExtra("menuType",2);
                intent.putExtra("selectType",selectType);
                if(selectType == 1){
                    intent.putExtra("village",village);
                }else if(selectType == 2){
                    intent.putExtra("grid",grid);
                }
                startActivity(intent);
                break;
        }
    }

}
