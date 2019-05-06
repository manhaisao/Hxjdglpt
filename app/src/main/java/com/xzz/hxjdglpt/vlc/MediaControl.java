package com.xzz.hxjdglpt.vlc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.xzz.hxjdglpt.utils.LogUtil;
import com.xzz.hxjdglpt.utils.ToastUtil;

import org.videolan.vlc.VlcVideoView;
import org.videolan.vlc.listener.MediaListenerEvent;
import org.videolan.vlc.listener.MediaPlayerControl;

/**
 * Created by yyl on 2016/11/3/003.
 */

public class MediaControl implements MediaController.MediaPlayerControl, MediaListenerEvent {
    private MediaPlayerControl mediaPlayer;
    final MediaController controller;
    private TextView logInfo;
    private LinearLayout mLoading;
    String tag = "MediaControl";

    private Context mContext;

    public MediaControl(Context mContext, VlcVideoView mediaPlayer, TextView logInfo) {
        this.mediaPlayer = mediaPlayer;
        this.logInfo = logInfo;
        this.mContext = mContext;
        controller = new MediaController(mediaPlayer.getContext());
        controller.setMediaPlayer(this);
        controller.setAnchorView(mediaPlayer);
//        mediaPlayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!controller.isShowing()) controller.show();
//                else controller.hide();
//                if (controller.isShowing()) controller.hide();
//            }
//        });
    }

    public MediaControl(Context mContext, VlcVideoView mediaPlayer, LinearLayout mLoading) {
        this.mediaPlayer = mediaPlayer;
        this.mLoading = mLoading;
        this.mContext = mContext;
        controller = new MediaController(mediaPlayer.getContext());
        controller.setMediaPlayer(this);
        controller.setAnchorView(mediaPlayer);
        controller.hide();
//        mediaPlayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!controller.isShowing()) controller.show();
//                else controller.hide();
        if (controller.isShowing()) controller.hide();
//            }
//        });
    }


    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return (int) mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return (int) mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mediaPlayer.getBufferPercentage();
    }

    @Override
    public boolean canPause() {
        return mediaPlayer.isPrepare();
    }

    @Override
    public boolean canSeekBackward() {//快退
        return true;
    }

    @Override
    public boolean canSeekForward() {//快进
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    long time;

    @Override
    public void eventBuffing(int event, float buffing) {

    }

    @Override
    public void eventPlayInit(boolean openClose) {
        if (openClose) {
            Log.i(tag, "打开时间  00000");
            time = System.currentTimeMillis();
        }
        if (logInfo != null) {
            logInfo.setText("加载中");
        } else if (mLoading != null) {
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void eventStop(boolean isPlayError) {
        if (logInfo != null) {
            logInfo.setText("Stop" + (isPlayError ? "  播放已停止   有错误" : ""));
        } else if (mLoading != null) {
            mLoading.setVisibility(View.VISIBLE);
            LogUtil.i("Stop" + (isPlayError ? "  播放已停止   有错误" : ""));
            ToastUtil.show(mContext, "Stop" + (isPlayError ? "  播放已停止   有错误" : ""));
        }
    }

    @Override
    public void eventError(int error, boolean show) {
        if (logInfo != null) {
            logInfo.setText("地址 出错了 error=" + error);
        } else if (mLoading != null) {
            mLoading.setVisibility(View.VISIBLE);
            ToastUtil.show(mContext, "地址 出错了 error=" + error);
        }
    }

    @Override
    public void eventPlay(boolean isPlaying) {
        if (isPlaying) {
//            controller.show();
            Log.i(tag, "打开时间是 time=" + (System.currentTimeMillis() - time));
            if (logInfo != null) {
                logInfo.setText("开始播放");
            } else if (mLoading != null) {
                mLoading.setVisibility(View.GONE);
            }
        }

    }

}
