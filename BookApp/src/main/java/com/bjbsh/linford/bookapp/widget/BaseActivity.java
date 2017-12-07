package com.bjbsh.linford.bookapp.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bjbsh.linford.bookapp.activity.MainActivity;
import com.bjbsh.linford.bookapp.activity.R;

import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by 艾特不出先生 on 8/16 0016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    //用于打印输出状态
    private static final String TAG = "BaseActivity";
    //封装所有Activity,用于批量操作
    private ArrayList<Activity> activityList = new ArrayList<>();
    private ToolbarHelper mToolbarHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把当前Activity放入Activity管理栈中
        ActivityStackManager.getActivityStackManager().pushActivity(this);

        //初始化ToolBar
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            mToolbarHelper = new ToolbarHelper(toolbar);

            hanldeToolbar(mToolbarHelper);
        }
    }
///////////////////////////////////定义基本使用的抽象方法/////////////////////////////////////////////

    /**
     * 初始化控件监听事件
     */
    public abstract void initViewListener();

    /**
     * 初始化数据源
     */
    public abstract void initDatas();

    /**
     * 注册广播服务
     */
    protected abstract void initBroadcastReceiver();

////////////////////////////////////封装BaseToolbar/////////////////////////////////////////////////

    /**
     * 用于子类重写该方法自定义TooBar的View
     *
     * @param toolbarHelper
     */
    protected void hanldeToolbar(ToolbarHelper toolbarHelper) {
    }

    /**
     * 封装一个BaseToolBar类
     */

    public class ToolbarHelper {

        private Toolbar mToolbar;
        private boolean showBackFlag;

        public ToolbarHelper(Toolbar toolbar) {
            this.mToolbar = toolbar;
        }

        public Toolbar getToolbar() {
            return mToolbar;
        }

        public void setTitle(String headTitle) {
            TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            if (title != null) {
                title.setText(headTitle);
                //关闭ToolBar默认标题
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        public void setSubTitle(String subTitle) {
            TextView subtitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
            subtitle.setText(subTitle);
        }

        /**
         * 是否显示后退按钮,默认显示,可在子类重写该方法.
         *
         * @return
         */
        protected boolean isShowBacking(Boolean flag) {
            return showBackFlag = flag;

        }

        /**
         * 版本号小于21的后退按钮图片
         */
        private void showBack() {
            //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
            getToolbar().setNavigationIcon(R.drawable.back);
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//添转到Intent
                    Activity currentActivity = ActivityStackManager.getActivityStackManager().currentActivity();
                    Intent intent = new Intent(currentActivity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            });

        }


    }


///////////////////////////////////////自定义公用方法////////////////////////////////////////////////

    /**
     * 自定义Toast内容弹窗
     */
    private void setToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

    }

    /**
     * 退出应用 * called while exit app.
     */
    public void exitLogic() {
        ActivityStackManager.getActivityStackManager().popAllActivity();
        //remove all activity.
        System.exit(0);//system exit.
    }

    /**
     * 跳转Activity * skip Another Activity * * @param activity * @param cls
     */
    public static void skipAnotherActivity(Activity activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }
    //---------------------------------------Android6.0以上版本动态权限管理-----------------------start-------->

    /**
     * 指定要申请的权限
     */
    public void grantPermission(String... permissions) {
        //判断是否有授权一些危险权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionGen.with(this)
                    .addRequestCode(100)
                    .permissions(permissions)
                    .request();
        }


    }

    /**
     * 处理用户权限的交互结果的回调方法
     * 同意授权{}
     * 拒绝授权{}
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    /**
     * When it succeeded in obtaining permission
     * 权限处理
     */
    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        Toast.makeText(this, "Contact permission is granted", Toast.LENGTH_SHORT).show();
    }

    /**
     * When it failed in obtaining permission
     */

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }
    //------------------------------------end----------------------------------------------------->
/////////////////////////////////////////BaseActivity的生命周期//////////////////////////////////////

    /**
     * 判断是否需要显示ToolBar的回退按钮
     */
    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != toolbar && mToolbarHelper.showBackFlag) {
            mToolbarHelper.showBack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
    }
}
