package com.edinson.agroemnew.proyecto;

import android.content.SharedPreferences;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        apiService.getNotificaciones("Bearer " + token).enqueue(new Callback<List<NotiConvocatorias>>() {
            @Override
            public void onResponse(Call<List<NotiConvocatorias>> call, Response<List<NotiConvocatorias>> response) {
                if (response.isSuccessful()) {
                    List<NotiConvocatorias> notificaciones = response.body();
                    Log.d("Notificaciones", "Datos recibidos: " + notificaciones);
                    mostrarNotificaciones(notificaciones);
                } else {
                    Log.e("Notificaciones", "Error al obtener las notificaciones: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<NotiConvocatorias>> call, Throwable t) {
                Log.e("Notificaciones", "Error en la solicitud: " + t.getMessage());
            }
        });
    }

    private void mostrarNotificaciones(List<NotiConvocatorias> nuevasNotificaciones) {
        if (nuevasNotificaciones != null && !nuevasNotificaciones.isEmpty()) {
            if (adapter == null) {
                // Si el adaptador es nulo, inicializa el adaptador con las notificaciones recibidas
                adapter = new NotificacionAdapter(nuevasNotificaciones, this);
                recyclerView.setAdapter(adapter);
            } else {
                // Si el adaptador ya existe, agregar las nuevas notificaciones al inicio de la lista
                adapter.agregarNotificaciones(nuevasNotificaciones);
            }
        } else {
            Toast.makeText(this, "No hay convocatorias disponibles", Toast.LENGTH_SHORT).show();
        }
    }
}
