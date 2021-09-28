package com.cookandroid.reviewnotesforwronganswers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CustomDialog extends Dialog {

    private Context context;
    private String Title;
    private String Mpath;
    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Mpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/memo";
        Title = ReadTitle(Mpath+"/Title.txt");
        //파일 생성
        File saveFile = new File(Mpath); // 저장 경로
        //폴더 생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        //제목 가져오기
        //readTitle(saveFile);

        //메모 가져오기
        read(saveFile);

        Button okbtn = (Button)findViewById(R.id.okbtn);
        Button cancelbtn = (Button)findViewById(R.id.cancelbtn);

        //확인 버튼 이벤트
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(saveFile);
                dismiss();
            }
        });

        //취소 버튼 이벤트
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    //메모 저장
    public void save(File path) {
        EditText ETmemo = (EditText)findViewById(R.id.ETmemo);
        //파일 쓰기
        try {
            FileOutputStream os = new FileOutputStream(path+"/"+Title);
            BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(os));
            buf.append(ETmemo.getText().toString()); // 파일 쓰기
            buf.newLine();
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(File path) {
        String line = null; // 한줄씩 읽기
        EditText ETmemo = (EditText)findViewById(R.id.ETmemo);

        //파일 읽기
        try {
            FileInputStream is = new FileInputStream(path+"/"+Title);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            while((line=buf.readLine())!=null){
                ETmemo.append(line);
                ETmemo.append("\n");
            }
            //다이얼로그 실행마다 추가되는 엔터 제거
            String temp = ETmemo.getText().toString();
            ETmemo.setText(temp.substring(0, temp.length()-1));
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 제목 가져오기
    public String ReadTitle(String path){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";
            while((line=reader.readLine())!=null){
                strBuffer.append(line);
            }
            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
        return strBuffer.toString();
    }
}
