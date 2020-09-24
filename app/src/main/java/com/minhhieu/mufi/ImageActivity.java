package com.minhhieu.mufi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class ImageActivity extends AppCompatActivity {

        EditText txttitle;
        ImageView img1;
        EditText txtGif;
        Button btadd;
        Button btload;


        int Request_code =123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        txttitle = (EditText) findViewById(R.id.textView);
        img1 = (ImageView) findViewById(R.id.imageView);
        txtGif = (EditText) findViewById(R.id.txtgif);
        btadd = (Button) findViewById(R.id.add);
        btload = (Button) findViewById(R.id.brower);


        btload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Request_code);
            }
        });


        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển imageview sang byte
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] ImageTitle = byteArrayOutputStream.toByteArray();

                LearnActivity.database.Insert_image(
                        txttitle.getText().toString().trim(),
                        ImageTitle,
                        txtGif.getText().toString().trim()
                );
                Toast.makeText(ImageActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ImageActivity.this,LearnActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==Request_code && resultCode == RESULT_OK&&data !=null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img1.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}