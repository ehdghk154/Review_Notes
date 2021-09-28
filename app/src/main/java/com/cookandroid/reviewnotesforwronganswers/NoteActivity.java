package com.cookandroid.reviewnotesforwronganswers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private final int SELECTED_ITEM = 120;
    String[] imgName; // 리스트에 들어갈 이름
    int count; // 리스트 개수
    int checked = -1; // 아이템 선택 확인용
    int t; // 아이템 인덱스 변수
    String check_name = ""; // 선택한 아이템 이름
    final ArrayList<String> items = new ArrayList<String>(); // 리스트 아이템
    ArrayAdapter adapter;

    String Ppath, Apath, Mpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Ppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/problem";
        Apath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/answer";
        Mpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/memo";
        getList();

        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        adapter = new ArrayAdapter(this, R.layout.single_choice_list, items);

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.LVlist) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checked = listview.getCheckedItemPosition();
                check_name = items.get(i);
                t = i;
            }
        });
        for(int i = 1; i < count; i++) {

            if(imgName[count-i] != null) {
                // 아이템 추가.
                items.add(imgName[count-i]);
                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        }

    }

    //같은 이름의 파일이 있는지 확인하는 함수
    public void getList() {
        int j = 0;
        try {// 내부저장소 캐시 경로를 받아오기
            File file = new File(Ppath);
            File[] flist = file.listFiles();
            imgName = new String[flist.length];
            count = flist.length;
            for(int i = 0; i < count; i++) {
                    imgName[j] = flist[i].getName();
                    j++;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
        }
    }

    //이미지 삭제 버튼
    public void bt2(View view) {
        if(checked == t) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("\n오답 노트 삭제").setMessage("정말로 삭제하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        ListView lv = (ListView)findViewById(R.id.LVlist);
                        File pfile = new File(Ppath);  // 저장소 경로를 받아오기
                        File afile = new File(Apath);
                        File mfile = new File(Mpath);
                        File[] pflist = pfile.listFiles(); // 경로의 파일들 받아오기
                        File[] aflist = afile.listFiles();
                        File[] mflist = mfile.listFiles();
                        for (int i = 0; i < pflist.length; i++) {    // 배열의 크기만큼 반복
                            if (pflist[i].getName().equals(check_name)) { // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                                pflist[i].delete();  // 문제 파일 삭제
                            }
                            if (aflist[i].getName().equals(check_name)) {
                                aflist[i].delete();  // 정답 파일 삭제
                            }
                        }
                        for(int i = 0; i < mflist.length; i++){
                            if (mflist[i].getName().equals(check_name)) {
                                mflist[i].delete();  // 메모 파일 삭제
                            }
                        }
                        items.remove(checked);
                        lv.setItemChecked(checked, false);
                        Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                        checked = -1;
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {}
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        if(checked == -1) {
            Toast.makeText(getApplicationContext(), "삭제할 파일을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }

    }
    //오답 목록 선택 버튼
    public void bt1(View view) {
        ListView lv = (ListView)findViewById(R.id.LVlist);
        //오답 선택 여부
        if(lv.getCheckedItemPosition() == -1) {
            Toast.makeText(getApplicationContext(), "확인할 오답을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, SelectActivity.class);
            intent.putExtra("SelectItem", check_name);
            startActivity(intent);
        }

    }
}