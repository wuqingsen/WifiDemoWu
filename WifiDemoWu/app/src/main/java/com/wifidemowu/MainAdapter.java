package com.wifidemowu;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wifidemowu.bean.WifiListBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Name: 吴庆森
 * Date: 2019/9/4
 * Mailbox: 1243411677@qq.com
 * Describe:
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private List<WifiListBean> wifiListBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public MainAdapter.OnItemClickListener mOnItemClickListerer;

    public void setmOnItemClickListerer(MainAdapter.OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }

    public MainAdapter(List<WifiListBean> wifiListBeanList) {
        this.wifiListBeanList = wifiListBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText("wifi名：" + wifiListBeanList.get(position).getName());
        holder.tv_encrypt.setText("加密方式：" + wifiListBeanList.get(position).getEncrypt());
        holder.btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListerer.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wifiListBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_encrypt;
        Button btn_link;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_encrypt = view.findViewById(R.id.tv_encrypt);
            btn_link = view.findViewById(R.id.btn_link);
        }
    }
}
