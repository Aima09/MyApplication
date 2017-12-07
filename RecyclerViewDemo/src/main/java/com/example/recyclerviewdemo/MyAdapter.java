package com.example.recyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 艾特不出先生 on 8/3 0003.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ItemBean> list;

    public MyAdapter(List<ItemBean> list) {
        this.list = list;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview,parent,false);
        ViewHolder  viewHolder=new ViewHolder(view);
        return viewHolder;
    }



    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).ItemTitle);
        holder.content.setText(list.get(position).ItemContent);
        holder.imageView.setImageResource(list.get(position).ItemImageResid);

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title,content;
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
            content= (TextView) itemView.findViewById(R.id.content);
            imageView= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
