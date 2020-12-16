package com.example.ocr;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;



public class MainActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //이미지 디코딩을 위한 초기화
        image = BitmapFactory.decodeResource(getResources(), R.drawable.sample3); //샘플이미지파일
        //언어파일 경로
        datapath += getFilesDir() + "/tesseract/";
        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "kor";

        mTess = new TessBaseAPI();
        Log.d("check",datapath);
        mTess.init(datapath, lang);
        Log.d("check",datapath);
    }

    //Process an Image
    public void processImage(View view) {
        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        //
        //String restr = OCRresult.replaceAll("[^0-9]","");
        String str = "";
        String[] array_word;
        String[] phone_number = new String[50];


        array_word = OCRresult.split("");
        int k = 0;


        /*for(int i=0;i<array_word.length;i++)
        {
            str = "";
            if(array_word[i].equals("0"))
            {
                int j = i;

                while(array_word[j].matches("[0-9]|[.)-]]"))
                {
                    str = str.concat(array_word[j]);
                    j++;
                }
                i = j;
                phone_number[k] = str;
                k++;
            }
        }//phone_number에 전화번호들 저장.*/

        //
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setMovementMethod(new ScrollingMovementMethod());
        //OCRTextView.setText(OCRresult);


    }


    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            Log.d("check", "카피 파일1");
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            Log.d("check", "카피 파일2");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
            Log.d("check","??" + dir);
            Log.d("check","exist 없데");

        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        else if(dir.exists()) {
            Log.d("check","??" + dir);
            Log.d("check","exist 있데");
            String datafilepath = datapath + "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            Log.d("check",datafilepath + "문제의 그곳");
            if(!datafile.exists()) {
                Log.d("check","카피1");
                copyFiles();
                Log.d("check","카피2");
            }
        }
    }
}