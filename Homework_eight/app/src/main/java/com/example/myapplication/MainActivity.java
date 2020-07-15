package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button button;
    private static final int VIDEO_CAPTURE_CODE=1;
    private String[] mPermissionsArrays = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView=findViewById(R.id.video);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissionAllGranted(mPermissionsArrays)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
                    } else {
                        // TODO
                        Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "已经获取所有所需权限", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!=null){
                        startActivityForResult(intent,VIDEO_CAPTURE_CODE);
                    }
                }
            }
        });
    }
    private final static int REQUEST_PERMISSION = 123;
    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            Toast.makeText(this, "已经授权" + Arrays.toString(permissions), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int RequestCode,int resultCode, Intent intent) {
        super.onActivityResult(RequestCode, resultCode, intent);
        if (RequestCode == VIDEO_CAPTURE_CODE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }
}