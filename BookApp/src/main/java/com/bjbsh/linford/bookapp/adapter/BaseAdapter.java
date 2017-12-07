package com.bjbsh.linford.bookapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjbsh.linford.bookapp.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 艾特不出先生 on 8/11 0011.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //定义一个自定义item点击回调接口
    private OnMyItemClickListener listener;
    //定义原始数据集合
    protected List<T> dataSet = new ArrayList<>();
    //定义一个获取Item的position供与其他回调方法使用(
    public int refreshPosition;

    private Context context;
    //给Holder加个type,getItemViewType使用
    private static final int VIEW_TYPE_TITLE = 0X01;
    private static final int VIEW_TYPE_ITEM = 0X02;
    private static final int VIEW_TYPE_FOOTER = 0X04;

    //加载数据时的状态,changeState调用
    public static final int LOAD_STATE_START = 0001;
    public static final int LOAD_STATE_LOADING = 1111;
    public static final int LOAD_STATE_NOMOREDATE = 0000;
    //默认状态为1
    private int FOOT_STATE = 0001;

    public BaseAdapter() {
    }

    public BaseAdapter(List<T> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    ////////////////////////////////重写父类的方法///////////////////////////////////////
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
      if (viewType == VIEW_TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //把position赋值给全局变量
        refreshPosition=position;
        if (listener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v,position);
                }
            });
            // set LongClick
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                  //  int position = holder.getLayoutPosition();
                    listener.mLongClick(v,position);
                    return true;
                }
            });
        }
                 //////////////////////////////////////
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (position == 0) {
                footerViewHolder.progressBar.setVisibility(View.GONE);
                footerViewHolder.stateText.setText("");
            }

            //FOOT_STATE在changState()方法里变化
            switch (FOOT_STATE) {
                case LOAD_STATE_START:
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.stateText.setText("上拉加载更多");
                    break;
                case LOAD_STATE_LOADING:
                    footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                    footerViewHolder.stateText.setText("正在加载....");
                    break;
                case LOAD_STATE_NOMOREDATE:
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                    footerViewHolder.stateText.setText("没有数据了,已经到底了!");
                    break;
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        //如果列表没有数据了返回到底部的视图
        if (position + 1 == getItemCount()) {
            return VIEW_TYPE_FOOTER;
        }
        //如果还有数据则显示加载更多
        return VIEW_TYPE_ITEM;
    }



////////////////////////////////自定义的方法/////////////////////////////////////////

    /**
     * 更新数据
     *
     * @param dataSet
     */
    public void updateData(List dataSet) {
        this.dataSet.clear();
        appendData(dataSet);
    }

    /**
     * 累加数据
     *
     * @param dataSet
     */
    public void appendData(List dataSet) {
        if (dataSet != null && !dataSet.isEmpty()) {
            this.dataSet.addAll(dataSet);
            notifyDataSetChanged();
        }
    }

    /**
     * 返回最初数据集合
     *
     * @return
     */
    public List<T> getDataSet() {
        return dataSet;
    }

    /**
     * 定义adapter的列表显示的数据数量
     * @return
     */

//////////////////////////////////////////////////////////////////

    /**
     * 刷新list集合数据时调用(插入数据)
     *
     * @param
     */
    public void setEnityList(List<T> list) {
        this.dataSet = list;
    }

    /**
     * 根据状态state刷新数据(Activity里调用刷新)
     * 在
     *
     * @param position
     */
    public void changeState(int position) {
        this.FOOT_STATE = position;
        notifyDataSetChanged();


    }

    /**
     * 从View中移除一个item
     *
     * @param position
     */
    public void removeItem(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
    }

    /**
     * 从View中添加一个item
     *
     * @param position
     */
    public void addItem(int position) {
        notifyItemInserted(position);
        notifyItemRangeChanged(position, dataSet.size());
    }

    /**
     * 页面添加一条数据时调用此方法
     *
     * @param enity
     */
    public void addEnity(T enity) {
        dataSet.add(enity);
    }

//////////////////////////////////定义item点击和长按的回调方法////////////////////////////////////////


    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;

    }

    public interface OnMyItemClickListener{
        //单击事件
        void myClick(View v, int pos);
        //长按事件
        void mLongClick(View v,int pos);
    }


//////////////////////////////// ItemHolder子布局的回调方法////////////////////////////////////////////

    //定义回调事件
    public SubItemOnClickListener suBListener;


    public void setSubItemOnClickListener(SubItemOnClickListener listener) {

        this.suBListener = listener;

    }

    public interface SubItemOnClickListener {
        void updateClick(View v, int refreshPosition, int queryPosition);

        void deleteClick(View v, int refreshPosition, int queryPosition);
    }


/////////////////////////////////FootViewHolder////////////////////////////////////////////////////

    /**
     * 上拉加载顶部View视图
     */
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView stateText;
        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            stateText = (TextView) itemView.findViewById(R.id.stateText);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

}
