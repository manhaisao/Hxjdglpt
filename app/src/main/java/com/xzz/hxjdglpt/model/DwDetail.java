package com.xzz.hxjdglpt.model;

import com.bin.david.form.annotation.ColumnType;
import com.bin.david.form.annotation.SmartColumn;

import java.util.List;

/**
 * Created by Administrator on 2019\3\25 0025.
 */

public class DwDetail {
    @SmartColumn(id = 1,name ="点位类型")
     String name;
    @SmartColumn(type = ColumnType.ArrayChild)
     List<DwDetailAdr> adrs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DwDetailAdr> getAdrs() {
        return adrs;
    }

    public void setAdrs(List<DwDetailAdr> adrs) {
        this.adrs = adrs;
    }
}
