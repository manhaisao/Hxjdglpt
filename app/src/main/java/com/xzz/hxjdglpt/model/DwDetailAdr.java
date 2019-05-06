package com.xzz.hxjdglpt.model;

import com.bin.david.form.annotation.SmartColumn;

import java.util.List;

/**
 * Created by Administrator on 2019\3\25 0025.
 */

public class DwDetailAdr {
    @SmartColumn(id = 2,name ="点位名称")
     String name;
    @SmartColumn(id = 3,name ="所在村居")
     String name1;
    @SmartColumn(id = 4,name ="详细地址")
     String name2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
