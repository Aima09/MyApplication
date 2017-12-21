package yf.com.a361ijkplayerdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;

import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity implements IMediaPlayer.OnInfoListener{

    private PlayerView player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         String url = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";
        //String url = "http://url.527578.com/youku.php/XMjUzNDIxNTk3Ng==.mp4";
        player = new PlayerView(this)
                .setTitle("什么")
                .setScaleType(PlayStateParams.fitxy)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(MainActivity.this)
                                //视频封面
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                //占位图
                                .placeholder(R.color.colorAccent)
                                //加载失败页面
                                .error(R.color.simple_player_stream_name_normal)
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(url)
                //设置自动重连的模式或者重连时间，isAuto true 出错重连，false出错不重连，connectTime重连的时间
                .setAutoReConnect( true, 5000)
                .startPlay();
        player.setOnInfoListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        //MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        //MediaUtils.muteAudioFocus(mContext, false);
        /**demo的内容，激活设备常亮状态*/
        //if (wakeLock != null) {
        //    wakeLock.acquire();
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /**demo的内容，恢复设备亮度状态*/
        //if (wakeLock != null) {
        //    wakeLock.release();
        //}
    }

    /**
     * 封装的视频播放信息返回码监听
     * @param iMediaPlayer
     * @param i
     * @param i1
     * @return
     */
    @Override public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }
}
