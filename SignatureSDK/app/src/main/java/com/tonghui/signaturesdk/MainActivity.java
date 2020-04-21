package com.tonghui.signaturesdk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.tonghui.signaturelib.BitmapUtil;
import com.tonghui.signaturelib.PaintView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    PaintView paintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = findViewById(R.id.paintView_id);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sava:
                saveJPG();
                break;

            case R.id.btn_clear:
                paintView.reset();
                break;

            case R.id.btn_set_style:
                paintView.setPaintWidth(10);
                paintView.setPaintColor(Color.BLUE);
                break;
            case R.id.btn_set_style2:
                paintView.setPaintWidth(20);
                paintView.setPaintColor(Color.RED);
                break;
            case R.id.btn_set_style3:
                paintView.setPaintWidth(30);
                paintView.setPaintColor(Color.GREEN);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        paintView.release();
    }

    private void saveJPG(){
        /**
         * paintView  画板
         * quality 图片质量 0-100
         * name：图片名字,自己根据业务添加
         * 返回值：在外部存储中   /storage/emulated/0/Android/data/com.tonghui.signaturedemo/cache/signImage/123.jpg
         */
        String filePth = Environment.getExternalStorageDirectory()+ File.separator+"signImage";
        String path = BitmapUtil.saveImage(this, paintView, 100, filePth,"123");
        Log.e("tag", "saveJPG: 图片路径" +path);
//        String s = BitmapUtil.saveImage(this, paintView, 100, "123");
//        Log.e("tag", "saveJPG: 图片路径" +s);
    }
}
