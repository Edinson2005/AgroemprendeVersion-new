package com.edinson.agroemnew.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.PorcentajeProyecto;
import com.edinson.agroemnew.R;
import com.edinson.agroemnew.adapters.SeccionAdapter;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.proyecto.ProyectoDetails;
import com.edinson.agroemnew.modelApi.proyecto.Seccion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacionProyecto  extends AppCompatActivity {

    private static final String TAG = "InformacionProyecto";

    private TextView tituloTextView,fechaTextView,estadoTextView,descripcionTextView;
    private ImageButton revisionesButton, porcentaje;

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
        //revisionesRecyclerView = findViewById(R.id.revisionesRecyclerView);
        revisionesButton = findViewById(R.id.revisionesButton);
        porcentaje = findViewById(R.id.porcentaje);


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
        // Configurar el OnClickListener para el botón de revisiones
        revisionesButton.setOnClickListener(v -> {
            Intent intent = new Intent(InformacionProyecto.this, Revisiones.class);
            intent.putExtra("projectId", projectId);
            startActivity(intent);
        });
        porcentaje.setOnClickListener(v -> {
            Intent intent = new Intent(InformacionProyecto.this, PorcentajeProyecto.class);
            startActivity(intent);

        });

    }

    private void loadProjectDetails(String projectId) {
        // Obtener el token de autenticación desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            // Crear instancia del servicio API
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

            // Hacer la llamada a la API
            Call<ProyectoDetails> call = apiService.getProyectoDetails("Bearer " + token, projectId);
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

        // Cambiar el color del estado
        String estado = proyecto.getEstado().toLowerCase();
        Log.d(TAG, "Estado del proyecto: " + estado);
        if (estado.contains("en progreso")) {
            estadoTextView.setTextColor(getResources().getColor(R.color.En_progreso));
        } else if (estado.contains("revisado")) {
            estadoTextView.setTextColor(getResources().getColor(R.color.revisado));
        } else {
            estadoTextView.setTextColor(getResources().getColor(R.color.black));
        }

        // Obtener la lista de secciones
        List<Seccion> secciones = proyecto.getSecciones();

        if (secciones != null) {
            // Imprimir fechas antes de ordenar
            for (Seccion seccion : secciones) {
                Log.d(TAG, "Fecha antes de ordenar: " + seccion.getFechaCreacion());
            }

            // Ordenar la lista de secciones por fecha en orden descendente
            Collections.sort(secciones, (s1, s2) -> {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date fecha1 = format.parse(s1.getFechaCreacion());
                    Date fecha2 = format.parse(s2.getFechaCreacion());
                    // Ordenar de más reciente a menos reciente
                    return fecha2.compareTo(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });

            // Imprimir fechas después de ordenar
            for (Seccion seccion : secciones) {
                Log.d(TAG, "Fecha después de ordenar: " + seccion.getFechaCreacion());
            }

            // Asignar el adaptador a RecyclerView
            SeccionAdapter adapter = new SeccionAdapter(secciones);
            seccionesRecyclerView.setAdapter(adapter);
        } else {
            // Manejar el caso en que la lista de secciones es null
            Toast.makeText(this, "No hay secciones disponibles", Toast.LENGTH_SHORT).show();
        }
    }
}