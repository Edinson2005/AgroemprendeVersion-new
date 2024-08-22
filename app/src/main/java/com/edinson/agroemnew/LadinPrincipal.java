package com.edinson.agroemnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LadinPrincipal extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_ladin_principal);

        imageView = findViewById(R.id.imageView3);

        // Cargar y aplicar animación
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        imageView.startAnimation(slideInAnimation);

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
            Intent intent = new Intent(LadinPrincipal.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra esta actividad para que el usuario no pueda volver a ella con el botón de retroceso
        } else {
            // Token no encontrado, muestra la pantalla de inicio de sesión
            Button loginButton = findViewById(R.id.button);
            loginButton.setOnClickListener(v -> {
                Intent intent = new Intent(LadinPrincipal.this, Login.class);
                startActivity(intent);
            });
        }
    }
}
