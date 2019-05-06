package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.Village;

import java.util.List;

public class LsVillageAdapter extends RecyclerView.Adapter<LsVillageAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Village> list;
    private OnItemClickListener onItemClickListener;

    public LsVillageAdapter(Context context, List<Village> list){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_village_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvVillage.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvVillage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvVillage = (TextView) itemView.findViewById(R.id.tv_village);
        }
    }

    //设置点击事件的方法
    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
