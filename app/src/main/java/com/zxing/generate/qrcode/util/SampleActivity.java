package com.zxing.generate.qrcode.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class SampleActivity extends Activity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mImageView = (ImageView) findViewById(R.id.image_view);
        new MyHandler(this).sendEmptyMessage(0);
    }

    final static class MyHandler extends Handler {
        SampleActivity activity;

        public MyHandler(SampleActivity sampleActivity) {
            WeakReference<SampleActivity>
                    activities = new WeakReference<SampleActivity>(sampleActivity);
            activity = activities.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != activity) {
                final String content = "http://www.baidu.com";
                final int qrWidth = 500;
                final int qrHeight = 500;
                final Bitmap qrImage = new
                        GenerateQrCodeUtil().createQRImage(content, qrWidth, qrHeight);
                activity.mImageView.setImageBitmap(qrImage);
            }
        }
    }
}
