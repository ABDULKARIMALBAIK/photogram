package com.abdulkarimalbaik.dev.photogram.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;

import com.abdulkarimalbaik.dev.photogram.Model.CipherImage;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DBImage extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private Context context;

    private static String DB_Name = "CipherImage.db";
    private static final int DATABASE_VER = 1;
    public static final String PASSWORD = ".......................";

    private static DBImage instance = null;

    private static String TABLE_NAME = "image";
    private static String COL_IMAGE_ID = "id";
    private static String COL_IMAGE_PICTURE = "picture";
    private static String COL_IMAGE_TITLE = "title";
    private static String COL_IMAGE_DISPLAYNAME = "display_name";
    private static String COL_IMAGE_DESCRIPTION = "description";
    private static String COL_IMAGE_DATE = "date_added";
    private static String COL_IMAGE_LENGTHENCRYPT = "length_encrypt_image";

    private static final String SQL_CREATE_TABLE_QUERY =
            "CREATE TABLE image (\n" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT  UNIQUE,\n" +
                    "picture BLOB NOT NULL,\n" +
                    "title TEXT NOT NULL,\n" +
                    "display_name TEXT NOT NULL,\n" +
                    "description TEXT NOT NULL,\n" +
                    "date_added TEXT NOT NULL,\n" +
                    "length_encrypt_image INTEGER NOT NULL );";

    private static final String SQL_DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS" + TABLE_NAME;

    public DBImage(Context context) {
        super(context, DB_Name, null, DATABASE_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }

    public static synchronized  DBImage getInstance(Context context){

        if (instance == null)
            instance = new DBImage(context);
        return instance;
    }

    public List<CipherImage> getAllImages(){

        SQLiteDatabase db = instance.getReadableDatabase(PASSWORD);
        Cursor cur = db.rawQuery(String.format
                        (
                                "SELECT * FROM '%s'",
                                TABLE_NAME
                        ),
                null
        );

        List<CipherImage> cipherImages = new ArrayList<>();

        if (cur.moveToFirst()){

            while (!cur.isAfterLast()){

                cipherImages.add(new CipherImage(
                        cur.getInt(cur.getColumnIndex(COL_IMAGE_ID)),
                        cur.getBlob(cur.getColumnIndex(COL_IMAGE_PICTURE)),
                        cur.getString(cur.getColumnIndex(COL_IMAGE_TITLE)),
                        cur.getString(cur.getColumnIndex(COL_IMAGE_DISPLAYNAME)),
                        cur.getString(cur.getColumnIndex(COL_IMAGE_DESCRIPTION)),
                        cur.getString(cur.getColumnIndex(COL_IMAGE_DATE)),
                        cur.getInt(cur.getColumnIndex(COL_IMAGE_LENGTHENCRYPT))
                ));

                cur.moveToNext();
            }

        }
        cur.close();

        return cipherImages;
    }

    public void deleteImage(String name){

        SQLiteDatabase db = instance.getWritableDatabase(PASSWORD);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGE_DISPLAYNAME , name);
        db.delete(TABLE_NAME ,COL_IMAGE_DISPLAYNAME  + "='" + name + "'"  , null);
        db.close();
    }

    public CipherImage selectImage(String name) {

        SQLiteDatabase db = instance.getReadableDatabase(PASSWORD);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE display_name = '" + name + "'" , null);

        CipherImage cipherImage = new CipherImage();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            cipherImage.setId(cursor.getInt(cursor.getColumnIndex(COL_IMAGE_ID)));
            cipherImage.setPicture(cursor.getBlob(cursor.getColumnIndex(COL_IMAGE_PICTURE)));
            cipherImage.setTitle(cursor.getString(cursor.getColumnIndex(COL_IMAGE_TITLE)));
            cipherImage.setDisplay_name(cursor.getString(cursor.getColumnIndex(COL_IMAGE_DISPLAYNAME)));
            cipherImage.setDescription(cursor.getString(cursor.getColumnIndex(COL_IMAGE_DESCRIPTION)));
            cipherImage.setDate_added(cursor.getString(cursor.getColumnIndex(COL_IMAGE_DATE)));
            cipherImage.setLength_encrypt_image(cursor.getInt(cursor.getColumnIndex(COL_IMAGE_LENGTHENCRYPT)));

            cursor.moveToNext();
        }

        cursor.close();

        return cipherImage;
    }

    public void saveImage(CipherImage cipherImage) {

        SQLiteDatabase db = instance.getWritableDatabase(PASSWORD);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGE_PICTURE , cipherImage.getPicture());
        contentValues.put(COL_IMAGE_TITLE , cipherImage.getTitle());
        contentValues.put(COL_IMAGE_DISPLAYNAME , cipherImage.getDisplay_name());
        contentValues.put(COL_IMAGE_DESCRIPTION , cipherImage.getDescription());
        contentValues.put(COL_IMAGE_DATE , cipherImage.getDate_added());
        contentValues.put(COL_IMAGE_LENGTHENCRYPT , cipherImage.getLength_encrypt_image());
        db.insert(TABLE_NAME , null , contentValues);
    }
}
