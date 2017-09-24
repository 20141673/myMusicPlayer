package cn.edu.cqu.mytestapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 陈圳林 on 2017/3/22.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="SongLists.db";
    private static final String TEB_NAME="LocalLists";
    private static final String CREATE_TBL="Create table "+" LocalLists(tvPath text primary key,ivIcon integer,tvName text)";
    private SQLiteDatabase db;
    public DBHelper(Context c) {
        super(c,DB_NAME,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        db.execSQL(CREATE_TBL);
    }
    public void insert(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TEB_NAME,null,values);
    }
    public Cursor query(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(TEB_NAME,null,null,null,null,null,null);
        return c;
    }
    public void del(String url){
        if(db==null)
            db=getWritableDatabase();
        db.delete(TEB_NAME,"tvPath=?",new String[] {url});
    }
    public void close(){
        if(db!=null)
            db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
