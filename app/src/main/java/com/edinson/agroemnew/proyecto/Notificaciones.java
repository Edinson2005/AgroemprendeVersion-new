package com.edinson.agroemnew.proyecto;

import android.content.SharedPreferences;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.NotiConvocatorias;
import com.edinson.agroemnew.adapters.NotificacionAdapter;

public class Notificaciones extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificacionAdapter adapter;
    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerviewNotificacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificacionAdapter(this);
        recyclerView.setAdapter(adapter);

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        token = sharedPreferences.getString("UserToken", "");

        Log.d("Notificaciones", "Token recuperado: " + token);

        if (token.isEmpty()) {
            Log.e("Notificaciones", "Token de autenticación no encontrado.");
            return;
        }

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        apiService.getNotificaciones("Bearer " + token).enqueue(new Callback<List<NotiConvocatorias>>() {
            @Override
            public void onResponse(Call<List<NotiConvocatorias>> call, Response<List<NotiConvocatorias>> response) {
                if (response.isSuccessful()) {
                    List<NotiConvocatorias> notificaciones = response.body();
                    Log.d("Notificaciones", "Datos recibidos: " + notificaciones);
                    mostrarNotificaciones(notificaciones);
                } else {
                    Log.e("Notificaciones", "Error al obtener las notificaciones: " + response.message());
                    Toast.makeText(Notificaciones.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotiConvocatorias>> call, Throwable t) {
                Log.e("Notificaciones", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(Notificaciones.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNotificaciones(List<NotiConvocatorias> nuevasNotificaciones) {
        if (nuevasNotificaciones != null && !nuevasNotificaciones.isEmpty()) {
            // Invertir el orden de la lista para que las más recientes aparezcan primero
            List<NotiConvocatorias> notificacionesInvertidas = new ArrayList<>(nuevasNotificaciones);
            Collections.reverse(notificacionesInvertidas);

            adapter.setNotificaciones(notificacionesInvertidas);
        } else {
            Toast.makeText(this, "No hay convocatorias disponibles", Toast.LENGTH_SHORT).show();
        }
    }
}