package com.cookandroid.reviewnotesforwronganswers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
// 오답 추가 액티비티
public class AddActivity extends AppCompatActivity {
    private final int GET_PROBLEM_IMAGE = 200;
    private final int GET_ANSWER_IMAGE = 201;
    private final int CAMERA_REQUEST_CODE = 202;
    ImageView IVanswer, IVproblem; // 문제/정답 이미지뷰
    String imgName; // 노트 제목
    Bitmap imgBitmap1, imgBitmap2; // 이미지 저장을 위한 비트맵
    int check1=0, check2=0, check3=0; // 갤러리or사진촬영, 문제or정답 선택 체크
    Uri providerURI; // 촬영 사진 불러오기 위한 URI
    String Ppath, Apath, TPpath, Mpath; // 문제/정답/임시 파일 경로
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button btnproblem = (Button)findViewById(R.id.btnproblem);
        Button btnanswer = (Button)findViewById(R.id.btnanswer);
        Button btnsave = (Button)findViewById(R.id.btnsave);
        IVproblem = (ImageView)findViewById(R.id.IVproblem);
        IVanswer = (ImageView)findViewById(R.id.IVanswer);

        Ppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/problem";
        File file = new File(Ppath);
        if (!file.exists()) file.mkdirs(); // 경로 폴더 없으면 생성
        Apath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReviewNote/Image/answer";
        file = new File(Apath);
        if (!file.exists()) file.mkdirs();
        TPpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReviewNote/Image/temp";
        file = new File(TPpath);
        if (!file.exists()) file.mkdirs();
        Mpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ReviewNote/memo";
        file = new File(Mpath);
        if (!file.exists()) file.mkdirs();


        // 문제 촬영 버튼 클릭
        btnproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check3 = 1;
                makeDialog(check3);
            }
        });

        // 정답 촬영 버튼 클릭
        btnanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check3 = 2;
                makeDialog(check3);
            }
        });

        // 저장 버튼 클릭
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBitmapToJpeg(imgBitmap1, imgBitmap2);
            }
        });
    }

    //Intent 결과 값 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 갤러리에서 문제 사진 가져오기
        if (requestCode == GET_PROBLEM_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri ImageUri = data.getData();
            ContentResolver resolver = getContentResolver();
            try {
                // 이미지뷰에 이미지 출력
                InputStream instream = resolver.openInputStream(ImageUri);
                imgBitmap1 = BitmapFactory.decodeStream(instream);
                IVproblem.setImageBitmap(imgBitmap1);    // 선택한 이미지 이미지뷰에 셋
                instream.close();   // 스트림 닫아주기
                check1 = 1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "사진 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }

        // 갤러리에서 정답 사진 가져오기
        if (requestCode == GET_ANSWER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri ImageUri = data.getData();
            ContentResolver resolver = getContentResolver();
            try {
                InputStream instream = resolver.openInputStream(ImageUri);
                imgBitmap2 = BitmapFactory.decodeStream(instream);
                IVanswer.setImageBitmap(imgBitmap2);    // 선택한 이미지 이미지뷰에 셋
                instream.close();   // 스트림 닫아주기
                check2 = 1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "사진 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }

        // 문제 촬영 이미지 가져오기
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && check3 == 1) {
            Uri ImageUri = providerURI;
            ContentResolver resolver = getContentResolver();
            try {
                File tmp = new File(TPpath, "temp.jpg");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(tmp));
                Bitmap rotatedBitmap = rotateBTMP(bitmap);
                imgBitmap1 = rotatedBitmap;
                IVproblem.setImageBitmap(imgBitmap1);
                check1 = 1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "사진 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }

        // 정답 촬영 이미지 가져오기
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && check3 == 2) {
            try {
                File tmp = new File(TPpath, "temp.jpg");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(tmp));
                Bitmap rotatedBitmap = rotateBTMP(bitmap);
                imgBitmap2 = rotatedBitmap;
                IVanswer.setImageBitmap(imgBitmap2);
                check2 = 1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "사진 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 촬영한 사진 회전 결과물 출력
    public Bitmap rotateBTMP(Bitmap bitmap) {
        Bitmap rotatedBitmap = null;
        if (bitmap != null) {
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(TPpath + "/temp.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        }
        return rotatedBitmap;
    }

    // 사진 회전 함수
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    // 저장된 문제 리스트 가져오기
    public boolean getList(String name) {
        try {
            File file = new File(Ppath);
            if (!file.exists()) file.mkdirs();
            File[] flist = file.listFiles();
            if(flist.length <= 0)
                return true;
            for(int i = 0; i < flist.length; i++) {
                if(flist[i].getName().equals(name)) {
                    return false;
                }else return true;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void saveBitmapToJpeg(Bitmap bitmap1, Bitmap bitmap2) { // 선택한 이미지 내부 저장소에 저장
        EditText ETtitle = (EditText)findViewById(R.id.ETtitle);
        imgName = ETtitle.getText().toString();

        if(!imgName.equals("")) {
            if(getList(imgName)) {
                if (check1 == 1 && check2 == 1) {
                    File tempFile1 = new File(Ppath, imgName); // 파일 경로와 이름 넣기
                    File tempFile2 = new File(Apath, imgName);
                    try {
                        tempFile1.createNewFile(); // 자동으로 빈 파일을 생성하기
                        FileOutputStream out1 = new FileOutputStream(tempFile1); // 파일을 쓸 수 있는 스트림을 준비하기
                        tempFile2.createNewFile();
                        FileOutputStream out2 = new FileOutputStream(tempFile2);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out1); // compress 함수를 사용해 스트림에 비트맵을 저장
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out2); // compress 함수를 사용해 스트림에 비트맵을 저장
                        out1.close(); // 스트림 닫아주기
                        out2.close();
                        check1 = 0;
                        check2 = 0;
                        IVproblem.setImageBitmap(null);
                        IVanswer.setImageBitmap(null);
                        ETtitle.setText("");
                        Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "이미지를 가져와주세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "같은 제목의 문제가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    //문제 or 정답 사진 출력(갤러리에서)
    public void selectAlbum(int i) {
        if(i == 1) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_PROBLEM_IMAGE);
        }else if(i == 2) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_ANSWER_IMAGE);
        }
    }

    //문제/정답 버튼 터치 시 다오는 다이얼로그 커스텀
    private void makeDialog(int i){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(AddActivity.this);
        alt_bld.setTitle("사진 업로드").setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        // 사진 촬영 클릭
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File tpfile = new File(TPpath, "temp.jpg");
                        providerURI = FileProvider.getUriForFile( getApplicationContext() , getPackageName()+".fileprovider" , tpfile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        //앨범에서 선택
                        selectAlbum(i);
                    }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

}