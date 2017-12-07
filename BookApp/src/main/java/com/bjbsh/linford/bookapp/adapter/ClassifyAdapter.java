package com.bjbsh.linford.bookapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bjbsh.linford.bookapp.activity.R;
import com.bjbsh.linford.bookapp.entity.ClassifyEnity;

import java.util.List;


/**
 * Created by 艾特不出先生 on 8/6 0006.
 * 为分类管理Activity自定义一个RecyclerView适配器
 */

public class ClassifyAdapter extends BaseAdapter {
    //数据集合封装
    private List<ClassifyEnity> classifyEnityList;
    //上下文
    private Context context;
    //定义item子控件回调事件
    public SubItemOnClickListener suBListener;
    //item对应状态码(getItemViewType使用)
    private static final int VIEW_TYPE_ITEM = 0X02;


    /**
     * 构造方法(MainActivity构造调用)
     *
     * @param classifyEnityList
     * @param context
     */
    public ClassifyAdapter(List<ClassifyEnity> classifyEnityList, Context context) {
        super(classifyEnityList, context);
        this.classifyEnityList = classifyEnityList;
        this.context = context;
    }


////////////////////////////////////重写父类的方法///////////////////////////////////////////////////

    /**
     * 加载item布局到adapter
     * 初始化item控件
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_classify, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * 为item加载数据和监听事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            //设置item的标题名
            ((ItemViewHolder) holder).classifyName.setText(classifyEnityList.get(position).getClassifyName());
            //把position数据放到tag存储
            ((ItemViewHolder) holder).classifyUpdate.setTag(new Long(getItemId(position)).intValue());
            ((ItemViewHolder) holder).classifyDelete.setTag(new Long(getItemId(position)).intValue());
            //更新和删除调用回调事件
            if (suBListener != null) {
                ((ItemViewHolder) holder).classifyUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回调方法     //greenDao数据库id自增是从1开始,但item是从0开始的,所以tag需要+1
                        suBListener.updateClick(v, position, (Integer) v.getTag());
                        notifyItemChanged(position);
                    }
                });


                ((ItemViewHolder) holder).classifyDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回调方法  从tag里取出position
                        suBListener.deleteClick(v, position, (Integer) v.getTag());
                        //removeItem(position);
                    }
                });
            }

        } else {
            //执行FootViewHolder
            super.onBindViewHolder(holder, position);

        }

    }


    /**
     * 定义Adapter列表显示的数量
     * 因为底部加了个FootViewHolder,所以要+1
     *
     * @return
     */
    @Override
    public int getItemCount() {

        return super.getDataSet() != null ? super.getItemCount() + 1 : 0;

    }

    /**
     * 用于query的position
     * 本类onBindViewHolder里调用
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return classifyEnityList.get(position).getClassify_id();
    }


/////////////////////////////////////定义不同ItemType的Holder/////////////////////////////////////////

    /**
     * 实例化itemHolder控件
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView classifyName;
        private Button classifyUpdate, classifyDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            classifyName = (TextView) itemView.findViewById(R.id.classifyName);
            classifyUpdate = (Button) itemView.findViewById(R.id.classifyUpdate);
            classifyDelete = (Button) itemView.findViewById(R.id.classifyDelete);
        }


    }


//////////////////////////////// ItemHolder子布局的回调方法////////////////////////////////////////////

    public void setSubItemOnClickListener(SubItemOnClickListener listener) {

        this.suBListener = listener;

    }

    public interface SubItemOnClickListener {
        void updateClick(View v, int refreshPosition, int queryPosition);

        void deleteClick(View v, int refreshPosition, int queryPosition);
    }


}
