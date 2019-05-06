package com.xzz.hxjdglpt.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzz.hxjdglpt.activity.LsWorkstationSecondary;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.model.ls.Work;

import java.util.List;

public class LsWorkstationAdapter extends RecyclerView.Adapter<LsWorkstationAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Work> list;

    public LsWorkstationAdapter(Context context, List<Work> list){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_workstation_item,parent,false);
        return new LsWorkstationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvWorkstation.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("work",list.get(position));
                Intent intent = new Intent(context, LsWorkstationSecondary.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWorkstation;
        public ViewHolder(View itemView) {
            super(itemView);
            tvWorkstation = (TextView) itemView.findViewById(R.id.tv_workstation);
        }
    }
}
