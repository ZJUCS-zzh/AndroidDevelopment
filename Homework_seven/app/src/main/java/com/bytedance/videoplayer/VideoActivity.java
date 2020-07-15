package com.bytedance.videoplayer;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.content.Context;
import android.widget.TextView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import com.bytedance.videoplayer.player.VideoPlayerIJK;
import com.bytedance.videoplayer.player.VideoPlayerListener;

public class VideoActivity extends Activity{
    private VideoPlayerIJK ijkPlayer;
    private boolean menu_visible=true;
    private boolean isPortrait=true;
    private boolean isRecycle=false;
    public boolean finish=false;
    public static final int refreshCode = 941;
    int VideoHeight=0;
    int VideoWidth=0;
    private Handler handler;
    private RelativeLayout bottom;
    private RelativeLayout player;
    private RelativeLayout loading_layout;
    private AudioManager audioManager;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private SeekBar Volume;
    private SeekBar process;
    private class VolumeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                int currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Volume.setProgress(currentVolume);
            }
        }
    }
    VolumeReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.layout_video);
        setTitle("Player");
        ijkPlayer = findViewById(R.id.ijkPlayer);
        //初始化控件
        prepareViews();
        //初始化播放器
        IJKPlayer();
        receiver=new VolumeReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.registerReceiver(receiver,filter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    private void prepareViews(){
        bottom=findViewById(R.id.bottom_setting);
        player=findViewById(R.id.player);
        loading_layout =findViewById(R.id.loading_layout);

        button1=findViewById(R.id.play);
        button2=findViewById(R.id.recycle);
        button3=findViewById(R.id.volume);
        button4=findViewById(R.id.setting);

        Volume=findViewById(R.id.set_volume);
        process=findViewById(R.id.seekBar);
        //播放和暂停
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button1.getText().toString().equals("播放")) {
                    button1.setText("暂停");
                    ijkPlayer.start();
                } else {
                    button1.setText("播放");
                     ijkPlayer.pause();
                }
            }
        });
        //循环

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button2.getText().toString().equals("循环")) {
                    button2.setText("单次");
                    isRecycle = false;
                } else {
                    button2.setText("循环");
                    isRecycle = true;
                }
            }
        });
        //音量


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                Volume.setMax(max);
                int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Volume.setProgress(current);
                Volume.setVisibility(View.VISIBLE);
            }
        });
        //横竖屏
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPortrait) {
                    horizontal();

                } else {
                    vertical();
                }
            }
        });
        //点击播放器
        ijkPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_visible == false) {
                    bottom.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_bottom);
                    bottom.startAnimation(animation);
                    menu_visible = true;
                } else {
                    bottom.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bottom);
                    bottom.startAnimation(animation);
                    menu_visible = false;
                }
            }
        });

        //刷新
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case refreshCode:
                        if (ijkPlayer.isPlaying()) {
                            refresh();
                            handler.sendEmptyMessageDelayed(refreshCode, 50);
                        }
                        break;
                }
            }
        };

        //进度条
        process.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ijkPlayer.seekTo(ijkPlayer.getDuration() * seekBar.getProgress() / 100);
                handler.sendEmptyMessageDelayed(refreshCode, 100);
            }
        });

        //音量条
        Volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Volume.setProgress(current);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Volume.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void refresh(){
        long current=ijkPlayer.getCurrentPosition()/1000;
        long duration=ijkPlayer.getDuration()/1000;
        long second=current%60;
        long minute=current/60;
        long Allsecond=duration%60;
        long Allminute=duration/60;
        String time=minute+":"+second+"/"+Allminute+":"+Allsecond;
        TextView timeText=findViewById(R.id.time);
        timeText.setText(time);
        if(duration!=0){
            process.setProgress((int)(current*100/duration));
        }
    }
    private void IJKPlayer() {
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        //多整几个
        ijkPlayer.setVideoPath(getVideoPath());
        ijkPlayer.setListener(new VideoPlayerListener());

        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                process.setProgress(100);
                if (!isRecycle) {
                    button1.setText("播放");
                } else {
                    ijkPlayer.start();
                }

            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                refresh();
                handler.sendEmptyMessageDelayed(refreshCode, 50);
                finish = false;
                VideoWidth = mp.getVideoWidth();
                VideoHeight = mp.getVideoHeight();
                if (isPortrait) vertical();
                else horizontal();
                mp.start();
                loading_layout.setVisibility(View.GONE);
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                VideoWidth = mp.getVideoWidth();
                VideoHeight = mp.getVideoHeight();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(refreshCode, 1000);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (ijkPlayer != null && ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        IjkMediaPlayer.native_profileEnd();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        if (ijkPlayer != null) {
            ijkPlayer.stop();
            ijkPlayer.release();
            ijkPlayer = null;
        }

        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private String getVideoPath() {
        //return "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
        //return "http://vjs.zencdn.net/v/oceans.mp4";
        return "http://ugcyd.qq.com/uwMROfz2r5zIIaQXGdGnC2dfDmb_xYKxrIGz_bGUg2Lja6ru/o0839k9l0l7.mp4?sdtfrom=v1000&type=mp4&vkey=CD863DEEFC4468E6131E338EB3F761108635D6ECD9F16F2E63DBB0FE291DD72103B15B7955C7565F3742A211CF2219E78159E63203C9C7A09BB60AADCC314FB151E029B98ACB06C478DD81D454435313FAEF30C2A31AED63D1A69BA1930141085E9281ED831C8FDC22AB4BD00274C06DB5880766C3100260F84E83A4F2385AA1&platform=11&br=20&fmt=hd&sp=0&charge=0&vip=0&guid=91BD7FB5FBDFD6E81E3B19A77E668D9B";
        //return "https://haokan.baidu.com/v?vid=1488265211050859181&pd=bjh&fr=bjhauthor&type=video";
        //return "android.resource://" + this.getPackageName() + "/" + R.raw.bytedance;
        //return "android.resource://" + this.getPackageName() + "/" + R.raw.hourse;
    }

    private void horizontal(){
        ijkPlayer.pause();
        isPortrait=false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager windowManager=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)player.getLayoutParams();
        layoutParams.height=(int) RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width=(int)RelativeLayout.LayoutParams.MATCH_PARENT;
        player.setLayoutParams(layoutParams);
        button4.setText("竖屏");
        ijkPlayer.start();
    }
    private void vertical(){
        ijkPlayer.pause();
        isPortrait=true;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WindowManager windowManager=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        float width=windowManager.getDefaultDisplay().getWidth();
        float height=windowManager.getDefaultDisplay().getHeight();
        float ratio=width/height;
        if(ratio<1)ratio=(float)1.0/ratio;
        RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)player.getLayoutParams();
        layoutParams.height=(int)(VideoHeight*ratio);
        layoutParams.width=(int)width;
        player.setLayoutParams(layoutParams);
        button4.setText("横屏");
        ijkPlayer.start();
    }

}
