package com.edinson.agroemnew.Usuario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.databinding.ActivityMainBinding;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";  // Para el log
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar BottomNavigationView y NavController
        setupNavigation();

        // Listener para el ítem de cerrar sesión
        setupLogoutListener();

        // Obtener token de FCM
        getFCMToken();
    }

    /**
     * Configura el sistema de navegación con el BottomNavigationView.
     */
    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_proyecto, R.id.navigation_perfil, R.id.navigation_cerrar)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Obtiene el token de Firebase Cloud Messaging (FCM).
     */
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Obtiene el nuevo token de registro de FCM
                        String token = task.getResult();

                        // Mostrar el token en un Log y un Toast
                        Log.d(TAG, "FCM Registration Token: " + token);
                        //Toast.makeText(MainActivity.this, "FCM Token: " + token, Toast.LENGTH_SHORT).show();

                        // Guardar el token en SharedPreferences
                        saveFCMToken(token);

                        // Obtener el ID del usuario desde SharedPreferences
                        String userId = getUserIdFromPreferences(); // Método para obtener el ID del usuario
                        if (userId != null) {
                            actualizarDeviceToken(userId, token);
                        } else {
                            Log.w(TAG, "User ID not found in SharedPreferences");
                        }
                    }
                });
    }

    private String getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        return sharedPreferences.getString("UserId", null); // Asegúrate de que el ID del usuario se guarda con esta clave
    }


    private void actualizarDeviceToken(String userId, String token) {
        ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
        Map<String, String> deviceTokenMap = new HashMap<>();
        deviceTokenMap.put("deviceToken", token);

        Call<Void> call = apiService.actualizarDeviceToken(userId, deviceTokenMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Device token updated successfully");
                    //Toast.makeText(MainActivity.this, "Device token updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w(TAG, "Failed to update device token: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error updating device token: " + t.getMessage());
            }
        });
    }



    private void saveFCMToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", token);
        editor.apply();
    }

    private void setupLogoutListener() {
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_cerrar) {
                logoutUser();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment_activity_main))
                    || MainActivity.super.onOptionsItemSelected(item);
        });
    }

    private void logoutUser() {
        // Limpiar las SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir al login y limpiar el historial de actividades
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
