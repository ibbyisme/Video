package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    Button start,stop,pause,returns,button2,button3;
    //ProgressBar progress,voice;
    SeekBar seekbar1;
    VideoView video;
    EditText text;
    String p;
    int timemax=0;
    MediaController contral;
    MediaPlayer mediaPlayer=new MediaPlayer();
    AudioManager audioManager=null;
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(video.isPlaying()) {
                int current = video.getCurrentPosition();
                seekbar1.setProgress(current);
            }
            handler.postDelayed(runnable,500);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LayoutInflater factory=LayoutInflater.from(Main2Activity.this);
        View activity=factory.inflate(R.layout.activity_main,null);
        text=(EditText)activity.findViewById(R.id.edittext);
        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(new ButtonListener());
        stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(new ButtonListener());
        pause=(Button)findViewById(R.id.pause);
        pause.setOnClickListener(new ButtonListener());
        returns=(Button)findViewById(R.id.returns);
        returns.setOnClickListener(new ButtonListener());
        seekbar1=(SeekBar) findViewById(R.id.seekbar1);
        seekbar1.setOnSeekBarChangeListener(new mSeekBarListener());

        audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new ButtonListener());
        button2.setOnClickListener(new ButtonListener());


        //seekbar2=(SeekBar) findViewById(R.id.seekbar2);
        video=(VideoView)findViewById(R.id.video);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(Main2Activity.this,"播放完成",Toast.LENGTH_LONG).show();
                int a=video.getCurrentPosition();
                //System.out.println(a+"----");
            }
        });
        contral=new MediaController(this);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET},0x123);
        contral.setMediaPlayer(video);
        String string=getIntent().getStringExtra("name");
        File file=new File(getExternalFilesDir("").toString()+string);
        //Uri uri= Uri.parse(Environment.getExternalStorageState()+string);
        p=Environment.getExternalStorageDirectory().getPath()+string;
        video.setVideoPath(file.getPath());
        video.setMediaController(contral);
        handler.postDelayed(runnable,0);
        video.start();
        try{
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            timemax=mediaPlayer.getDuration();
            System.out.println(timemax);
        }
        catch(Exception e){

        }
        seekbar1.setMax(timemax);
        video.setOnErrorListener(new MediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        video.requestFocus();
    }


    public class mSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress=seekbar1.getProgress();
            System.out.println(progress);
            if(video.isPlaying()){
                video.seekTo(progress);
            }
        }
    };
    public class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            switch(v.getId())
            {
                case R.id.start:
                    video.start();
                    //handler.postDelayed(runnable,0);
                    break;
                case R.id.pause:
                    video.pause();
                    break;
                case R.id.stop:
                    video.stopPlayback();
                    break;
                case R.id.returns:
                    Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button2:
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                    break;
                case R.id.button3:
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                    break;
            }
        }
    }
}
