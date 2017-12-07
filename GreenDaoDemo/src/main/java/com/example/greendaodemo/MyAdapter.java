package com.example.greendaodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linfrod.greendao.entity.User;

import java.util.List;

/**
 * Created by 艾特不出先生 on 7/30 0030.
 */

public class MyAdapter extends BaseAdapter {
    private List<User> list;
    private Context context;
    private LayoutInflater myInFlater;

    public MyAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
        this.myInFlater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view=myInFlater.inflate(R.layout.item_listview,null);
            viewHolder.name= (TextView) view.findViewById(R.id.name);
            viewHolder.age= (TextView) view.findViewById(R.id.age);
            viewHolder.sex= (TextView) view.findViewById(R.id.sex);
            viewHolder.salary= (TextView) view.findViewById(R.id.salary);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        User user=list.get(i);
        viewHolder.name.setText(user.getName());
        viewHolder.age.setText(user.getAge());
        viewHolder.sex.setText(user.getSex());
        viewHolder.salary.setText(user.getSalary());
        return view;
    }

    class ViewHolder{
        public TextView name,age,sex,salary;
    }
}
