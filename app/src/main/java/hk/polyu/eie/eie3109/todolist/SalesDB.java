package hk.polyu.eie.eie3109.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SalesDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SalesDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CUSTOMER = "customer";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_GENDER = "gender";

    public SalesDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + " integer primary key autoincrement," +
                "" + CUSTOMER_NAME + " text,"
                + "" + CUSTOMER_GENDER + " text)";
        try {
            db.execSQL(sql);

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
