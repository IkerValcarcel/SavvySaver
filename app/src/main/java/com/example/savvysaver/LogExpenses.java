package com.example.savvysaver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.savvysaver.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogExpenses extends AppCompatActivity implements View.OnClickListener {
    EditText expenseNameET;
    EditText expenseAmountET;
    Spinner expenseCategorySP;
    EditText expenseDescriptionET;
    Button saveExpenseBTN;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_expenses);

        // Establecemos la conexion con la base de datos
        db = new DataBase(this);

        // Obtenemos todos los elementos de la interfaz
        expenseNameET = findViewById(R.id.etExpenseName);
        expenseAmountET = findViewById(R.id.etExpenseAmount);
        expenseAmountET.setText("0"); // set default value to 0
        expenseCategorySP = findViewById(R.id.spExpenseCategory);
        expenseDescriptionET = findViewById(R.id.etExpenseDescription);
        saveExpenseBTN = findViewById(R.id.btnSaveExpense);

        // Añadimos el listener al boton
        saveExpenseBTN.setOnClickListener(this);

        // Rellenamos el desplegable
        ArrayList<String> expenseCategories = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.expense_types);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                expenseCategories.add(line);
            }
        } catch (IOException e) {
            // handle exception
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // handle exception
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, expenseCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseCategorySP.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveExpense:
                logExpense();
                break;
        }
    }

    protected void logExpense() {
        // Obtenemos el usuario de la sesion iniciada
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String username = preferences.getString("user", null);
        // Obtenemos los datos a cargar
        String expenseName = expenseNameET.getText().toString();
        float expenseAmount = Float.parseFloat(expenseAmountET.getText().toString());
        String expenseCategory = expenseCategorySP.getSelectedItem().toString();
        String expenseDescription = expenseDescriptionET.getText().toString();
        // Verificar que son correctos
        if (username == null)
            Toast.makeText(this, "Sesion no iniciada.", Toast.LENGTH_SHORT).show();
        else if (expenseName.length() < 4)
            Toast.makeText(this, "El nombre del gasto es demasiado corto (>= 4).", Toast.LENGTH_SHORT).show();
        else if (expenseDescription.length() <= 0)
            Toast.makeText(this, "La descripción del gasto es demasiado corta (>= 4).", Toast.LENGTH_SHORT).show();
        else {
            db.addExpense(username, expenseName, expenseAmount, expenseCategory, expenseDescription);
            // Mostramos la notificacion de que se ha añadido el gasto.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showExpenseNotification(username, expenseAmount);
            }
            // Mostramos el gasto añadido
            Intent intent = new Intent(this, SeeExpenses.class);
            startActivity(intent);
            finish();
        }
    }

    private void showExpenseNotification(String username, float expenseAmount) {
        // Se obtiene el gasto total del usuario para mostrar en la notificacion
        float totalExpendedAmount = db.getTotalExpendedAmount(username);
        // Se genera la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "expenses")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Nuevo gasto introducido de " + expenseAmount + "€")
                .setContentText("Los gastos totales son de " + totalExpendedAmount + "€.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
