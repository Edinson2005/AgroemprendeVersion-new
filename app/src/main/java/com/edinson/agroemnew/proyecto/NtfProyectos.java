package com.edinson.agroemnew.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.adapters.NotiProyectoAdapter;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.notificaciones.ProyectoNot;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NtfProyectos extends AppCompatActivity {


    private RecyclerView recyclerView;
    private NotiProyectoAdapter adapter;
    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntf_proyectos);

        // Ocultar la barra de acción si existe
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerviewNotificacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        token = sharedPreferences.getString("UserToken", "");

        Log.d("NtfProyectos", "Token recuperado: " + token);

        if (token.isEmpty()) {
            Log.e("NtfProyectos", "Token de autenticación no encontrado.");
            Toast.makeText(this, "Error de autenticación. Por favor, inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        cargarProyectos();
    }

    private void cargarProyectos() {
        apiService.getProyectos("Bearer " + token).enqueue(new Callback<List<ProyectoNot>>() {
            @Override
            public void onResponse(Call<List<ProyectoNot>> call, Response<List<ProyectoNot>> response) {
                if (response.isSuccessful()) {
                    List<ProyectoNot> proyectos = response.body();
                    Log.d("NtfProyectos", "Datos recibidos: " + proyectos);
                    mostrarProyectos(proyectos);
                } else {
                    Log.e("NtfProyectos", "Error al obtener los proyectos: " + response.message());
                    Toast.makeText(NtfProyectos.this, "Error al obtener los proyectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProyectoNot>> call, Throwable t) {
                Log.e("NtfProyectos", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(NtfProyectos.this, "Error al cargar los proyectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarProyectos(List<ProyectoNot> proyectos) {
        Log.d("NtfProyectos", "Mostrar proyectos: " + proyectos);
        if (proyectos != null && !proyectos.isEmpty()) {
            adapter = new NotiProyectoAdapter(proyectos, projectId -> {
                // Guardar el ID del proyecto en SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectID", projectId);
                editor.apply();

                // Iniciar la actividad InformacionProyecto
                Intent intent = new Intent(NtfProyectos.this, InformacionProyecto.class);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
            Log.d("NtfProyectos", "Adapter configurado.");
        } else {
            Log.e("NtfProyectos", "Lista de proyectos es nula o está vacía.");
            Toast.makeText(this, "No hay notificaciones", Toast.LENGTH_SHORT).show();
        }
    }
}