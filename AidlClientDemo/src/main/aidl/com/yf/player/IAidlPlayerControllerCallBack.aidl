// IAidlPlayerControllerCallBack.aidl
package com.yf.player;

// Declare any non-default types here with import statements

interface IAidlPlayerControllerCallBack {
     //回调回显
     //1 正在播放
     //2 暂停
     //3 停止
     void updateMediaPlayState(int state);
    //当前位置
     void updateMediaPlayPosition(int position);
    //回调歌曲信息 注意阿里的数据在Media.getMusicInfo中
     void updateMedia(String media);
    //回调歌曲总长度(只有Alink用到这个接口)
     void updateMediaPlayDuration(int duration);
    /** public static final int ALL_PLAY_MODE = 5;           // 循环播放所有媒体模式
        public static final int SINGLE_PLAY_MODE = 6;        // 单曲播放模式
        public static final int RANDOM_PLAY_MODE = 7;        // 随机播放模式
        阿里alink只支持后两者
     **/
     void updateMediaMode(int mode);
    /* //本地音源
     public static final int L；、。OCAL_PLAY_SOURCE = 1;
     //阿里音源
     public static final int ALINK_PLAY_SOURCE = 2;
     //远程音源
     public static final int REMOTE_PLAY_SOURCE = 3;*/
     void updateMediaSource(int source);

    //歌词回调，能拿到歌词----------------------start---------------------------------
     void updateLoadLyricFail(String msg);

     void updateLoadLyric(String lyricString);
     //歌词回调，能拿到歌词----------------stop---------------------------------
}
