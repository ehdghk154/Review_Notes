package com.cookandroid.reviewnotesforwronganswers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cookandroid.reviewnotesforwronganswers.support.PermissionSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PermissionSupport permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionCheck();
    }
    // 권한 체크
    private void permissionCheck(){
        // SDK 23버전 이하 버전 권한 필요x
        if(Build.VERSION.SDK_INT >= 23){
            // 방금 전 만들었던 클래스 객체 생성
            permission = new PermissionSupport(this, this);
            // 권한 체크한 후에 리턴이 false로 들어온다면
            if (!permission.checkPermission()){
                // 권한 요청
                permission.requestPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용을 거부하였다면)
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            // 다시 Permission 요청
            //permission.requestPermission();
            Toast.makeText(getApplicationContext(), "권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //오답 추가 버튼 클릭 시 발생
    public void AddClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
    //오답 목록 버튼 클릭 시 발생
    public void ListClick(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
}