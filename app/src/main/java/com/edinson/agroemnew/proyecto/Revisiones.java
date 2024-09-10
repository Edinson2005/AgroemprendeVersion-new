package com.edinson.agroemnew.proyecto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.adapters.RevisionAdapter;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.proyecto.ProyectoDetails;
import com.edinson.agroemnew.modelApi.proyecto.Revision;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Revisiones extends AppCompatActivity {
    private RecyclerView revisionesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revisiones);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        revisionesRecyclerView = findViewById(R.id.revisionesRecyclerView);
        revisionesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String projectId = getIntent().getStringExtra("projectId");
        if (projectId != null) {
            loadProjectDetails(projectId);
        } else {
            Toast.makeText(this, "ID del proyecto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProjectDetails(String projectId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

            Call<ProyectoDetails> call = apiService.getProyectoDetails("Bearer " + token, projectId);
            call.enqueue(new Callback<ProyectoDetails>() {
                @Override
                public void onResponse(Call<ProyectoDetails> call, Response<ProyectoDetails> response) {
                    if (response.isSuccessful()) {
                        ProyectoDetails proyecto = response.body();
                        if (proyecto != null) {
                            setupRevisionesRecyclerView(proyecto.getRevisiones());
                        } else {
                            Toast.makeText(Revisiones.this, "No se encontraron detalles del proyecto", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Revisiones.this, "Error al cargar los detalles del proyecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProyectoDetails> call, Throwable t) {
                    Toast.makeText(Revisiones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontró el token de autorización", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRevisionesRecyclerView(List<Revision> revisiones) {
        if(revisiones == null || revisiones.isEmpty()){
            Toast.makeText(this, "No hay revisiones disponibles", Toast.LENGTH_SHORT).show();

        }else{
            RevisionAdapter revisionAdapter = new RevisionAdapter(revisiones);
            revisionesRecyclerView.setAdapter(revisionAdapter);
        }

    }
}