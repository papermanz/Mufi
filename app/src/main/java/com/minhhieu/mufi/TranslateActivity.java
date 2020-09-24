package com.minhhieu.mufi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import Data.Word;

public class TranslateActivity extends AppCompatActivity {
        TextView txtEng;
        TextView txtVie;

    private static final String KEY_WORD = "KEY_WORD";

    public static Intent newIntent(Context context, @NotNull Word item){
        Intent intent = new Intent(context, TranslateActivity.class);
        intent.putExtra(KEY_WORD, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        txtEng = findViewById(R.id.txtketqua);
        txtVie = findViewById(R.id.txtvie);
        Word item = (Word) getIntent().getSerializableExtra(KEY_WORD);
        txtEng.setText(item.getEng());
        txtVie.setText(item.getVie());

    }
}