package com.czdxwx.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.czdxwx.test.dialog.FileSelectFragment;
import com.czdxwx.test.utils.FileUtils;
import com.czdxwx.test.utils.UriUtils;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.exoplayer.DemoPlayer;
import com.google.android.exoplayer.exoplayer.PlayerUtils;

import java.io.File;
import java.util.Map;

public class MyVideoActivity extends Activity implements SurfaceHolder.Callback, DemoPlayer
        .Listener, View.OnClickListener, FileSelectFragment.FileSelectCallbacks {

    private AspectRatioFrameLayout videoFrame;//用来控制视频的宽高比
    private SurfaceView surfaceView; //播放区
    private RelativeLayout video_skin;

    //视频控制的各个按钮
    private RelativeLayout video_control;
    private ImageButton py;
    private ImageButton fs;
    private SeekBar seekBar;

    private DemoPlayer player;
    private Uri contentUri; //视频的uri
    private int contentType;//流媒体传输协议类型
    private int Duration;//视频的大小
    private int video_width;
    private int video_heigth;

    private long playerPosition;
    private boolean playerNeedsPrepare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exo_player);
        //常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();

    }


    @Override
    public void onResume() {
        super.onResume();
        onShown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initView() {
        videoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);
        video_skin = (RelativeLayout) findViewById(R.id.video_skin);

        //视频控制按钮
        video_control = (RelativeLayout) findViewById(R.id.video_control);
        video_control.setVisibility(View.GONE);
        py = (ImageButton) findViewById(R.id.py);
        py.setOnClickListener(this);
        fs = (ImageButton) findViewById(R.id.fs);
        fs.setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.sk);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//放开
                player.seekTo(seekBar.getProgress());
            }
        });

        video_skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voidControlsVisibility();
            }
        });


    }

    //释放player
    private void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    //获取视频数据
    @SuppressLint("SdCardPath")
    private void onShown() {
        String[] videoExs = new String[]{"mp4", "3gp", "mkv", "mov", "avi",};
        FileSelectFragment.show(this,videoExs,null);
    }



    //控制按钮的显示
    private void voidControlsVisibility() {
        int vs = video_control.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
        video_control.setVisibility(vs);
    }


    private void preparePlayer(boolean playWhenReady) {
        if (player == null) {
            player = new DemoPlayer(PlayerUtils.getRendererBuilder(this, contentType, contentUri));
            player.addListener(this);
            player.seekTo(playerPosition);//播放进度的设置
            playerNeedsPrepare = true; //是否立即播放
        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }

    //surfaceView的监听
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (player != null) {
            player.setSurface(surfaceHolder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:

                break;
            case ExoPlayer.STATE_ENDED:

                break;
            case ExoPlayer.STATE_IDLE://空的

                break;
            case ExoPlayer.STATE_PREPARING:

                surfaceView.setVisibility(View.VISIBLE);

                break;
            case ExoPlayer.STATE_READY:

                boolean first = true;
                if (first) {
                    //记录视频的宽高
                    video_width = videoFrame.getWidth();
                    video_heigth = videoFrame.getHeight();
                    //skin的宽高
                    if (video_width != 0 && video_heigth != 0) {
                        PlayerUtils.scaleLayout(this, video_skin, video_width, video_heigth);
                    }
                    //进度条的时间设置
                    Duration = (int) player.getDuration();
                    Log.e("TAG", "Duration--" + Duration);
                    seekBar.setMax(Duration);
                    videoTime seek = new videoTime();
                    seek.start();
                    first = false;
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onError(Exception e) {

        playerNeedsPrepare = true;
    }

    //pixelWidthHeightRatio 显示器的宽高比
    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float
            pixelWidthHeightRatio) {
        videoFrame.setAspectRatio(height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
    }

    //横竖屏切换
    //    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            //隐藏状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            PlayerUtils.scaleLayout(this, video_skin, 0, 0);

        } else {//竖屏
            //显示状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //skin的宽高
            if (video_width != 0 && video_heigth != 0) {
                PlayerUtils.scaleLayout(this, video_skin, video_width, video_heigth);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.py://播放键
                if (player.getPlayWhenReady()) {
                    py.setBackground(getResources().getDrawable(R.drawable.play));
                    player.setPlayWhenReady(false);
                } else {
                    py.setBackground(getResources().getDrawable(R.drawable.pause));
                    player.setPlayWhenReady(true);
                }

                break;
            case R.id.fs://全屏键

                if (this.getResources().getConfiguration().orientation == Configuration
                        .ORIENTATION_LANDSCAPE) {//横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    fs.setBackground(getResources().getDrawable(R.drawable.full));
                } else if (this.getResources().getConfiguration().orientation == Configuration
                        .ORIENTATION_PORTRAIT) {//竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    fs.setBackground(getResources().getDrawable(R.drawable.lessen));
                }
                break;
        }
    }

    //更新进度条
    private final int SEEKBAR = 111;
    private Handler seekbarHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEEKBAR:
                    seekBar.setProgress(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onConfirmSelect(String absolutePath, String fileName, Map<String, Object> map_param) {
        //拼接
        String file_path=absolutePath+"/"+fileName;
        File file = new File(file_path);
        contentUri = Uri.parse(file.toURI().toString());
        contentType = PlayerUtils.inferContentType(contentUri);
        Log.e("TAG", "contentUri" + contentUri + "contentType" + contentType);
        if (player == null) {
            preparePlayer(true);
        } else {
            player.setBackgrounded(false);
        }
    }

    @Override
    public boolean isFileValid(String absolutePath, String fileName, Map<String, Object> map_param) {
        return true;
    }

    class videoTime extends Thread {
        public void run() {
            while (player != null && player.getCurrentPosition() <= Duration) {
                if (player.getPlayWhenReady()) {
                    Message message = new Message();
                    message.what = SEEKBAR;
                    message.arg1 = (int) player.getCurrentPosition();
                    seekbarHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private static Uri getVideoContentUri(Context context, File videoFile) {
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/video/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (videoFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
