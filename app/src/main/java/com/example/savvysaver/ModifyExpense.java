package com.example.savvysaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.savvysaver.utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ModifyExpense extends AppCompatActivity implements View.OnClickListener {
    EditText expenseNameET;
    EditText expenseAmountET;
    Spinner expenseCategorySP;
    EditText expenseDescriptionET;
    Button modifyExpenseBTN;
    Button deleteExpenseBTN;
    DataBase db;

    ArrayList<String> expenseCategories;
    HashMap<String, String> expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_expense);

        // Establecemos la conexion con la base de datos
        db = new DataBase(this);

        // Obtenemos todos los elementos de la interfaz
        expenseNameET = findViewById(R.id.etExpenseName);
        expenseAmountET = findViewById(R.id.etExpenseAmount);
        expenseAmountET.setText("0");
        expenseCategorySP = findViewById(R.id.spExpenseCategory);
        expenseDescriptionET = findViewById(R.id.etExpenseDescription);
        modifyExpenseBTN = findViewById(R.id.btnModifyExpense);
        deleteExpenseBTN = findViewById(R.id.btnDeleteExpense);

        // Añadimos el listener al boton
        modifyExpenseBTN.setOnClickListener(this);
        deleteExpenseBTN.setOnClickListener(this);

        // Rellenamos el desplegable
        expenseCategories = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.expense_types);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                expenseCategories.add(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, expenseCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseCategorySP.setAdapter(adapter);
        // Definimos los valores
        setValues();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModifyExpense:
                modifyExpense();
                break;
            case R.id.btnDeleteExpense:
                deleteExpense();
                break;
        }
    }

    private void setValues() {
        // Obtenemos los datos de la instacia a modificar
        Intent intent = getIntent();
        expense = (HashMap<String, String>) intent.getSerializableExtra("data");

        String name = expense.get(Utilities.EXPENSES_NAME);
        String amount = expense.get(Utilities.EXPENSES_AMOUNT);
        String type = expense.get(Utilities.EXPENSES_TYPE);
        int typePos = getTypeIndex(type);
        String description = expense.get(Utilities.EXPENSES_DESCRIPTION);

        expenseNameET.setText(name);
        expenseAmountET.setText(amount);
        expenseCategorySP.setSelection(typePos);
        expenseDescriptionET.setText(description);
    }

    private int getTypeIndex(String type) {
        for (int i = 0; i < expenseCategories.size(); i++) {
            if (expenseCategories.get(i).equals(type))
                return i;
        }
        return 0;
    }

    private void modifyExpense() {
        // Obtenemos los datos a cargar
        String id = expense.get(Utilities.EXPENSES_ID);
        String expenseName = expenseNameET.getText().toString();
        float expenseAmount = Float.parseFloat(expenseAmountET.getText().toString());
        String expenseCategory = expenseCategorySP.getSelectedItem().toString();
        String expenseDescription = expenseDescriptionET.getText().toString();
        // Verificar que son correctos
        if (expenseName.length() < 4)
            Toast.makeText(this, "El nombre del gasto es demasiado corto (>= 4).", Toast.LENGTH_SHORT).show();
        else if (expenseDescription.length() <= 0)
            Toast.makeText(this, "La descripción del gasto es demasiado corta (>= 4).", Toast.LENGTH_SHORT).show();
        else {
            // Creamos un dialogo para que el usuario confirme que quiere actualizar el gasto.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estas seguro de que quieres hacer la modificación?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id1) {
                    // Esto sirve para indicar a la actividad anterior que se ha hecho un cambio en la base de datos y que tiene que actualizar la interfaz grafica.
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    // Mostramos el gasto añadido
                    db.updateExpenses(id, expenseName, expenseAmount, expenseCategory, expenseDescription);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private void deleteExpense() {
        // Obtenemos los el id del gasto
        String id = expense.get(Utilities.EXPENSES_ID);
        // Creamos un dialogo para que el usuario confirme que quiere borrar el gasto.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estas seguro de que quieres hacer la modificación?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                // Borramos el gasto
                db.deleteExpense(id);
                // Cerramos la actividad
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
