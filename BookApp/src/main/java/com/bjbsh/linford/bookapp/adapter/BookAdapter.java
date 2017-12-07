package com.bjbsh.linford.bookapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjbsh.linford.bookapp.activity.R;
import com.bjbsh.linford.bookapp.entity.BookEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 艾特不出先生 on 8/6 0006.
 * 为主页MainActivity自定义一个RecyclerView适配器
 */

public class BookAdapter extends BaseAdapter {
    private static List<BookEntity> bookEntityList;
    private Context context;
    //ItemViewHolder的状态(BaseAdapter里封装了状态)
    private static final int VIEW_TYPE_ITEM = 0X02;
    //定义item点击事件
    private static ItemOnClickListener itemOnClickListener;
    //定义Item长按事件
    private ItemOnLongClickListener itemOnLongClickListener;

    /**
     * 构造方法
     *
     * @param bookEntityList
     * @param context
     */
    public BookAdapter(List<BookEntity> bookEntityList, Context context) {
        super(bookEntityList, context);
        this.bookEntityList = bookEntityList;
        this.context = context;
    }

    /**
     * 初始化item布局,把item布局放到adapter里面
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);

    }

    /**
     * 为item控件加载数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //将position保存在itemView的Tag中,ViewHolder类中item数据赋值使用
            holder.itemView.setTag(position);
            //必须传个数据库id过去而不是item的position
            ((ItemViewHolder) holder).initViews(new Long(getItemId(position)).intValue());
        }
        super.onBindViewHolder(holder, position);
    }

    /**
     * 返回数据集合的大小
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
     * 返回数据的id
     *
     * @param position Item的position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return bookEntityList.get(position).getBook_id();
    }
/////////////////////////////////////定义ItemViewHolder////////////////////////////////////////////////

    /**
     * 实例化MainActivity的item布局控件
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Index_bookImage)
        ImageView IndexBookImage;
        @BindView(R.id.Index_bookName)
        TextView IndexBookName;
        @BindView(R.id.Index_bookClassify)
        TextView IndexBookClassify;
        @BindView(R.id.Index_bookUpdate)
        Button IndexBookUpdate;
        @BindView(R.id.Index_bookType)
        TextView IndexBookType;
        @BindView(R.id.Index_bookDelete)
        Button IndexBookDelete;
        private ItemOnClickListener onClickListener;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        /**
         * 初始化控件
         *
         * @param position
         */
        public void initViews(final int position) {
            IndexBookName.setText(bookEntityList.get((Integer) this.itemView.getTag()).getBook_name());
            Uri imageUri=Uri.parse(bookEntityList.get((Integer) this.itemView.getTag()).getBook_imgUrl());
            IndexBookImage.setImageURI(imageUri);
            IndexBookType.setText(bookEntityList.get((Integer) this.itemView.getTag()).getBook_type());
            IndexBookClassify.setText(bookEntityList.get((Integer) this.itemView.getTag()).getBook_classifyName());
            if (itemOnClickListener != null) {
                IndexBookUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemOnClickListener.onItemUpdateOnClick(view, position);
                    }
                });
                IndexBookDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemOnClickListener.onItemDeleteOnClick(view, position);
                    }
                });
            }

        }


    }

    ////////////////////////////////定义item点击回调事件///////////////////////////////////////////
    public interface ItemOnClickListener {
        void onItemUpdateOnClick(View view, int position);

        void onItemDeleteOnClick(View view, int position);
    }

    public void setItemOnClickListener(ItemOnClickListener onClickListener) {
        this.itemOnClickListener = onClickListener;
    }

    public interface ItemOnLongClickListener {
        void onLongClickListener(View view, int position);
    }


    public void setItemOnLongClickListener(ItemOnLongClickListener onLongClickListener) {
        this.itemOnLongClickListener = onLongClickListener;
    }

}
