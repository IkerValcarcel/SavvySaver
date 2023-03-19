package com.example.savvysaver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private DataBase db;
    EditText emailET, usernameET, passwordET;
    Button registerBTN, returnBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DataBase(this);

        emailET = findViewById(R.id.email);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        registerBTN = findViewById(R.id.register);
        returnBTN = findViewById(R.id.return_button);

        registerBTN.setOnClickListener(this);
        returnBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                register();
                break;
            case R.id.return_button:
                finish();
                break;
        }
    }

    private void register() {
        // Recogemos los datos introducidos por el usuario
        String email = emailET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        // Verificamos que los datos cumplen los requisitos para registrarlos
        if (!isValidEmail(email))
            Toast.makeText(this, "El formato del correo electrónico es incorrecto.", Toast.LENGTH_SHORT).show();
        else if (username.length() < 4)
            Toast.makeText(this, "El nombre de usuario es demasiado corto (>= 4).", Toast.LENGTH_SHORT).show();
        else if (!db.isUserNameFree(username))
            Toast.makeText(this, "El nombre de usuario esta ocupado.", Toast.LENGTH_SHORT).show();
        else if (password.length() < 8)
            Toast.makeText(this, "La contraseña es demasiado corta (>= 8).", Toast.LENGTH_SHORT).show();
        else {
            // Se añade el usuario
            db.addUser(email, username, password);
            // Se añade a las preferencias las credenciales del usuario
            SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user", username);
            editor.commit();
            // Se inicia la pantalla de menu y se cierra el registro y el login
            Intent intent = new Intent(this, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private boolean isValidEmail(String email){
        // Se comprueba si el formato del correo es correcto.
        if (!email.contains("@"))
            return false;
        String[] splitEmail = email.split("@");
        return splitEmail[0].length() != 0 && splitEmail[1].length() != 0;
    }
}
