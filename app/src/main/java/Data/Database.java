package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Truy vấn không trả về kết quả :CREATE, INSERT, UPDATE, DELETE
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void Insert_image(String Title, byte[]ImageTitle,String Gif){
        SQLiteDatabase Sqldatabase = getWritableDatabase();
        String sql = "INSERT INTO Image VALUES (null,?,?,?)";
        SQLiteStatement statement = Sqldatabase.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,Title);
        statement.bindBlob(2,ImageTitle);
        statement.bindString(3,Gif);
        statement.executeInsert();//thực thi insert

    }
    public Boolean Insert_Time(long valueX, float valueY){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValues", valueX);
        contentValues.put("yValues",valueY);
        sqLiteDatabase.insert("Time",null,contentValues);
        return true;

    }

    //Truy vấn có trả về kết quả : SELECT
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public void SaveExercise(String valX, String valY){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(constant.DATE,valX);
        values.put(constant.yAXIS,valY);
        db.insert(constant.TABLE_NAME,null,values);


    }

    public ArrayList<String>QueryXData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> xData = new ArrayList<String>();
        String query = "SELECT "+ constant.DATE + " FROM " + constant.TABLE_NAME + " GROUP BY " + constant.DATE;
        Cursor cursor = db.rawQuery(query, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            xData.add(cursor.getString(0));
        }
        cursor.close();
        return xData;

    }

    public ArrayList<String>QueryYData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> yData = new ArrayList<String>();
        String query = "SELECT " + constant.yAXIS +" FROM " + constant.TABLE_NAME + " WHERE " + constant.yAXIS +" IS NOT NULL " + " GROUP BY " + constant.DATE;
        Cursor cursor = db.rawQuery(query, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            yData.add(cursor.getString(0));
        }
        cursor.close();
        return yData;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
