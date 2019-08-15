package com.selvamani.mykartthoughtworks.LocalDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.Cursor;

import com.selvamani.mykartthoughtworks.LocalDatabase.DAO.DAO;
import com.selvamani.mykartthoughtworks.Model.CartModel;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.Model.UserModel;


@Database(entities = {UserModel.class, ProductsModel.class, CartModel.class}, version =1,exportSchema = false)//
//@TypeConverters({RoomConverters.class})

public abstract class LocalDBConnection extends  RoomDatabase {

    private static LocalDBConnection INSTANCE;
    private static SupportSQLiteDatabase db;
    private static final String DATABASE_NAME = "MyKart.db";

    public static LocalDBConnection getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDBConnection.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LocalDBConnection.class,
                            DATABASE_NAME)
                            .setJournalMode(JournalMode.TRUNCATE)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public abstract DAO LoadUserdata();

    public boolean isColumnExists (String table, String column) {
        boolean isExists = false;
        Cursor cursor = null;
        try {
            cursor = db.query("PRAGMA table_info("+ table +")");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equalsIgnoreCase(name)) {
                        isExists = true;
                        break;
                    }
                }
            }

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return isExists;
    }

}
