package com.edinson.agroemnew;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.ProyectoAdapter;
import com.edinson.agroemnew.modelApi.ProyectoNot;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class NtfProyectos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProyectoAdapter adapter;
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

        recyclerView = findViewById(R.id.recyclerviewntfproyectos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        token = sharedPreferences.getString("UserToken", "");

        Log.d("NtfProyectos", "Token recuperado: " + token);

        if (token.isEmpty()) {
            Log.e("NtfProyectos", "Token de autenticación no encontrado.");
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
                }
            }

            @Override
            public void onFailure(Call<List<ProyectoNot>> call, Throwable t) {
                Log.e("NtfProyectos", "Error en la solicitud: " + t.getMessage());
            }
        });
    }

    private void mostrarProyectos(List<ProyectoNot> proyectos) {
        if (proyectos != null && !proyectos.isEmpty()) {
            adapter = new ProyectoAdapter(proyectos);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("NtfProyectos", "Lista de proyectos es nula o está vacía.");
        }
    }
}