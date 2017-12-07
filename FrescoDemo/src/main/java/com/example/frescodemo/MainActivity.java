package com.example.frescodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView mSimpleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        mSimpleView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501359296468&di=7c7990e06a88c64a1e6ec6212250aa4d&imgtype=0&src=http%3A%2F%2Fimg2.niutuku.com%2F1312%2F0800%2F0800-niutuku.com-14339.jpg";
        mSimpleView.setImageURI(uri);


    }
}
