package com.minhhieu.mufi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import Data.Word;

public class TranslateActivity extends AppCompatActivity {
        TextView txtketqua;
        TextView txtVie;

    private static final String KEY_WORD = "KEY_WORD";

    public static Intent newIntent(Context context, Word item){
        Intent intent = new Intent(context, TranslateActivity.class);
        intent.putExtra(KEY_WORD, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        txtketqua = (TextView) findViewById(R.id.txtketqua);
        txtVie = (TextView) findViewById(R.id.txtvie);
        Word item = (Word) getIntent().getSerializableExtra(KEY_WORD);
        txtketqua.setText(item.getEng());
        txtVie.setText(item.getVie());



    }
}