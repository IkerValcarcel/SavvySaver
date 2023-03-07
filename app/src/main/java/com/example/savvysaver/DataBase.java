package com.example.savvysaver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SavvySaver";
    private static final int DATABASE_VERSION = 1;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creamos la tabla Users que contiene la un nombre de usuario y una contraseña
        sqLiteDatabase.execSQL("CREATE TABLE Users (" +
                "'username' VARCHAR(255) PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "'password' VARCHAR(255))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addUser(String username, String password) {
        // Obtenemos el objeto que nos permite escribir en la base de datos
        SQLiteDatabase thisBD = getWritableDatabase();
        // Creamos la sentencia que se va a ejecutar
        String dbQuery = String.format(
                "INSERT INTO Users(username, password) VALUES(%s, %s);",
                username, password
        );
        // Ejecutamos la sentencia
        thisBD.execSQL(dbQuery);
    }

    public String getUserPassword(String username) {
        // Obtenemos el objeto que nos permite leer en la base de datos
        SQLiteDatabase thisBD = getReadableDatabase();
        // Creamos la sentencia que se va a ejecutar
        String dbQuery = "SELECT password FROM Users WHERE username=?;";
        String[] dbParams = new String[]{username};
        // Ejecutamos la sentencia y devolvemos la contraseña
        Cursor res = thisBD.rawQuery(dbQuery ,dbParams);
        String password = res.getString(0);
        res.close();
        return password;
    }

    public boolean checkLogin(String username, String password) {
        //Obtenemos la contraseña de la base de datos y comprobamos si es igual que la introducida
        String dbPassword = getUserPassword(username);
        return dbPassword.equals(password);
    }
}
