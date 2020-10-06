package com.minhhieu.mufi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import Data.Database;
import Data.Exercise;
import Data.ExerciseAdapter;
import Data.Word;
import Data.WordAdapter;

public class LearnActivity extends AppCompatActivity {

    ListView lvhienthi;
    ListView lvexercise;
    public static Database database;
    ArrayList<Word> arrayWord;
    ArrayList<Exercise> arrayImage;
    WordAdapter adapter;
    ExerciseAdapter eadapter;
    TextView tvaddimage;
    TextView addWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initViewPager();

        arrayWord = new ArrayList<>();
        adapter = new WordAdapter(this, R.layout.word_eng, arrayWord);
        lvhienthi.setAdapter(adapter);


        eadapter = new ExerciseAdapter(this, R.layout.image_title,arrayImage);
        lvexercise.setAdapter(eadapter);


        //tạo database Word
        database = new Database(this, "Learn.sqlite", null, 1);

        //tạo bảng công việc translate
        database.QueryData("CREATE TABLE IF NOT EXISTS Word(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Eng VARCHAR(200), Vie VARCHAR(200))");

       //tạo bảng công việc excercise
     database.QueryData("CREATE TABLE IF NOT EXISTS Image(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
           " Title VARCHAR(200), ImageTitle BLOB, Gif VARCHAR(200))");

        GetDataWord();
        GetDataImage();
        lvhienthi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word item = adapter.getItem(position);
                startActivity(TranslateActivity.newIntent(LearnActivity.this, item));
            }
        });

        lvexercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise itemExercise = (Exercise) eadapter.getItem(position);
                startActivity(ExerciseActivity.newIntentExercise(LearnActivity.this,itemExercise));

            }
        });

        tvaddimage = (TextView) findViewById(R.id.addimage);
        tvaddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this,ImageActivity.class);
                startActivity(intent);
            }
        });
        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAdd();
            }
        });
    }

    //Dialog xoá từ mới
    public void DialogDeleteWord(final String tencv, final int Id){
        AlertDialog.Builder dialogdelete = new AlertDialog.Builder(this);
        dialogdelete.setMessage("Bạn có muốn xoá từ "+ tencv+ " không ?");
        dialogdelete.setPositiveButton("có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM Word WHERE ID = '"+ Id +"'");
                Toast.makeText(LearnActivity.this, "Đã xoá từ "+ tencv +" thành công !", Toast.LENGTH_SHORT).show();
                GetDataWord();
            }
        });
        dialogdelete.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogdelete.show();
    }

    //Get random item sqlite in listview
    public void GetRandom(){
        Cursor dataWords = database.GetData("SELECT * FROM Word WHERE Id IN (SELECT Id FROM Word ORDER BY RANDOM() LIMIT 3)");
        arrayWord.clear();
        while (dataWords.moveToNext()){
            String vie = dataWords.getString(2);
            String eng = dataWords.getString(1);
            int id = dataWords.getInt(0);
            arrayWord.add(new Word(id,eng,vie));

        }
        adapter.notifyDataSetChanged();
    }

    private void GetDataWord(){
        //select Data( đọc dữ liệu)
        Cursor dataWords = database.GetData("SElECT * FROM Word");
        arrayWord.clear();
        while (dataWords.moveToNext()){
            String vie = dataWords.getString(2);
            String eng = dataWords.getString(1);
            int id = dataWords.getInt(0);
            arrayWord.add(new Word(id,eng,vie));

        }
        adapter.notifyDataSetChanged();
    }


    //select Data( đọc dữ liệu hình ảnh)
    private void GetDataImage(){
        Cursor dataImage = database.GetData("SELECT * FROM Image");
        arrayImage.clear();
        while (dataImage.moveToNext()){
            String Gif = dataImage.getString(3);
            byte[] imageTitle = dataImage.getBlob(2);
            String title = dataImage.getString(1);
            int idImage = dataImage.getInt(0);
            arrayImage.add(new Exercise(idImage,title,imageTitle,Gif));
        }
        eadapter.notifyDataSetChanged();
    }


   private void initViewPager (){
       lvhienthi = findViewById(R.id.lvhienthi);
       lvexercise = findViewById(R.id.lvexercise);
       arrayImage = new ArrayList<>();
       addWord = (TextView) findViewById(R.id.addword);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_word,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            GetRandom();
            lvhienthi.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }



    //Dialog add từ mới
    private void DialogAdd(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_word);
        final EditText edt_eng = dialog.findViewById(R.id.edt_eng);
        final EditText edt_vie = dialog.findViewById(R.id.edt_vie);
        Button btn_add_word = dialog.findViewById(R.id.btn_add);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);


        btn_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word_eng = edt_eng.getText().toString();
                String word_vie = edt_vie.getText().toString();
                if(word_eng.equals("")) {
                    Toast.makeText(LearnActivity.this, "Vui lòng nhập từ mới cần thêm !", Toast.LENGTH_SHORT).show();
                }else if(word_vie.equals("")) {
                    Toast.makeText(LearnActivity.this, "Vui lòng nhập nghĩa của từ mới cần thêm !", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.QueryData("INSERT INTO Word VALUES (null, '" + word_eng + "','" + word_vie + "')");
                    Toast.makeText(LearnActivity.this, "Đã Thêm từ mới vào từ điển", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataWord();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    //Dialog xoá bài tập Homeout

    public void DialogDeleteExercise(final int idImage, final String title, byte[] imageTitle) {
        AlertDialog.Builder dialogdeleteExercise = new AlertDialog.Builder(this);
        dialogdeleteExercise.setMessage("Bạn có muốn xoá bài tập "+ title + " không ?");
        dialogdeleteExercise.setPositiveButton("có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM Image WHERE ID = '"+idImage +"'");
                Toast.makeText(LearnActivity.this, "Đã xoá bài tập "+ title +" thành công !", Toast.LENGTH_SHORT).show();
                GetDataImage();
            }
        });
        dialogdeleteExercise.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogdeleteExercise.show();
    }

}