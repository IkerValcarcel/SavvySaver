package com.example.savvysaver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savvysaver.Adapter.Expense;
import com.example.savvysaver.Adapter.ExpenseAdapter;
import com.example.savvysaver.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class SeeExpenses extends AppCompatActivity {

    RecyclerView listView;
    ExpenseAdapter expenseAdapter;
    ArrayList<HashMap<String,String>> expenses;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_expenses);

        db = new DataBase(this);

        // Listamos los gastos
        listExpenses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listExpenses();
    }

    private void listExpenses() {
        // Obtenemos el usuario iniciado
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String user = preferences.getString("user", null);
        // Obtenemos los gastos
        expenses = db.getExpenses(user);
        ArrayList<Expense> listData = new ArrayList<Expense>();
        for (HashMap<String, String> data : expenses) {
            Expense expense = new Expense(
                    data.get(Utilities.EXPENSES_ID),
                    data.get(Utilities.EXPENSES_NAME),
                    data.get(Utilities.EXPENSES_AMOUNT),
                    data.get(Utilities.EXPENSES_TYPE),
                    data.get(Utilities.EXPENSES_DESCRIPTION));
            listData.add(expense);
        }
        listView = findViewById(R.id.expenses);
        listView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(listData, this);
        listView.setAdapter(expenseAdapter);
    }
}
