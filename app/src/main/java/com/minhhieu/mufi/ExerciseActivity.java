package com.minhhieu.mufi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

   // SQLiteDatabase sqLiteDatabase;
   // LineDataSet lineDataSet = new LineDataSet(null, null);
    //ArrayList<ILineDataSet>dataSets = new ArrayList<>();
    //LineData lineData;
    long date = System.currentTimeMillis();







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

        addDataToGraph();
        LoadChart();




      //  SaveTime();
//        sqLiteDatabase = database.getWritableDatabase();


       // ShowChart();




//        LineChart.setScaleEnabled(false);
//        LineDataSet set1 = new LineDataSet(DataValues1(),"Số lần tập");
//        set1.setFillAlpha(110);
//        set1.setColor(Color.RED);
//        set1.setLineWidth(2f);
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//
//        XAxis xAxis = LineChart.getXAxis();
//        YAxis yAxisLeft = LineChart.getAxisLeft();
//        YAxis yAxisRight = LineChart.getAxisRight();
//        xAxis.setValueFormatter(new DayValueFormatter());
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(true);
//        xAxis.setLabelCount(5);
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//
//
//
//        dataSets.add(set1);
//
//
//        LineData data = new LineData(dataSets);
//        LineChart.setData(data);
//        LineChart.invalidate();
//
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

//
//    private void ShowChart() {
//        LoadExercise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lineDataSet.setValues(getDataValues());
//                lineDataSet.setLabel("số lần tập");
//                dataSets.clear();
//                dataSets.add(lineDataSet);
//                lineData = new LineData(dataSets);
//                LineChart.clear();
//                LineChart.setData(lineData);
//                LineChart.invalidate();
//            }
//        });
//
//    }
//
//    private ArrayList<Entry>getDataValues(){
//        ArrayList<Entry> dataVals = new ArrayList<>();
//        String[] columns = {"xValues","yValues"};
//        Cursor cursor = sqLiteDatabase.query("Time",columns,null,null,null,null,null);
//        for(int i = 0;i<cursor.getCount();i++)
//        {
//            cursor.moveToNext();
//            dataVals.add(new Entry(cursor.getFloat(0),cursor.getFloat(1)));
//
//        }
//        return dataVals;
//
//    }


    
//    private void SaveTime() {
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        final String dateTime = simpleDateFormat.format(calendar.getTime());
//
//        SaveExercise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    String xVal = dateTime;
//                    float yVal = Float.parseFloat(String.valueOf(IdEntry.getText()));
//                database.QueryData("INSERT INTO Time VALUES (null, '" + yVal + "','" + xVal + "')");
//            }
//        });
//    }


//    private ArrayList<Entry> DataValues1(){
//        ArrayList<Entry> yValues = new ArrayList<>();
//        yValues.add(new Entry(1,60f));
//        yValues.add(new Entry(2,50f));
//        yValues.add(new Entry(3,70f));
//        yValues.add(new Entry(4,65f));
//        yValues.add(new Entry(5,30f));
//        return yValues;
//    }




//    public class DayValueFormatter extends ValueFormatter{
//        public DayValueFormatter(){
//        }
//        public String getFormattedValue(float value){
//            return ((int)value)+"day";
//        }
//    }

    private void initView() {
        gif = (ImageView) findViewById(R.id.imagegif);
        TitleExercise = (TextView) findViewById(R.id.title_exercise);
        LineChart = (com.github.mikephil.charting.charts.LineChart) findViewById(R.id.linechart);
        IdRecommended = (TextView) findViewById(R.id.id_recommended);
        IdEntry = (EditText) findViewById(R.id.id_entry);
        SaveExercise = (Button) findViewById(R.id.save_exercise);
    }


}