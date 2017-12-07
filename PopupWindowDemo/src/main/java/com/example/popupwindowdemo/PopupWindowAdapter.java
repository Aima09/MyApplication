package com.example.popupwindowdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by 艾特不出先生 on 12/3 0003.
 */

public class PopupWindowAdapter extends RecyclerView.Adapter {
    private static Context context;
    private static String[] sourceStr;
    //定义item点击事件
    private MyItemClickListener mItemClickListener;

    public PopupWindowAdapter(Context context, String[] sourceStr) {
        this.context = context;
        this.sourceStr = sourceStr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popupwindow_item_main, parent, false);

        return new ItemViewHolder(view, mItemClickListener);

        //return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //将position保存在itemView的Tag中,ViewHolder类中item数据赋值使用
            holder.itemView.setTag(position);
            //必须传个数据库id过去而不是item的position
            ((ItemViewHolder) holder).initViews(position);
        }
    }

    @Override
    public int getItemCount() {
        return sourceStr.length;
    }

    private static int selectedPosition = -1;

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    /**
     * 实例化MainActivity的item布局控件
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @butterknife.BindView(R.id.item_selected)
        ImageView itemSelected;
        @butterknife.BindView(R.id.play_source)
        TextView playSource;

        private MyItemClickListener mListener;


        public ItemViewHolder(View itemView, MyItemClickListener ItemClickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            //将全局的监听赋值给接口
            this.mListener = ItemClickListener;
            itemView.setOnClickListener(this);

        }

        /**
         * 初始化控件
         */
        public void initViews(int position) {
            //设置选中效果
            if (selectedPosition == position) {
                itemSelected.setImageResource(R.mipmap.play_source_list_select_ic_true);
                playSource.setTextColor(Color.RED);
            } else {
                itemSelected.setImageResource(R.mipmap.play_source_list_select_ic_false);
                playSource.setTextColor(Color.WHITE);
            }

            playSource.setText(sourceStr[position]);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, (int) this.itemView.getTag());
            }
        }
    }


////////////////////////////////定义item点击回调事件///////////////////////////////////////////

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);

    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}