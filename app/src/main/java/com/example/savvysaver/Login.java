package com.example.savvysaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private DataBase db;
    private EditText usernameET, passwordET;
    private Button loginBTN, registerBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Establecemos conexion con la base de datos
        db = new DataBase(this);

        // Obtenemos los elementos de la interfaz
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        loginBTN = findViewById(R.id.login);
        registerBTN = findViewById(R.id.register);

        // Se definenen los listeners en los botones
        loginBTN.setOnClickListener(this);
        registerBTN.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Call finish to close the activity
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) {
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                // Se clica el boton de login
                login();
                break;
            case R.id.register:
                // Se clica el boton de registro
                register();
                break;
        }
    }

    protected void login() {
        // Obtenemos el texto introducido por el usuario
        String inUsername = usernameET.getText().toString().trim();
        String inPassword = passwordET.getText().toString().trim();
        // Verificamos que este sea correcto y si lo es se inicia sesion
        if (db.isLoginCorrect(inUsername, inPassword)) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            // Se añade a las preferencias las credenciales del usuario
            SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user", inUsername);
            editor.commit();
            // Se inicia el menu principal
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Nombre de usuario o contraseña invalida", Toast.LENGTH_SHORT).show();
        }
    }

    protected void register() {
        // Nos movemos al activity del registro
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}