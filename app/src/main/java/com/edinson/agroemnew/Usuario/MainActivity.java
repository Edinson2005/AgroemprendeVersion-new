package com.edinson.agroemnew.Usuario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edinson.agroemnew.DeviceManager;
import com.edinson.agroemnew.R;

import com.edinson.agroemnew.databinding.ActivityMainBinding;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.Convocatoria;
import com.edinson.agroemnew.modelApi.notificaciones.ConvocatoriaNtf;
import com.edinson.agroemnew.modelApi.notificaciones.Template;
import com.edinson.agroemnew.modelApi.notificaciones.TokenRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;



public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";  // Para el log
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar BottomNavigationView y NavController
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_proyecto, R.id.navigation_perfil, R.id.navigation_cerrar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Listener para el ítem de cerrar sesión
        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_cerrar) {
                logoutUser();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || MainActivity.super.onOptionsItemSelected(item);
        });

        // Obtener el token de FCM
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Obtener el nuevo token de registro FCM
                    String token = task.getResult();
                    SharedPreferences sharedPreferences = getSharedPreferences("SP_FILE", 0);
                    String tokenGuardado = sharedPreferences.getString("DEVICEID", null);

                    // Si el token es diferente o no existe, registrar en el servidor
                    if (token != null) {
                        if (tokenGuardado == null || !token.equals(tokenGuardado)) {
                            // Registrar el token en el servidor
                            DeviceManager.postRegistrarDispositivoEnServidor(token, MainActivity.this);

                            // Guardar el nuevo token en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("DEVICEID", token);
                            editor.apply();

                            // Enviar token a la API
                            enviarTokenApi(token);
                        }
                    }

                    // Mostrar el token en un Toast
                    String msg = "FCM Token: " + token;
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    // Aquí también se guarda el token en SharedPreferences si es necesario
                    saveTokenToPreferences(token);
                });
    }

    private void enviarTokenApi(String token) {
        Log.d(TAG, "Método enviarTokenApi llamado con token: " + token);

        ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        // Ajustar el constructor de Template según sea necesario
        Template template = new Template("Planteamiento del problema", "Descripción del problema");

        ConvocatoriaNtf convocatoriaNtf = new ConvocatoriaNtf(
                "2025-06-20",
                "Proyectos sobre el campo colombiano",
                "Se reciben proyectos orientados a mejorar la economía campesina",
                "2024-09-18T22:57:05.211Z",
                "Activa",
                "Insertar un archivo que contenga información sobre la convocatoria",
                template
        );

        // Crear el objeto TokenRequest
        TokenRequest tokenRequest = new TokenRequest(token, "Título de la notificación", "Cuerpo de la notificación", convocatoriaNtf, "Vista");

        // Log para confirmar la creación del objeto TokenRequest
        Log.d(TAG, "TokenRequest creado: " + tokenRequest.toString());

        // Llamada al endpoint de enviar token
        apiService.enviarToken(tokenRequest).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                Log.d(TAG, "Respuesta recibida del servidor. Código: " + response.code());
                if (response.isSuccessful()) {
                    Log.d(TAG, "Token enviado correctamente al servidor.");
                    // Mostrar un Toast indicando que el token se envió correctamente
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Token enviado correctamente.", Toast.LENGTH_SHORT).show());
                } else {
                    Log.e(TAG, "Error al enviar el token: " + response.code());
                    // Mostrar un Toast indicando que hubo un error al enviar el token
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al enviar el token. Código: " + response.code(), Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Log.e(TAG, "Fallo en la llamada al enviar token: " + t.getMessage());
                // Mostrar un Toast indicando que hubo un fallo en la llamada
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Fallo en el envío del token: " + t.getMessage(), Toast.LENGTH_LONG).show());
            }
        });

        // Log después de llamar a enqueue para verificar que el método se ha ejecutado
        Log.d(TAG, "Llamada a enqueue realizada.");
    }




    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Borra todas las preferencias
        editor.apply();
        Intent intent = new Intent(getApplication(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);
    }

    private void saveTokenToPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", token);  // Guardar el token
        editor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
