package com.bjbsh.linford.bookapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.bjbsh.linford.bookapp.adapter.BookAdapter;
import com.bjbsh.linford.bookapp.dao.BookDaoHelper;
import com.bjbsh.linford.bookapp.entity.BookEntity;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{

    //第一页
    int offset = 0;
    //界面布局控件定义
    @BindView(R.id.book_Toolbar)
    Toolbar bookToolbar;
    @BindView(R.id.book_SwipeRefresh)
    SwipeRefreshLayout bookSwipeRefresh;
    @BindView(R.id.book_recycler_view)
    RecyclerView bookRecyclerView;
    //RecyclerView布局格式
    private LinearLayoutManager mLayoutManager;
    //数据库操作管理类
    private BookDaoHelper bookDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册Fresco图片框架
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        toolBarEvent();
        getBookData();
    }

    /**
     * 实例化控件
     */
    private void initViews() {
        bookDaoHelper=new BookDaoHelper(this);
        mLayoutManager=new LinearLayoutManager(this);
        bookRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        bookRecyclerView.setHasFixedSize(true);
        //为RecyclerView添加默认动画效果
        bookRecyclerView.setItemAnimator(new DefaultItemAnimator());

      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * ToolBar导航条处理
     */
    public void toolBarEvent() {
        //添加ActionBar按钮
        bookToolbar.inflateMenu(R.menu.toolbar_main_actionbar);
        //隐藏Title
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //ActionBar的子菜单点击事件(more更多)
        bookToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_more_add:
                        Intent addBookIntent = new Intent(MainActivity.this, AddBookActivity.class);
                        startActivity(addBookIntent);
                        break;
                    case R.id.action_addClassify:
                        Intent addClassifyIntent = new Intent(MainActivity.this, ClassifyManagerActivity.class);
                        startActivity(addClassifyIntent);
                        break;
                    case R.id.action_searchBook:
                        Intent searchBookIntent = new Intent(MainActivity.this, SearchBookActivity.class);
                        startActivity(searchBookIntent);
                        break;
                }
                return true;
            }
        });

    }

    /**
     * 数据源初始化到页面
     */
    public void getBookData(){
       List<BookEntity> bookEntityList= bookDaoHelper.queryBookAll();
        System.out.println("BookList==========>"+bookEntityList.toString());
        bookRecyclerView.setAdapter(new BookAdapter(bookEntityList,MainActivity.this));

    }

    /**
     * 自定义Toast内容弹窗
     */
    private void setToast(String toastText) {
        Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {

    }
}
