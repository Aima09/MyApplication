package yf.com.gsyvideoplayerijkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

   // @BindView(R.id.video_player) NormalGSYVideoPlayer mVideoPlayer;
    @BindView(R.id.video_player) StandardGSYVideoPlayer mVideoPlayer;
    private IjkMediaPlayer mIjkMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mVideoPlayer.findViewById(R.id.video_player);
        String url = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";
        mIjkMediaPlayer = new IjkMediaPlayer();
        mVideoPlayer.setUp(url, true, "hello World");
        mVideoPlayer.startWindowFullscreen(MainActivity.this, true, true);
        //  mVideoPlayer.showSmallVideo(new Point(200,200),true,true);
       // mVideoPlayer.startPlayLogic();
       /* try {
            mIjkMediaPlayer.setDataSource(url);
            mIjkMediaPlayer._prepareAsync();
            mIjkMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}
