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
 * Created by 艾特不出先生 on 8/11 0011.
 */

public class RV_ClassifyAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    //数据集合封装
    private List<ClassifyEnity> classifyEnityList;
    //上下文
    private Context context;
    //position
    public int refreshPosition;

    //给Holder加个type,getItemViewType使用
    private static final int VIEW_TYPE_TITLE = 0X01;
    private static final int VIEW_TYPE_ITEM = 0X02;
    private static final int VIEW_TYPE_FOOTER = 0X04;

    //加载数据时的状态,changeState调用
    private static final int LOAD_STATE_START = 0001;
    private static final int LOAD_STATE_LOADING = 1111;
    private static final int LOAD_STATE_NOMOREDATE = 0000;
    //默认状态为1
    private int FOOT_STATE = 0001;


    /**
     * 构造方法(MainActivity构造调用)
     *
     * @param classifyEnityList
     * @param context
     */
    public RV_ClassifyAdapter(List<ClassifyEnity> classifyEnityList, Context context) {
        this.classifyEnityList = classifyEnityList;
        this.context = context;
    }

    public RV_ClassifyAdapter(Context context) {
        this.context = context;
    }

    public void setClassifyEnityList(List<ClassifyEnity> classifyEnityList) {
        this.classifyEnityList = classifyEnityList;
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

          View  view = LayoutInflater.from(context).inflate(R.layout.item_classify, parent, false);
            return new ItemViewHolder(view);

    }

    /**
     * 为item加载数据和监听事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        refreshPosition = position;
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).classifyName.setText(classifyEnityList.get(position).getClassifyName());
            ((ItemViewHolder) holder).classifyUpdate.setTag(new Long(getItemId(position)).intValue());
            ((ItemViewHolder) holder).classifyDelete.setTag(new Long(getItemId(position)).intValue());
            if (listener != null) {
                ((ItemViewHolder) holder).classifyUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.updateClick(v, position, (Integer) v.getTag());
                        notifyItemChanged(position);
                    }
                });


                ((ItemViewHolder) holder).classifyDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.deleteClick(v, position, (Integer) v.getTag());
                        //removeItem(position);
                    }
                });
            }

        }


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


////////////////////////////////自定义的方法//////////////////////////////////////////



    /**
     * 从View中移除一个item
     *
     * @param position
     */
    public void removeItem(int position) {
        classifyEnityList.remove(position);
        notifyItemRemoved(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, classifyEnityList.size());
    }

    /**
     * 从View中添加一个item
     *
     * @param position
     */
    public void addItem(int position) {
        notifyItemInserted(position);
        notifyItemRangeChanged(position, classifyEnityList.size());
    }


//////////////////////////////// ItemHolder子布局的回调方法////////////////////////////////////////////

    //定义回调事件
    private SubItemOnClickListener listener;

    public void setSubItemOnClickListener(SubItemOnClickListener listener) {
        this.listener = listener;

    }

    public interface SubItemOnClickListener {
        void updateClick(View v, int refreshPosition, int queryPosition);

        void deleteClick(View v, int refreshPosition, int queryPosition);
    }


}
