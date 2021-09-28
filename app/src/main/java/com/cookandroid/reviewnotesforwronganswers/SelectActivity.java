package com.cookandroid.reviewnotesforwronganswers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SelectActivity extends AppCompatActivity {
    private CustomDialog customdlg;
    private String Title; // 오답 제목
    private String Ppath, Apath, Mpath; // 문제/정답/메모 파일 경로
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Ppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/problem";
        Apath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/answer";
        Mpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/memo";

        TextView TVtitle = (TextView)findViewById(R.id.TVtitle);
        ImageView IVanswer = (ImageView)findViewById(R.id.IVanswer);
        Button btnback = (Button)findViewById(R.id.btnback);
        Button btnconfirm = (Button)findViewById(R.id.btnconfirm);
        Button btnMemo = (Button)findViewById(R.id.btnmemo);
        Intent intent = getIntent();
        Title = intent.getStringExtra("SelectItem"); // 선택한 오답 제목 받아오기
        customdlg = new CustomDialog(this);

        TVtitle.setText(Title); // 제목 설정


        getImage(Title); // 문제, 정답 이미지 불러오기

        //뒤로 버튼
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //정답 확인 버튼
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnconfirm.getText() != "정답 확인") {
                    IVanswer.setVisibility(View.INVISIBLE);
                    btnconfirm.setText("정답 확인");
                }else {
                    IVanswer.setVisibility(View.VISIBLE);
                    btnconfirm.setText("정답 숨김");
                }
            }
        });

        setFilename(); // 제목 저장

        //메모 버튼
        btnMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdlg.show();
            }
        });
    }

    // 제목 저장 함수
    public void setFilename() {
        //제목 넘겨주기
        File saveFile = new File(Mpath);
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/Title.txt")); // 버퍼 할당
            buf.append(Title); // 파일 쓰기
            buf.newLine();
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    // 문제/정답 이미지 가져오는 함수
    public void getImage(String name) {
        try {
            //캐시에 저장된 이미지 불러오기
            ImageView IVproblem = (ImageView) findViewById(R.id.IVproblem);
            ImageView IVanswer = (ImageView) findViewById(R.id.IVanswer);
            File Pfile = new File(Ppath);
            File Afile = new File(Apath);
            //문제 이미지
            Bitmap bm1 = BitmapFactory.decodeFile(Pfile+"/"+name);
            IVproblem.setImageBitmap(bm1);
            //정답 이미지
            Bitmap bm2 = BitmapFactory.decodeFile(Afile+"/"+name);
            IVanswer.setImageBitmap(bm2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}