package com.edinson.agroemnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.Notificacion;
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

        // Ocultar la barra de acción si existe
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerviewNotificacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear instancia del servicio API
        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        // Recuperar el token desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        token = sharedPreferences.getString("UserToken", "");

        // Verificar el valor del token
        Log.d("Notificaciones", "Token recuperado: " + token);

        // Verificar si el token está vacío
        if (token.isEmpty()) {
            Log.e("Notificaciones", "Token de autenticación no encontrado.");
            return;
        }

        // Cargar las notificaciones
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        // Usar el token para realizar la solicitud a la API
        apiService.getNotificaciones("Bearer " + token).enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (response.isSuccessful()) {
                    List<Notificacion> notificaciones = response.body();
                    Log.d("Notificaciones", "Datos recibidos: " + notificaciones);
                    mostrarNotificaciones(notificaciones);
                } else {
                    Log.e("Notificaciones", "Error al obtener las notificaciones: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                Log.e("Notificaciones", "Error en la solicitud: " + t.getMessage());
            }
        });
    }

    private void mostrarNotificaciones(List<Notificacion> notificaciones) {
        if (notificaciones != null && !notificaciones.isEmpty()) {
            // Pasar el contexto junto con la lista de notificaciones
            adapter = new NotificacionAdapter(notificaciones, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("Notificaciones", "Lista de notificaciones es nula o está vacía.");
        }
    }
}