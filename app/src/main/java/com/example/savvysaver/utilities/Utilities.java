package com.example.savvysaver.utilities;

import android.content.SharedPreferences;

public class Utilities {
    // Users Table
    public static final String USERS_TABLE = "Users";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_TABLE_DEFINITION = "CREATE TABLE " + USERS_TABLE + " ("
            + USERS_EMAIL + " VARCHAR(255), "
            + USERS_USERNAME + " VARCHAR(255) PRIMARY KEY NOT NULL, "
            + USERS_PASSWORD + " VARCHAR(255)" +
            ")";
    // Expenses Table
    public static final String EXPENSES_TABLE = "Expenses";
    public static final String EXPENSES_ID = "id";
    public static final String EXPENSES_USERNAME = "username";
    public static final String EXPENSES_NAME = "expense_name";
    public static final String EXPENSES_TYPE = "type";
    public static final String EXPENSES_DESCRIPTION = "description";
    public static final String EXPENSES_AMOUNT = "amount";
    public static final String EXPENSES_TABLE_DEFINITION = "CREATE TABLE " + EXPENSES_TABLE + " ("
            + EXPENSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EXPENSES_USERNAME + " VARCHAR(255), "
            + EXPENSES_NAME + " VARCHAR(255), "
            + EXPENSES_TYPE + " VARCHAR(255), "
            + EXPENSES_DESCRIPTION + " VARCHAR(255), "
            + EXPENSES_AMOUNT + " FLOAT, "
            + "FOREIGN KEY (" + EXPENSES_USERNAME + ") REFERENCES " + USERS_TABLE + "(" + USERS_USERNAME + ")"
            + ")";
}
