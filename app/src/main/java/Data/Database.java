package Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

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

    //Truy vấn có trả về kết quả : SELECT
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
