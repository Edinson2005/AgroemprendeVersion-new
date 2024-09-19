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

import java.util.HashMap;
import java.util.Map;


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
                    SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                    String tokenGuardado = sharedPreferences.getString("FCMToken", null);

                    // Si el token es diferente o no existe, registrar en el servidor
                    if (token != null) {
                        if (tokenGuardado == null || !token.equals(tokenGuardado)) {
                            // Registrar el token en el servidor
                            DeviceManager.postRegistrarDispositivoEnServidor(token, MainActivity.this);

                            // Guardar el nuevo token en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("DEVICEID", token);
                            editor.apply();

                            actualizarTokenEnServidor();

                        }
                    }

                    // Mostrar el token en un Toast
                    String msg = "FCM Token: " + token;
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    // Aquí también se guarda el token en SharedPreferences si es necesario
                    saveTokenToPreferences(token);
                });
    }

    private void actualizarTokenEnServidor() {
        // Obtener el token guardado desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String tokenGuardado = sharedPreferences.getString("FCMToken", null);

        // Obtener el ID del usuario desde SharedPreferences
        String userId = sharedPreferences.getString("UserId", null);

        if (tokenGuardado != null && userId != null) {
            // Crear el Map con el token
            Map<String, String> deviceToken = new HashMap<>();
            deviceToken.put("deviceToken", tokenGuardado);

            // Crear una instancia de ApiService
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

            // Llamar al método para actualizar el token en el servidor
            apiService.actualizarDeviceToken(userId, deviceToken).enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Token actualizado correctamente en el servidor.");
                        Toast.makeText(MainActivity.this, "Token actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Error al actualizar el token: " + response.code());
                        Toast.makeText(MainActivity.this, "Error al actualizar el token. Código: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    Log.e(TAG, "Fallo en la llamada al actualizar token: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Fallo en la actualización del token: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.e(TAG, "Token o ID de usuario no encontrados.");
        }
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
        editor.putString("UserToken", token);  // Guardar el token
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
