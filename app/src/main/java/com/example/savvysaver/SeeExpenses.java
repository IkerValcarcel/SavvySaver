package com.example.savvysaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.savvysaver.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class SeeExpenses extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<HashMap<String,String>> expenses;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_expenses);

        db = new DataBase(this);
        listView = findViewById(R.id.expenses);

        // Listamos los gastos
        listExpenses();
        // EventListener para el ListView
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listExpenses();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, ModifyExpense.class);
        // Pasamos los datos al para que el usuario pueda visualizarlos completos
        intent.putExtra("data", expenses.get(position));
        startActivity(intent);
    }

    private void listExpenses() {
        // Obtenemos el usuario iniciado
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String user = preferences.getString("user", null);
        // Obtenemos los gastos
        expenses = db.getExpenses(user);
        ArrayList<String> listData = new ArrayList<String>();
        for (HashMap<String, String> data : expenses) {
            String listItem = "Nombre: " + data.get(Utilities.EXPENSES_NAME) + " - Cantidad: " + data.get(Utilities.EXPENSES_AMOUNT) + "€";
            listData.add(listItem);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }
}
