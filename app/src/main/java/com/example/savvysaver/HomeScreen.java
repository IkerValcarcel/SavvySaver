package com.example.savvysaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Button logoutBTN;
    private Button seeExpensesBTN;
    private Button logExpenseBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Obtenemos los elementos de la interfaz
        logoutBTN = findViewById(R.id.log_out);
        seeExpensesBTN = findViewById(R.id.see_expenses);
        logExpenseBTN = findViewById(R.id.log_expense);
        // // Se definenen los listeners en los botones
        logoutBTN.setOnClickListener(this);
        seeExpensesBTN.setOnClickListener(this);
        logExpenseBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Se clica en CERRAR SESION
            case R.id.log_out:
                logout();
                break;
            // Se clica en "INTRODUCIR NUEVO GASTO"
            case R.id.log_expense:
                logExpense();
                break;
            // Se clica en "VER TODOS LOS GASTO"
            case R.id.see_expenses:
                seeExpenses();
                break;
        }
    }
    private void logout() {
        // Se borra la session de shared preferences
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        preferences.edit().remove("user").commit();
        // Se abre la pestaña de inicio de sesion y se cierra el menu principal
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    private void logExpense(){
        Intent intent = null;
        intent = new Intent(this, LogExpenses.class);
        startActivity(intent);
    }
    private void seeExpenses(){
        Intent intent = null;
        intent = new Intent(this, SeeExpenses.class);
        startActivity(intent);
    }
}