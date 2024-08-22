package com.edinson.agroemnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LadinPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_ladin_principal);
        }

            // Manejar las inserciones de ventana para la vista principal
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            // Token encontrado, redirige a la pantalla principal
            Intent intent = new Intent(LadinPrincipal.this, MainActivity.class); // Cambia MainActivity por tu actividad principal
            startActivity(intent);
            finish(); // Cierra esta actividad para que el usuario no pueda volver a ella con el botón de retroceso
        } else {
            // Token no encontrado, muestra la pantalla de inicio de sesión
            setContentView(R.layout.activity_ladin_principal);

            // Manejar las inserciones de ventana para la vista principal
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Configurar el botón para iniciar sesión
            Button loginButton = findViewById(R.id.button);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(LadinPrincipal.this, Login.class);
                startActivity(intent);
            });
        }

        }
    }