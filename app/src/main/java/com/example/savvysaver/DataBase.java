package com.example.savvysaver;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.savvysaver.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SavvySaver";
    private static final int DATABASE_VERSION = 1;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creamos la tabla Users que contiene la un nombre de usuario y una contraseña
        sqLiteDatabase.execSQL(Utilities.USERS_TABLE_DEFINITION);
        sqLiteDatabase.execSQL(Utilities.EXPENSES_TABLE_DEFINITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utilities.USERS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utilities.EXPENSES_TABLE);
    }
    // Metodos de Usuarios
    public void addUser(String email, String username, String password) {
        // Conexion con la base de datos
        SQLiteDatabase db = getWritableDatabase();
        // Definimos los parametros a introducir en la tabla
        ContentValues values = new ContentValues();
        values.put(Utilities.USERS_EMAIL, email);
        values.put(Utilities.USERS_USERNAME, username);
        values.put(Utilities.USERS_PASSWORD, password);
        // Insertamos los datos
        db.insert(Utilities.USERS_TABLE, Utilities.USERS_USERNAME, values);
        // Cerramos la conexion
        db.close();
    }

    public boolean isUserNameFree(String username) {
        // Conexion con la base de datos
        SQLiteDatabase db = getReadableDatabase();
        // Definimos los parametros a consultar en la tabla
        String[] inputParameters = {username};
        String[] returnParameters = {Utilities.USERS_USERNAME};
        // Ejecutamos la consulta
        Cursor cursor = db.query(Utilities.USERS_TABLE,
                returnParameters, Utilities.USERS_USERNAME + "=?",
                inputParameters, null, null, null);
        return cursor.getCount() == 0;
    }

    public boolean isLoginCorrect(String username, String password) {
        // Conexion con la base de datos
        SQLiteDatabase db = getReadableDatabase();
        // Definimos los parametros a consultar en la tabla
        String[] inputParameters = {username};
        String[] returnParameters = {Utilities.USERS_USERNAME, Utilities.USERS_PASSWORD};
        // Ejecutamos la consulta
        Cursor cursor = db.query(Utilities.USERS_TABLE,
                returnParameters, Utilities.USERS_USERNAME + "=?",
                inputParameters, null, null, null);
        cursor.moveToFirst();
        // Comprobamos que el usuario exista
        if (cursor.getCount() == 0)
            return false;
        // Obtenemos la contraseña correcta
        String correctPassword = cursor.getString(1);
        return password.equals(correctPassword);
    }
    // Metodos de gastos
    public void addExpense(String username, String name, float amount, String type, String description) {
        // Conexion con la base de datos
        SQLiteDatabase db = getWritableDatabase();
        // Definimos los parametros a introducir en la tabla
        ContentValues values = new ContentValues();
        values.put(Utilities.EXPENSES_USERNAME, username);
        values.put(Utilities.EXPENSES_NAME, name);
        values.put(Utilities.EXPENSES_AMOUNT, amount);
        values.put(Utilities.EXPENSES_TYPE, type);
        values.put(Utilities.EXPENSES_DESCRIPTION, description);
        // Insertamos los datos
        db.insert(Utilities.EXPENSES_TABLE, Utilities.EXPENSES_NAME, values);
        // Cerramos la conexion
        db.close();
    }
    public ArrayList<HashMap<String, String>> getExpenses(String username) {
        // Conexion con la base de datos
        SQLiteDatabase db = getReadableDatabase();
        // Definimos los parametros a consultar en la tabla
        String[] inputParameters = {username};
        String[] returnParameters = {
                Utilities.EXPENSES_ID,
                Utilities.EXPENSES_NAME,
                Utilities.EXPENSES_AMOUNT,
                Utilities.EXPENSES_TYPE,
                Utilities.EXPENSES_DESCRIPTION
        };
        // Ejecutamos la consulta
        Cursor cursor = db.query(Utilities.EXPENSES_TABLE,
                returnParameters,Utilities.EXPENSES_USERNAME + "=?",
                inputParameters, null, null, null);
        //Guardamos el resultado en una lista
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put(Utilities.EXPENSES_ID, cursor.getString(cursor.getColumnIndexOrThrow(Utilities.EXPENSES_ID)));
                data.put(Utilities.EXPENSES_NAME, cursor.getString(cursor.getColumnIndexOrThrow(Utilities.EXPENSES_NAME)));
                data.put(Utilities.EXPENSES_AMOUNT, cursor.getString(cursor.getColumnIndexOrThrow(Utilities.EXPENSES_AMOUNT)));
                data.put(Utilities.EXPENSES_TYPE, cursor.getString(cursor.getColumnIndexOrThrow(Utilities.EXPENSES_TYPE)));
                data.put(Utilities.EXPENSES_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(Utilities.EXPENSES_DESCRIPTION)));

                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return dataList;
    }

    public HashMap<String, String> getExpense(String user, String id){
        ArrayList<HashMap<String, String>> expenses = getExpenses(user);
        for (HashMap<String, String> expense: expenses) {
            if (id.equals(expense.get(Utilities.EXPENSES_ID)))
                    return expense;
        }
        return null;
    }

    public void updateExpenses(String id, String name, float amount, String type, String description) {
        // Conexion con la base de datos
        SQLiteDatabase db = getWritableDatabase();
        // Añadimos los valores a modificar
        ContentValues values = new ContentValues();
        values.put(Utilities.EXPENSES_NAME, name);
        values.put(Utilities.EXPENSES_AMOUNT, amount);
        values.put(Utilities.EXPENSES_TYPE, type);
        values.put(Utilities.EXPENSES_DESCRIPTION, description);
        // Ahora definimos el where
        String whereClause = "id = ?";
        String[] whereArgs = {id};
        // Ejecutamos la consulta
        db.update(Utilities.EXPENSES_TABLE, values, whereClause, whereArgs);
        // Cerramos la base de datos
        db.close();
    }

    public void deleteExpense(String id) {
        // Conexion con la base de datos
        SQLiteDatabase db = getWritableDatabase();
        // Definimos el where
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(id) };
        // Ejecutamos la consulta
        db.delete(Utilities.EXPENSES_TABLE, whereClause, whereArgs);
        // Cerramos la base de datos
        db.close();
    }

    public float getTotalExpendedAmount(String user) {
        ArrayList<HashMap<String, String>> expenses = getExpenses(user);
        float totalExpendedAmount = (float) expenses.stream()
                .mapToDouble(expense -> Integer.valueOf(expense.get(Utilities.EXPENSES_AMOUNT)))
                .sum();
        return totalExpendedAmount;
    }
}
