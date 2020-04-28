package com.example.video;

//import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaCodecInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.net.URI;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    //Main2Activity main2;
    Button button;
    ListView listview;
    EditText edittext;
    TextView textview;
    //SimpleCursorAdapter adapter;
    VideoView video;
    MediaController contral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

        //main2=new Main2Activity();
        edittext = (EditText) findViewById(R.id.edittext);
        listview = (ListView) findViewById(R.id.list);
        textview = (TextView) findViewById(R.id.textView);
        //video=(VideoView)activity2.findViewById(R.id.video);
        contral=new MediaController(this);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET},0x123);
        contral.setMediaPlayer(video);

       //  /sdcard/Android/data/v1.mp4
/*-----------------------------------------------------------------------------------------*/
        ContentResolver resolver=getContentResolver();
        Uri uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //Uri u=Uri.parse(Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()));
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse(Environment.getExternalStorageDirectory().getPath())));
            System.out.println("刷新2");
        }
        String[] string=new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA};
        Cursor c = resolver.query(uri,string,
                null, null, null);
        if (c ==null||c.getCount()==0 ) {
            System.out.println("存储列表为空");
        }
        else{
            SimpleCursorAdapter adapter=new SimpleCursorAdapter(MainActivity.this,R.layout.list,c,string,new int[]{R.id.id,R.id.name,R.id.data});
            listview.setAdapter(adapter);
        }

        /*------------------------------------------------------------*////mnt/sdcard/Android/data/com.example.video/files/v1.mp4///storage/emulated/0/Android/data/com.example.video/files
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String a=null;
               File file=new File(getExternalFilesDir("").toString()+edittext.getText().toString());
               String b=getExternalFilesDir("").toString();
                    Toast toast2=Toast.makeText(MainActivity.this,"你点击了按钮",Toast.LENGTH_LONG);
                    toast2.show();

                if(file.exists())
                {
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("name",edittext.getText().toString());
                    //startActivity(intent1);
                    startActivity(intent);
                }
                else
                {
                    System.out.println("文件不存在");
                }
                }
        });
    }
}
