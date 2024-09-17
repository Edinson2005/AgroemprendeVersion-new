package com.edinson.agroemnew.Usuario;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import android.Manifest;

import com.edinson.agroemnew.NotificationDebugReceiver;
import com.edinson.agroemnew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.edinson.agroemnew.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    private NotificationDebugReceiver debugReceiver;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Solicita permisos para notificaciones si es necesario
        requestNotificationPermission();

        // Configura el receptor de notificaciones
        debugReceiver = new NotificationDebugReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(debugReceiver,
                new IntentFilter("NOTIFICACION_RECIBIDA"));

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Configura AppBarConfiguration y NavController
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_proyecto, R.id.navigation_perfil, R.id.navigation_cerrar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Configura el listener para los ítems del menú de navegación
        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_cerrar) {
                logoutUser();
                return true;
            }
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || MainActivity.super.onOptionsItemSelected(item);
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes proceder con las notificaciones
            } else {
                // Permiso denegado, maneja la situación según corresponda
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegúrate de desregistrar el receiver cuando la actividad se destruye
        if (debugReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(debugReceiver);
        }
    }
}
