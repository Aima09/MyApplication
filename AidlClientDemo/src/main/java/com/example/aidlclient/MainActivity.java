package com.example.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yf.player.IAidlPlayerControllerInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 跨进程访问Player进程的ALnk服务
 * 测试
 */
public class MainActivity extends AppCompatActivity {
    //日志
    public static final String TAG = "MainActivity";
    // 服务端 AndroidManifest.xml中的intent-filter action声明的字符串
    public static final String ACTION = "com.yf.player.controller.service.PlayControllerService";
    //定义远程服务
    private IAidlPlayerControllerInterface alinkService;
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.stop)
    Button stop;

    //是否绑定
    private boolean isBinded = false;
    //是否播放
    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //绑定服务
        doBind();
    }

    /**
     * 绑定服务处理----------------------------------------------------start------------------------
     */
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
            alinkService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            alinkService = IAidlPlayerControllerInterface.Stub.asInterface(service);
            isBinded = true;
        }
    };

    /**
     * 绑定服务
     */
    public void doBind() {
        Intent intent = new Intent(ACTION);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑服务
     */
    public void doUnBind() {
        if (isBinded) {
            unbindService(conn);
            alinkService = null;
            isBinded = false;
        }
    }
//----------------------------------------服务绑定结束-----------------------------------------------

    /**
     * 按钮点击时间,操作远程服务
     *
     * @param view
     */
    @OnClick({R.id.play, R.id.previous, R.id.next, R.id.stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play:
                if(isPlay){
                    try {
                        alinkService.pause();
                        isPlay=true;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        alinkService.play();
                        isPlay=false;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.previous:
                try {
                    alinkService.previous();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.next:
                try {
                    alinkService.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
                try {
                    alinkService.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
