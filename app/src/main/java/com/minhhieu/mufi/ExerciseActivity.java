package com.minhhieu.mufi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import Data.Database;
import Data.Exercise;

public class ExerciseActivity extends AppCompatActivity{
    private static final String TAG="ExerciseActivity";


    ImageView gif;
    TextView TitleExercise;
    TextView IdRecommended;
    EditText IdEntry;
    LineChart LineChart;
    Button SaveExercise;
    Database database;
    long date = System.currentTimeMillis();
    SharedPreferences saveDecommended;
    int TotalDecommended = 10;






    private static final String KEY_EXERCISE = "KEY_EXERCISE";

    @NotNull
    public static Intent newIntentExercise(Context context, Exercise itemExercise) {

        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra(KEY_EXERCISE, itemExercise);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initView();
        Exercise itemExercise = (Exercise) getIntent().getSerializableExtra(KEY_EXERCISE);
        TitleExercise.setText(itemExercise.getTitle());

        //Glide resize gif
        Glide.with(this)
                .load(itemExercise.getGif())
                .into(gif);

        database = new Database(this, "Learn.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS Time(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " yAXIS STRING, DATE STRING)");

        //Create Preferences
        saveDecommended = getSharedPreferences("Decommended", MODE_PRIVATE);
        addDataToGraph();
        LoadChart();

        //get Decommended
        TotalDecommended = saveDecommended.getInt("Decommended",10);
        IdRecommended.setText(TotalDecommended+"");



    }


    private void LoadChart(){

        SaveExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SaveToDatabase();
                Toast.makeText(ExerciseActivity.this, "Chúc mừng bạn đã hoàn thành bài tập xuất sắc !", Toast.LENGTH_SHORT).show();
                IdEntry.setText("");
            }
        });
    }

    public void SaveToDatabase(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String xvalue = sdf.format(date);
        String yvalue = IdEntry.getText().toString().trim();
        database.SaveExercise(xvalue,yvalue);
        addDataToGraph();
        int Entry = Integer.parseInt(IdEntry.getText().toString());
        if(Entry>TotalDecommended){
            TotalDecommended = Entry + 5;
        }
        IdRecommended.setText(TotalDecommended+"");
        SavePreferences();
        database.close();

    }

    public void addDataToGraph(){
        final ArrayList<Entry> yVals = new ArrayList<Entry>();
        final ArrayList<String> ydata = database.QueryYData();

        for(int i = 0; i < database.QueryYData().size(); i++){
            Entry newEntry = new Entry(i, Float.parseFloat(database.QueryYData().get(i)));
            yVals.add(newEntry);
        }
        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xdata = database.QueryXData();
        for(int i = 0; i < database.QueryXData().size(); i++){
            xVals.add(xdata.get(i));
        }
        LineDataSet lineDataSet = new LineDataSet(yVals,"REPS");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        LineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        LineChart.setData(lineData);

        XAxis xAxis = LineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        xAxis.setGranularity(1f);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(2f);
        LineChart.setMaxVisibleValueCount(5);
        LineChart.invalidate();


    }

    private void initView() {
        gif = (ImageView) findViewById(R.id.imagegif);
        TitleExercise = (TextView) findViewById(R.id.title_exercise);
        LineChart = (com.github.mikephil.charting.charts.LineChart) findViewById(R.id.linechart);
        IdRecommended = (TextView) findViewById(R.id.id_recommended);
        IdEntry = (EditText) findViewById(R.id.id_entry);
        SaveExercise = (Button) findViewById(R.id.save_exercise);
    }
    private void SavePreferences(){
        SharedPreferences.Editor editor = saveDecommended.edit();
        editor.putInt("Decommended",TotalDecommended);
        editor.commit();
    }


}