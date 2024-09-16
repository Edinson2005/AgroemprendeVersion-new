package com.edinson.agroemnew.Usuario;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import com.edinson.agroemnew.R;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CANAL_ID",
                    "Nombre del Canal",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Descripción del canal");

            // Registra el canal en el sistema
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
