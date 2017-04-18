package com.example.yls.ad0418;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.aip.face.AipFace;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button btn_detect;
    private FaceView img;
    private AipFace client;
    private Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDetect();
       // detect();
        initHandler();

    }

    private void initHandler() {
        myHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Rect rect= (Rect) msg.obj;
                img.drawFace(rect);
                return true;
            }
        });
    }

    private void initViews() {
        img= (FaceView) findViewById(R.id.iv_picture);
        btn_detect= (Button) findViewById(R.id.btn_detect);
        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect();
            }
        });
    }

    private void initDetect() {
        // 初始化一个FaceClient
         client = new AipFace("9533086", "scy0t2WavPAhjhfFtgQNU0fs", "6sSiGP0z0xOR5et4KzYlB5WBkQO7t2Bk");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    private void detect() {
        Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.ziyi1);
        final byte[] imgByte=Bitmap2Bytes(bmp);
        final HashMap<String ,String > paraMap=new HashMap<String,String>();
        paraMap.put("face_fields","age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject res=client.detect(imgByte,paraMap);
                Log.e("MainActivity",res.toString());
//                Rect r=new Rect((int)(117/1.5f),(int)(127/1.5f),(117+207),(127+194));
                Rect r=new Rect(117,127,(117+207),(127+194));
                Message msg= Message.obtain();
                msg.obj=r;
                myHandler.sendMessage(msg);
            }
        }).start();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
