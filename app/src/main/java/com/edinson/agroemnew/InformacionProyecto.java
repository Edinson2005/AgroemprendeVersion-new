package com.edinson.agroemnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.ProyectoDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacionProyecto  extends AppCompatActivity {

    private static final String TAG = "InformacionProyecto";

    private TextView tituloTextView;
    private TextView fechaTextView;
    private TextView estadoTextView;
    private TextView descripcionTextView;
    private RecyclerView seccionesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_proyecto);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar los TextViews
        tituloTextView = findViewById(R.id.tituloTextView);
        fechaTextView = findViewById(R.id.fechaTextView);
        estadoTextView = findViewById(R.id.estadoTextView);
        descripcionTextView = findViewById(R.id.descripcioTextView);
        seccionesRecyclerView = findViewById(R.id.seccionesRecyclerView);

        // Configuración de RecyclerView
        seccionesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener el ID del proyecto desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String projectId = sharedPreferences.getString("SelectID", null);

        if (projectId != null) {
            // Llamar a la API para obtener los detalles del proyecto
            loadProjectDetails(projectId);
        } else {
            Toast.makeText(this, "ID de proyecto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProjectDetails(String projectId) {
        // Obtener el token de autenticación desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            // Crear instancia del servicio API
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

            // Hacer la llamada a la API
            Call<ProyectoDetails> call = apiService.getProjectDetails("Bearer " + token, projectId);
            call.enqueue(new Callback<ProyectoDetails>() {
                @Override
                public void onResponse(Call<ProyectoDetails> call, Response<ProyectoDetails> response) {
                    if (response.isSuccessful()) {
                        ProyectoDetails proyecto = response.body();
                        if (proyecto != null) {
                            // Actualizar la interfaz de usuario con los detalles del proyecto
                            updateUI(proyecto);
                        } else {
                            Toast.makeText(InformacionProyecto.this, "No se encontraron detalles", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InformacionProyecto.this, "Error al cargar los detalles del proyecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProyectoDetails> call, Throwable t) {
                    Toast.makeText(InformacionProyecto.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontró el token de autorización", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(ProyectoDetails proyecto) {
        // Actualizar los TextViews con la información del proyecto
        tituloTextView.setText(proyecto.getTitulo());
        fechaTextView.setText(proyecto.getFecha());
        estadoTextView.setText(proyecto.getEstado());
        descripcionTextView.setText(proyecto.getDescripcion());

        SeccionAdapter adapter = new SeccionAdapter(proyecto.getSecciones());
        seccionesRecyclerView.setAdapter(adapter);
    }
}