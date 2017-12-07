package com.bjbsh.linford.bookapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bjbsh.linford.bookapp.adapter.ClassifyAdapter;
import com.bjbsh.linford.bookapp.dao.ClassifyDaoHelper;
import com.bjbsh.linford.bookapp.entity.ClassifyEnity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.offset;
import static com.bjbsh.linford.bookapp.activity.R.id.classify_SwipeRefresh;


/**
 * 主题:分类管理页面
 * 使用到的技术:RecyclerView,GreenDao,ToolBar,AlertDialog
 * <p>
 * 相关文件关联:
 * 布局(layout:activity_classify_manager.xml+Menu:toolbar_classify_actionbar.xml,values:styles.xml,drawable:bolder.xml)
 * ClassifyAdapter.java(RecyclerView的适配器)
 * <p>
 * 关键经验:
 * 1.Enity类型和接收数据String类型的转换
 * 2.Adapter回调方法子控件点击事件
 * 3.Adapter传过来的position与表id不一致问题(回调时传入getItemId()方法)
 * 4.Insert一条数据时,除了Adapter要刷新,数据源list也要重新刷新一下(重新查询一下),并赋值给adapter的list对象
 * 5.dialogView(AlertDialog自定义子布局)必须要在AlertView实例化后才能实例化
 * 6.AlertDialog按钮点击事件如果有view数据验证要隔离开来
 * <p>
 * 未解决的问题:
 * 1.IllegalStateException 1
 * 2.添加数据不能动态刷新 1
 * 3.更新数据无法接受EditText数据问题 1  :获取EditText数据一定要放在点击事件内,否则无法获取改变后的数据
 * 4.添加数据时候不能过滤null值 1 :在添加点击事件前进行数据为null的判断
 * 5.上拉加载无法执行第二次 0
 */
public class ClassifyManagerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.classifyToolbar)
    Toolbar classifyToolbar;
    @BindView(R.id.classifyRecyclerView)
    RecyclerView classifyRecyclerView;
    @BindView(classify_SwipeRefresh)
    SwipeRefreshLayout classifySwipeRefresh;
    //界面布局控件定义

    //RecyclerView布局格式
    private LinearLayoutManager mLayoutManager;
    //数据库操作管理类
    private ClassifyDaoHelper classifyDaoHelper;
    //定义适配器
    private ClassifyAdapter classifyAdapter;
    //定义数据库查询结果实体类
    private ClassifyEnity classifyEnityOfUpdate, classifyEnityOfDelete;

    //定义一个数据集合封装新添加的"分类"
    private List<ClassifyEnity> classifyEnityList;

    //点击更新后的AlertDialog弹窗视图
    private View dialogView;
    //存储dialogView里的EditText文本内容
    private String classifyNameOfDialogViewString, sortNameOfDialogViewString;

    //可见item列表的最后一个
    int lastVisibleItem;
    //RecyclerVIew显示的第几页
    int page = 0;
    boolean isLoading = false;//用来控制进入getdata()的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_manager);
        ButterKnife.bind(this);
        //实例化控件
        initView();
        //配置RecyclerView
        recyclerViewEvent();
        //ToolBar事件
        toolBarEvent();
    }

    /**
     * 初始化控件
     */
    public void initView() {

//        titleText = (TextView) findViewById(R.id.classifyTitleText);
//        titleText.setText("分类管理");
        // back = (ImageButton) findViewById(R.id.classifyBackButton);
        //     more = (ImageButton) findViewById(R.id.classifyMore);
        //事件
//        back.setOnClickListener(this);
        //       more.setOnClickListener(this);
        //实例化ClassifyDao
        //实例化Classify数据库管理类
        classifyDaoHelper = new ClassifyDaoHelper(ClassifyManagerActivity.this);
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        //下拉刷新监听事件
        classifySwipeRefresh.setOnRefreshListener(this);

    }

    /**
     * ToolBar导航条处理
     */
    public void toolBarEvent() {
        //添加ActionBar按钮
        classifyToolbar.inflateMenu(R.menu.toolbar_classify_actionbar);
        //菜单条点击事件(返回按钮事件)
        classifyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassifyManagerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //ActionBar的子菜单点击事件(more更多)
        classifyToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_more_add:
                        //使用AlertDialog弹窗
                        AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setTitle("请添加一个分类");
                        builder.setPositiveButton("确定", null);
                        builder.setNegativeButton("取消", null);

                        //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                        View view2 = LayoutInflater.from(ClassifyManagerActivity.this).inflate(R.layout.dialog_classifyadd, null);
                        //设置我们自己定义的布局文件作为弹出框的ContentView
                        builder.setView(view2);
                        //获取自定义dialogview
                        final TextView classifyText = (TextView) view2.findViewById(R.id.classifyText);
                        TextView sortText = (TextView) view2.findViewById(R.id.sortText);
                        classifyText.setText("分类:");
                        sortText.setText("排序:");
                        final EditText classifyName = (EditText) view2.findViewById(R.id.classifyEdit);
                        final EditText sortName = (EditText) view2.findViewById(R.id.sortEdit);

                        /**
                         * 把Dialog的点击事件分离开来,可避免在数据验证前关闭弹窗
                         */
                        final AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button positionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                positionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String a = classifyName.getText().toString().trim();
                                        String b = sortName.getText().toString().trim();
                                        //防止sortName输入框输入的是字符,捕捉一下异常
                                        Pattern pattern = Pattern.compile("[0-9]*");
                                        Matcher matcher = pattern.matcher(b);
                                        if (a.equals("") || b.equals("")) {
                                            setToast("添加数据不能为空!");
                                            return;
                                        } else if (!matcher.matches()) {//正则表达式验证输入框是否为数字
                                            setToast("请输入正确的数字");                                            //不执行后面代码
                                            return;
                                        }
                                        ClassifyEnity classifyEnity = new ClassifyEnity(null, a, Integer.valueOf(b), 1);
                                        //插入实体数据
                                        classifyDaoHelper.insertClassify(classifyEnity);
                                        //将Long类型转化为int类型
                                        // position=(new Long(classifyEnity.getClassify_id()).intValue())-1;
                                        //重新获取数据
                                        //  classifyEnityList = classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1)).list();
                                        Log.i("List", classifyEnityList.toString());
                                        //classifyAdapter.notifyDataSetChanged();
                                        //在Adapter里刷新list数据
                                        // classifyAdapter.setClassifyEnityList(classifyEnityList);
                                        classifyAdapter.addEnity(classifyEnity);
                                        //最后刷新Adapter列表
                                        classifyAdapter.addItem(classifyAdapter.refreshPosition);

                                        //    将输入的用户名和密码打印出来
                                        setToast("添加数据成功");
                                        dialog.dismiss();
                                    }
                                });
                                negativeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        dialog.show();
                        break;
                }
                return true;
            }
        });

    }


    /**
     * 处理主体RecyclerView列表适配器
     * item的子控件的监听事件
     */
    public void recyclerViewEvent() {
        mLayoutManager = new LinearLayoutManager(this);
        classifyRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        classifyRecyclerView.setHasFixedSize(true);
        //为RecyclerView添加默认动画效果
        classifyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 加载数据
        classifyEnityList = classifyDaoHelper.queryClassifyByPage(page);
        classifyAdapter = new ClassifyAdapter(classifyEnityList, ClassifyManagerActivity.this);

        //加载数据到适配器
        classifyRecyclerView.setAdapter(classifyAdapter);
        // 为RecyclerView添加滚动监听事件
        classifyRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("下拉加载更多...................");
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == classifyAdapter.getItemCount() && !isLoading) {
                    //到达底部之后如果footView的状态不是正在加载的状态,就将 他切换成正在加载的状态
                    //求出总数据量
                    Long classifyCount = classifyDaoHelper.classifyCount();
                    //算出总页数
                    int totalPage = new Double(Math.ceil(classifyCount * 1.0 / 5)).intValue();
                    System.out.println("当前页=====>"+page+",===总页数====>"+totalPage);
                    //判断当前页是否小于总页数
                    if (page < totalPage - 1) {
                        Log.e("linford", "onScrollStateChanged: " + "进来了");
                        isLoading = true;
                        classifyAdapter.changeState(0001);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //累加分页数据
                                classifyEnityList.addAll(classifyDaoHelper.queryClassifyByPage(++page));
                                isLoading = false;
                                System.out.println(classifyEnityList.toString() + "------------->>>");
                                //在Adapter里刷新list数据
                                //  classifyAdapter.setClassifyEnityList(classifyEnityList);
                                classifyAdapter.setEnityList(classifyEnityList);
                                classifyAdapter.notifyDataSetChanged();
                               // page++;
                            }
                        }, 2000);
                    } else {
                        //显示底部没有数据footView
                        classifyAdapter.changeState(0000);

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一个出现的item的位置
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
//        classifyEnityList= classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(1)).list();
//
//        final RV_ClassifyAdapter adapter=new RV_ClassifyAdapter(classifyEnityList,this);
//        //创建装饰者实例，并传入被装饰者和回调接口
//        mAdapter = new LoadMoreAdapterWrapper(adapter, new LoadMoreAdapterWrapper.OnLoad() {
//            @Override
//            public void load(int pagePosition, int pageSize, final LoadMoreAdapterWrapper.ILoadCallback callback) {
//                //此处模拟做网络操作，2s延迟，将拉取的数据更新到adpter中
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //数据的处理最终还是交给被装饰的adapter来处理
//                        adapter.appendData( //加载数据
//                                classifyEnityList);
//                        callback.onSuccess();
//                        //模拟加载到没有更多数据的情况，触发onFailure
//                        if (loadCount++ == 3) {
//                            callback.onFailure();
//                        }
//                    }
//                }, 2000);
//            }
//        });
//        classifyRecyclerView.setAdapter(mAdapter);
//        adapter.setSubItemOnClickListener(new RV_ClassifyAdapter.SubItemOnClickListener() {
//            //更新一个分类数据
//
//            @Override
//            public void updateClick(View v, final int refreshPosition, int queryPosition) {
//                //greenDao数据库id自增是从1开始,但item是从0开始的,所以tag需要+1,已在ClassifyAdapter回调方法里添加了
//                //查询出Classify_delFlag为0的所有数据
//                List<ClassifyEnity> list = classifyDao.queryBuilder().where(ClassifyEnityDao.Properties.Classify_delFlag.eq(0)).list();
//                //查询出Classify_delFlag为1的所有数据,且position为当前positioin+为0的list.size()
//                // (因为item的position始终从第一个显示的item算起的,没有包括在数据库没有显示的数量,造成了表id与position不统一的问题)
//                Query query = classifyDao.queryBuilder().where(
//                        ClassifyEnityDao.Properties.Classify_delFlag.eq(1), ClassifyEnityDao.Properties.Classify_id.eq(queryPosition)).build();
//                classifyEnityOfUpdate = (ClassifyEnity) query.unique();
//
//
//                //使用AlertDialog弹窗
//                AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
//                builder.setIcon(R.mipmap.ic_launcher);
//                builder.setTitle("请修改分类");
//                builder.setPositiveButton("确定", null);
//                builder.setNegativeButton("取消", null);
//                //    设置我们自己定义的布局文件作为弹出框的Content(必须在AlertDialog后面实例化)
//                dialogView = LayoutInflater.from(ClassifyManagerActivity.this).inflate(R.layout.dialog_classifyadd, null);
//                builder.setView(dialogView);
//
//                TextView classifyText = (TextView) dialogView.findViewById(R.id.classifyText);
//                TextView sortText = (TextView) dialogView.findViewById(R.id.sortText);
//                classifyText.setText("分类:");
//                sortText.setText("排序:");
//
//                final EditText classifyName = (EditText) dialogView.findViewById(R.id.classifyEdit);
//                final EditText sortName = (EditText) dialogView.findViewById(R.id.sortEdit);
//                //把查询到的数据显示在要修改的输入框(有选择的修改)
//                classifyName.setText(classifyEnityOfUpdate.getClassifyName());
//                //这里要把integer类型转换为String类型,否则会报异常
//                sortName.setText(String.valueOf(classifyEnityOfUpdate.getSort()));
//                //获取EditText里的内容
//                classifyNameOfDialogViewString = classifyName.getText().toString().trim();
//                sortNameOfDialogViewString = sortName.getText().toString().trim();
//
//                final AlertDialog dialog = builder.create();
//                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialogInterface) {
//
//
//                        Button positionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                        positionButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //点击确定之前先判断输入框内容是否为空,否则点击确定时会报Resources$NotFoundException异常
//                                Pattern pattern = Pattern.compile("[0-9]*");
//                                Matcher matcher = pattern.matcher(sortNameOfDialogViewString);
//                                if (classifyNameOfDialogViewString == null && sortNameOfDialogViewString == null) {
//                                    setToast("添加数据不能为空!");
//                                    return;
//                                } else if (!matcher.matches()) {//正则表达式验证输入框是否为数字
//                                    setToast("请输入正确的数字");                                            //不执行后面代码
//                                    return;
//                                }
//                                classifyEnityOfUpdate.setClassifyName(classifyNameOfDialogViewString);
//                                classifyEnityOfUpdate.setSort(Integer.valueOf(sortNameOfDialogViewString));
//                                System.out.println("2333333333333333--->" + classifyEnityOfUpdate);
//                                //更新数据
//                                classifyDao.update(classifyEnityOfUpdate);
//                                classifyAdapter.notifyItemChanged(refreshPosition);
//                                //更新listView列表
//                                classifyAdapter.notifyDataSetChanged();
//
//                                //  Toast提示添加数据成功
//                                Toast.makeText(ClassifyManagerActivity.this, "更新数据成功", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        });
//                        negativeButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.cancel();
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                });
//                dialog.show();
//
//
//            }
//
//            @Override
//            public void deleteClick(View v, final int refreshPosition, int queryPosition) {
//                //查询出Classify_delFlag为1的所有数据,且position为当前positioin+为0的list.size()
//                // (因为item的position始终从第一个显示的item算起的,没有包括在数据库没有显示的数量,造成了表id与position不统一的问题)
//                Query query = classifyDao.queryBuilder().where(
//                        ClassifyEnityDao.Properties.Classify_delFlag.eq(1), ClassifyEnityDao.Properties.Classify_id.eq(queryPosition)).build();
//                classifyEnityOfDelete = (ClassifyEnity) query.unique();
//
//                if (classifyEnityOfDelete != null) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
//                    //设置主题、内容、按钮
//                    builder.setTitle("你确定删除吗?").setNegativeButton("否", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            arg0.dismiss();
//                            //关闭对话框
//                        }
//                    });
//                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            classifyEnityOfDelete.setClassify_delFlag(0);
//                            classifyDao.update(classifyEnityOfDelete);
//                            classifyAdapter.removeItem(refreshPosition);
//                            Toast.makeText(ClassifyManagerActivity.this, "删除成功！",
//                                    Toast.LENGTH_SHORT).show();
//                                /*提醒测试作用*/
//                        }
//                    });
//                    builder.create().show();
//                }
//            }
//
//
//        });

        //item子控件的点击事件

        //item子控件的点击事件(更新和删除)
        //为item的子控件添加事件
        classifyAdapter.setSubItemOnClickListener(new ClassifyAdapter.SubItemOnClickListener() {
            //更新一个分类数据

            @Override
            public void updateClick(View v, final int refreshPosition, int queryPosition) {
                //查询出Classify_delFlag为1的所有数据,且position为当前positioin+为0的list.size()
                // (因为item的position始终从第一个显示的item算起的,没有包括在数据库没有显示的数量,造成了表id与position不统一的问题)
                //查询当前选中的id的Classify数据
                classifyEnityOfUpdate = classifyDaoHelper.queryClassify(queryPosition);

                //使用AlertDialog弹窗
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("请修改分类");
                builder.setPositiveButton("确定", null);
                builder.setNegativeButton("取消", null);
                // 设置我们自己定义的布局文件作为弹出框的Content(必须在AlertDialog后面实例化)
                dialogView = LayoutInflater.from(ClassifyManagerActivity.this).inflate(R.layout.dialog_classifyadd, null);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();

                TextView classifyText = (TextView) dialogView.findViewById(R.id.classifyText);
                TextView sortText = (TextView) dialogView.findViewById(R.id.sortText);
                classifyText.setText("分类:");
                sortText.setText("排序:");

                final EditText classifyName = (EditText) dialogView.findViewById(R.id.classifyEdit);
                final EditText sortName = (EditText) dialogView.findViewById(R.id.sortEdit);

                //把查询到的数据显示在要修改的输入框(有选择的修改)
                classifyName.setText(classifyEnityOfUpdate.getClassifyName());
                //这里要把integer类型转换为String类型,否则会报异常
                sortName.setText(String.valueOf(classifyEnityOfUpdate.getSort()));
                //为dialogView里的按钮添加点击事件
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        //定义"确定按钮"
                        Button positionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        //定义"取消按钮"
                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //获取EditText数据一定要放在点击事件内,否则无法获取改变后的数据
                                classifyNameOfDialogViewString = classifyName.getText().toString().trim();
                                sortNameOfDialogViewString = sortName.getText().toString().trim();
                                //点击确定之前先判断输入框内容是否为空,否则点击确定时会报Resources$NotFoundException异常
                                Pattern pattern = Pattern.compile("[0-9]*");
                                Matcher matcher = pattern.matcher(sortNameOfDialogViewString);
                                if (classifyNameOfDialogViewString.equals("") || sortNameOfDialogViewString.equals("")) {
                                    setToast("添加数据不能为空!");
                                    return;
                                } else if (!matcher.matches()) {//正则表达式验证输入框是否为数字
                                    setToast("请输入正确的数字");                                            //不执行后面代码
                                    return;
                                }
                                classifyEnityOfUpdate.setClassifyName(classifyNameOfDialogViewString);
                                classifyEnityOfUpdate.setSort(Integer.valueOf(sortNameOfDialogViewString));
                                System.out.println("2333333333333333--->" + classifyEnityOfUpdate);
                                //更新数据
                                classifyDaoHelper.updateClassify(classifyEnityOfUpdate);
                                //因为要实时更新列表数据,所以要再查询一遍数据set到Adapter里面刷新
                                classifyEnityList = classifyDaoHelper.queryClassifyByPage(offset);
                                Log.i("List", classifyEnityList.toString());
                                //classifyAdapter.notifyDataSetChanged();
                                //在Adapter里刷新list数据
                                classifyAdapter.setEnityList(classifyEnityList);
                                classifyAdapter.notifyItemChanged(refreshPosition);
                                //更新listView列表
                                classifyAdapter.notifyDataSetChanged();

                                //  Toast提示添加数据成功
                                Toast.makeText(ClassifyManagerActivity.this, "更新数据成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();


            }

            @Override
            public void deleteClick(View v, final int refreshPosition, int queryPosition) {
                //查询出Classify_delFlag为1的所有数据,且position为当前positioin+为0的list.size()
                // (因为item的position始终从第一个显示的item算起的,没有包括在数据库没有显示的数量,造成了表id与position不统一的问题)
                classifyEnityOfDelete = classifyDaoHelper.queryClassify(queryPosition);

                if (classifyEnityOfDelete != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClassifyManagerActivity.this);
                    //设置主题、内容、按钮
                    builder.setTitle("你确定删除吗?").setNegativeButton("否", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            //关闭对话框
                        }
                    });
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //更改数据
                            classifyEnityOfDelete.setClassify_delFlag(0);
                            classifyDaoHelper.updateClassify(classifyEnityOfDelete);
                            classifyAdapter.removeItem(refreshPosition);
                            Toast.makeText(ClassifyManagerActivity.this, "删除成功！",
                                    Toast.LENGTH_SHORT).show();
                                /*提醒测试作用*/
                        }
                    });
                    builder.create().show();
                }
            }


        });

    }


    /**
     * 实现SwipeRefreshLayout.OnRefreshListener接口里的方法
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                classifySwipeRefresh.setRefreshing(false);
                classifyEnityList.clear();
                //重新获取数据,刷新数据
                page=0;
                classifyEnityList = classifyDaoHelper.queryClassifyByPage(page);
                classifyAdapter = new ClassifyAdapter(classifyEnityList, ClassifyManagerActivity.this);
                //加载数据到适配器
                classifyRecyclerView.setAdapter(classifyAdapter);
                Toast.makeText(ClassifyManagerActivity.this, "已是最新数据了", Toast.LENGTH_LONG).show();
                classifyAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    /**
     * 自定义Toast内容弹窗
     */
    private void setToast(String toastText) {
        Toast.makeText(ClassifyManagerActivity.this, toastText, Toast.LENGTH_SHORT).show();

    }


}
