package com.xzz.hxjdglpt.signView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Qd;
import com.xzz.hxjdglpt.utils.LogUtil;

import java.util.List;

/**
 * 日历 适配器
 * Created by aiyang on 2017/4/14.
 */

public class RegistrationAdapter extends BaseAdapter {
    private Context context;
    private final int days;
    private final int week;
    private int[] dayNumber;
    private final int day;
    private List<Qd> qds;

    public RegistrationAdapter(Context context, int days, int week, int day, List<Qd> qds) {
        this.context = context;
        this.days = days;
        this.week = week;
        this.day = day;
        this.qds = qds;
        getEveryDay();
    }


    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public String getItem(int i) {

        return null;
    }

    @Override
    public long getItemId(int i) {
        return dayNumber[i];
    }//点击时

    private ViewHolder viewHolder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_registrationadatper, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.day.setText(dayNumber[i] == 0 ? "" : dayNumber[i] + "");
//        if (dayNumber[i] != 0 && dayNumber[i] < day && dayNumber[i] % 2 == 1) {
//            viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
//        }
        for (int j = qds.size() - 1; j >= 0; j--) {
            String dayStr = qds.get(j).getTime().substring(8, 10);
            if (Integer.valueOf(dayStr) == Integer.valueOf(dayNumber[i])) {
                if (qds.get(j).getType() == 1) {
                    viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
                } else if (qds.get(j).getType() == 2) {
                    viewHolder.day.setBackgroundResource(R.mipmap.qj);
                }
                break;
            }
        }
//        for (Qd qd : qds) {
//            String dayStr = qd.getTime().substring(8, 10);
//            if (Integer.valueOf(dayStr) == Integer.valueOf(dayNumber[i])) {
//                if (qd.getType() == 1) {
//                    viewHolder.day.setBackgroundResource(R.mipmap.member_ok);
//                } else if (qd.getType() == 2) {
//                    viewHolder.day.setBackgroundResource(R.mipmap.qj);
//                }
//                switch (qd.getType()) {
//                    case 1:
//                        viewHolder.day.setText("上下午都签");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_sqxq);
//                        break;
//                    case 2:
//                        viewHolder.day.setText("上签、下请");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_sqxs);
//                        break;
//                    case 3:
//                        viewHolder.day.setText("上请、下签");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_ssxq);
//                        break;
//                    case 4:
//                        viewHolder.day.setText("上请、下请");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_ssxs);
//                        break;
//                    case 5:
//                        viewHolder.day.setText("上签，下未");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_sqxw);
//                        break;
//                    case 6:
//                        viewHolder.day.setText("上请，下未");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_ssxw);
//                        break;
//                    case 7:
//                        viewHolder.day.setText("上未，下簽");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_swxq);
//                        break;
//                    case 8:
//                        viewHolder.day.setText("上未，下請");
//                        viewHolder.day.setText("");
//                        viewHolder.day.setBackgroundResource(R.mipmap.q_swxs);
//                        break;
//                }
//                break;
//            }
//        }
//        if (dayNumber[i] == day) {
//            viewHolder.day.setText("今");
//            view.setBackgroundResource(R.color.colorPrimary);
//            viewHolder.day.setTextColor(Color.parseColor("#ffffff"));
//        }

        return view;
    }

    private class ViewHolder {
        private TextView day;

        public ViewHolder(View view) {
            this.day = (TextView) view.findViewById(R.id.day);
        }
    }

    /**
     * 得到42格子 每一格子的值
     */
    private void getEveryDay() {
        dayNumber = new int[42];

        for (int i = 0; i < 42; i++) {
            if (i < days + week && i >= week) {
                dayNumber[i] = i - week + 1;
            } else {
                dayNumber[i] = 0;
            }
        }
    }
}
