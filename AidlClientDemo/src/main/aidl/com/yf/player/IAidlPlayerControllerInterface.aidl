// IAidlPlayerControllerInterface.aidl
package com.yf.player;

// Declare any non-default types here with import statements
import com.yf.player.IAidlPlayerControllerCallBack;
//客户端主动
interface IAidlPlayerControllerInterface {
    //回调事宜
    void registerListener(IAidlPlayerControllerCallBack callBack);
    void unRegisterListener(IAidlPlayerControllerCallBack callBack);
    void doInBackGround();//服务端调此方法测试使用，客户端不要管

    void play();//播放
    void pause();//暂停
    void playPause();//播放暂停
    void stop();//停止
//    void volumeDown();//音量- 直接操作音量就好了
//    void volumeUp();//音量+
    void previous();//上一曲
    void next();//下一曲
    void seekTo(int curProgress);//设置进度
    int getCurrentPosition();//获取进度
    //获取状态
    //1 正在播放
    //2 暂停
    //3 停止
    int getPlayStatus();
    //是否在播放
    boolean isPlaying();
    //获取当前歌曲信息
    String getCurrentMedia();
    //设置mode
    void setMode(int mode);
    //设置播放源(音源)
    void setSource(int source);
    //这里开始结束录音阿里录音
    void startStopRec();

//       void
       //alink操作接口
      // void setMusicInfo(in MusicInfo musicInfo);//这个接口要做切换为阿里音源
//       void setProgress(int progress);//设置进度
//       void setMax(int maxProgress);//设置最大进度
}
