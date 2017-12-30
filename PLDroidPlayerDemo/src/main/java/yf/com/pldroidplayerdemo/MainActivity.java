package yf.com.pldroidplayerdemo;

import android.content.Context;
import android.media.session.MediaController;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMediaController, PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnSeekCompleteListener {

    public static final String TAG = "MainActivity";
    //播放参数
    public final static int MEDIA_CODEC_SW_DECODE = 0;//硬解
    public final static int MEDIA_CODEC_HW_DECODE = 1;//软解
    public final static int MEDIA_CODEC_AUTO = 2;//先硬解,失败后再软解

    public final static int PREFER_FORMAT_M3U8 = 1;
    public final static int PREFER_FORMAT_MP4 = 2;
    public final static int PREFER_FORMAT_FLV = 3;

    private MediaController mMediaController;

    //参数配置类
    private  AVOptions options ;
    @BindView(R.id.PLVideoView) PLVideoView mPLVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewListener();
        initVideoPlayer();
    }

    /**
     * 初始化界面和事件监听
     */
    private void initViewListener() {
        options = new AVOptions();
        setAvOption();

       /* //播放控制
        int isLiveStreaming = getIntent().getIntExtra("liveStreaming", 1);
        if (isLiveStreaming == 1) {
            options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }
        mMediaController = new MediaController(this, false, isLiveStreaming == 1);*/
        mPLVideoView.setMediaController(this);

        mPLVideoView.setOnPreparedListener(this);
        mPLVideoView.setOnInfoListener(this);
        mPLVideoView.setOnCompletionListener(this);
        mPLVideoView.setOnVideoSizeChangedListener(this);
        mPLVideoView.setOnErrorListener(this);
        mPLVideoView.setOnBufferingUpdateListener(this);
        mPLVideoView.setOnSeekCompleteListener(this);
    }


    /**
     * 初始化播放参数
     */
    private void setAvOption() {

        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        //options.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        // 默认值是：无
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 默认的缓存大小，单位是 ms
        // 默认值是：500
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 500);
        // 最大的缓存大小，单位是 ms
        // 默认值是：2000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);


        // 设置 DRM 密钥
        // byte[] key = {xxx, xxx, xxx, xxx, xxx ……};
        //options.setByteArray(AVOptions.KEY_DRM_KEY, key);

        // 设置偏好的视频格式，设置后会加快对应格式视频流的加载速度，但播放其他格式会出错
        options.setInteger(AVOptions.KEY_PREFER_FORMAT, PREFER_FORMAT_M3U8);


        // 开启解码后的视频数据回调
        // 默认值是：0
        options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);
        // 开启解码后的音频数据回调
        // 默认值是：0
        options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);


        // 在开启 ##5.12 音视频数据回调 的基础上，再开启如下配置，即可关闭 SDK 内部的视频渲染和音频播放
        // 开启自定义视频数据渲染
        // 默认值是：0，由 SDK 内部渲染视频
        options.setInteger(AVOptions.KEY_VIDEO_RENDER_EXTERNAL, 1);
        // 开启自定义音频数据播放
        // 默认值是：0，由 SDK 内部播放音频
        options.setInteger(AVOptions.KEY_VIDEO_RENDER_EXTERNAL, 1);


        // 请在开始播放之前配置
        mPLVideoView.setAVOptions(options);
    }

    /**
     * 初始化播放控制
     */
    private void initVideoPlayer() {
//        View loadingView = findViewById(R.id.LoadingView);
//        mPLVideoView.setBufferingIndicator(loadingView);


        //关联播放控制器
        yf.com.pldroidplayerdemo.MediaController mediaController=new yf.com.pldroidplayerdemo.MediaController(this);
        mPLVideoView.setMediaController(mediaController);
        // 提供了各种画面预览模式，包括：原始尺寸、适应屏幕、全屏铺满、16:9、4:3 等
        //适应屏幕
        mPLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);

        //还支持画面旋转，支持播放画面以 0度，90度，180度，270度旋转，设置方法如下：
        // mPLVideoView.setDisplayAspectRatio(90); // 旋转90度

        //还支持画面的镜像变换，设置方法如下：
        //mPLVideoView.setMirror(true);

        //设置播放地址
        String videoPath = "http://url.527578.com/youku.php/XMjUzNDIxNTk3Ng==.mp4";
        mPLVideoView.setVideoPath(videoPath);

        //视频封面
        //mPLVideoView.setCoverView(View coverView);
        //播放
        mPLVideoView.start();
        //控制播放器音量(音量参数的取值范围是：0.0～1.0，使用如下代码可以达到静音的效果：setVolume(0.0f, 0.0f);)
        // mPLVideoView.setVolume();

        //暂停
        // mPLVideoView.pause();

        //停止播放
        //  mPLVideoView.stopPlayback();

        //获取当前播放状态
        //mPLVideoView.getPlayerState();

        // 设置音视频数据回调的监听对象
        mPLVideoView.setOnVideoFrameListener(new PLMediaPlayer.OnVideoFrameListener() {
            /**
             * 回调一帧视频帧数据
             * @param data   视频帧数据
             * @param size   数据大小
             * @param width  视频帧的宽
             * @param height 视频帧的高
             * @param format 视频帧的格式，0代表 YUV420P，1 代表 JPEG， 2 代表 SEI
             * @param ts     时间戳，单位是毫秒
             */
            @Override
            public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
                Log.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", i420, " + ts);
            }
        });

        mPLVideoView.setOnAudioFrameListener(new PLMediaPlayer.OnAudioFrameListener() {
            /**
             * 回调一帧音频帧数据
             * @param data   音频帧数据
             * @param size   数据大小
             * @param samplerate 采样率
             * @param channels 通道数
             * @param datawidth 位宽，目前默认转换为了16bit位宽
             * @param ts     时间戳，单位是毫秒
             */
            @Override
            public void onAudioFrameAvailable(byte[] data, int size, int samplerate, int channels, int datawidth, long ts) {
                Log.i("FragmentActivity", "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);
            }
        });
    }

    //////////////////////////////////控制回调///////////////////////////////////////////////////////////
    @Override public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
        mediaPlayerControl.start();
    }

    @Override public void show() {

    }

    @Override public void show(int i) {

    }

    @Override public void hide() {

    }

    @Override public boolean isShowing() {
        return false;
    }

    @Override public void setEnabled(boolean b) {

    }

    @Override public void setAnchorView(View view) {

    }
    /////////////////////////////////控制回调结束///////////////////////////////////////////////////

    /**
     * 该对象用于监听播放结束的消息，关于该回调的时机，有如下定义：
     * 如果是播放文件，则是播放到文件结束后产生回调
     * 如果是在线视频，则会在读取到码流的EOF信息后产生回调，回调前会先播放完已缓冲的数据
     * 如果播放过程中产生onError，并且没有处理的话，最后也会回调本接口
     * 如果播放前设置了 setLooping(true)，则播放结束后会自动重新开始，不会回调本接口
     * <p>
     * 5.2.4 OnErrorListener
     *
     * @param plMediaPlayer
     */
    @Override public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }

    /**
     * 错误回调
     * MEDIA_ERROR_UNKNOWN	-1	未知错误
     * ERROR_CODE_OPEN_FAILED	-2	播放器打开失败
     * ERROR_CODE_IO_ERROR	-3	网络异常
     * ERROR_CODE_SEEK_FAILED	-4	拖动失败
     * ERROR_CODE_HW_DECODE_FAILURE	-2003	硬解失败
     * <p>
     *
     * @param plMediaPlayer
     * @param i
     * @return
     */
    @Override public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    /**
     * 该对象用于监听播放器的状态消息，在播放器启动后，
     * SDK 会在播放器发生状态变化时调用该对象的 onInfo 方法，同步状态信息。
     *
     * @param plMediaPlayer
     * @param what   定义了消息类型
     * @param extra   是附加参数
     * @return
     */
    @Override public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        return false;
    }

    /**
     * 该对象用于监听播放器的 prepare 过程，该过程主要包括：创建资源、建立连接、请求码流等等，
     * 当 prepare 完成后，SDK 会回调该对象的 onPrepared 接口，下一步则可以调用播放器的
     * start() 启动播放。
     *
     * @param plMediaPlayer
     * @param i
     */
    @Override public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {

    }

    /**
     * 该回调用于监听当前播放的视频流的尺寸信息，在 SDK 解析出视频的尺寸信息后，
     * 会触发该回调，开发者可以在该回调中调整 UI 的视图尺寸。
     *
     * @param plMediaPlayer
     * @param width
     * @param height
     */
    @Override public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
        System.out.println("width:" + width + "---heightL:" + height);

    }

    /**
     * 该回调用于监听当前播放器已经缓冲的数据量占整个视频时长的百分比，
     * 在播放直播流中无效，仅在播放文件和回放时才有效。
     *
     * @param plMediaPlayer
     * @param i
     */
    @Override public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {

    }

    /**
     * 该回调用于监听 seek 完成的消息，当调用的播放器的 seekTo 方法后，
     * SDK 会在 seek 成功后触发该回调。
     *
     * @param plMediaPlayer
     */
    @Override public void onSeekComplete(PLMediaPlayer plMediaPlayer) {

    }

    /**
     * 对于直播应用而言，播放器本身是无法判断直播是否结束，这需要通过业务服务器来告知。
     * 当主播端停止推流后，播放器会因为读取不到新的数据而产生超时，从而触发 ERROR_CODE_IO_ERROR 回调。
     * <p>
     * 建议的处理方式是：在 ERROR_CODE_IO_ERROR 回调后，查询业务服务器，
     * 获知直播是否结束，如果已经结束，则关闭播放器，清理资源；如果直播没有结束，则等待 SDK 内部自动做重连。
     * <p>
     * 建议收到 ERROR_CODE_IO_ERROR 之后，使用如下方法判断一下网络的联通性，如果用户的网络已断，则可以退出播放。
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
