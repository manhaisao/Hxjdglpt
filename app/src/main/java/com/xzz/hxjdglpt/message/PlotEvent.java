package com.xzz.hxjdglpt.message;

/**
 * Created by Administrator on 2019\4\25 0025.
 */

public class PlotEvent {

    private boolean isAdd;
    public  PlotEvent(boolean isAdd){
        this.isAdd=isAdd;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
