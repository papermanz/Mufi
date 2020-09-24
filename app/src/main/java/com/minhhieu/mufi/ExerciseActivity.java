package com.minhhieu.mufi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import Data.Exercise;

public class ExerciseActivity extends AppCompatActivity {

    ImageView gif;




    private static final String KEY_WORD1 = "KEY_WORD1";

    @NotNull
    public static Intent newIntent1(Context context, Exercise item1) {

        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra(KEY_WORD1, item1);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initView();
        Exercise item1 = (Exercise) getIntent().getSerializableExtra(KEY_WORD1);

        //Glide resize gif
        Glide.with(this)
                .load(item1.getGif())
                .into(gif);


    }
    private void initView() {

        gif = (ImageView) findViewById(R.id.imagegif);




    }


}