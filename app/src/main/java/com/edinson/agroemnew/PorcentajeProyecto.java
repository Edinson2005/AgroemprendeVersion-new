package com.edinson.agroemnew;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.proyecto.ProyectoDetails;
import com.edinson.agroemnew.modelApi.proyecto.Revision;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PorcentajeProyecto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_porcentaje_proyectos);

        //ocultar el action bar de la vista

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Referencia al PieChart
        PieChart pieChart = findViewById(R.id.pieChartProyectos);

        // Configuración inicial del gráfico
        setupPieChart(pieChart);

        //obtener el ID del proyecto
        String projectId = getSharedPreferences("MyApp", MODE_PRIVATE)
                .getString("SelectID", null);

        if(projectId != null){
            loadProjectDetails(projectId, pieChart);
        } else{
            Toast.makeText(this,"ID de proyecto no ecnotrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupPieChart(PieChart pieChart) {

        //detectar el modo oscuro o claro
        int currenNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int centerTextColor;
        int holeColor;

        if(currenNightMode == Configuration.UI_MODE_NIGHT_YES){
            //modo oscuro
            centerTextColor = Color.WHITE;
            holeColor = Color.BLACK;
        } else{
            //mod claro
            centerTextColor = Color.BLACK;
            holeColor = Color.WHITE;
        }

        pieChart.setCenterText("Revisiones del Proyecto");
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holeColor);

        //configuracion de legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true); // Habilita la leyenda
        legend.setTextSize(16f);
        legend.setTextColor(centerTextColor);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    private void loadProjectDetails(String projectId, PieChart pieChart) {
        //ontener el token
        String token = getSharedPreferences("MyApp", MODE_PRIVATE)
                .getString("UserToken", null);
        if (token != null) {
            //crear instancia del servidio API
            ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

            //hacer el llamdo a la api
            Call<ProyectoDetails> call = apiService.getProyectoDetails("Bearer " + token, projectId);
            call.enqueue(new Callback<ProyectoDetails>() {
                @Override
                public void onResponse(Call<ProyectoDetails> call, Response<ProyectoDetails> response) {
                    if (response.isSuccessful()) {
                        ProyectoDetails proyecto = response.body();
                        if (proyecto != null && proyecto.getRevisiones() != null) {
                            //procesar revisiones y dividir en aprobadas y rechazadas
                            processRevisiones(proyecto.getRevisiones(), pieChart);
                        } else {
                            Toast.makeText(PorcentajeProyecto.this, "No se encontraron revisiones", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PorcentajeProyecto.this, "Error al cargar los detalles", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProyectoDetails> call, Throwable t) {
                    Toast.makeText(PorcentajeProyecto.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontro el token", Toast.LENGTH_SHORT).show();
        }
    }
    private void processRevisiones(List<Revision> revisions, PieChart pieChart) {
        int aprobadas = 0;
        int rechazadas = 0;

        for (Revision revision : revisions) {
            // Asume que el estado de las revisiones es aprobado o rechazado
            if ("APROBADO".equalsIgnoreCase(revision.getEstado())) {
                aprobadas++;
            } else if ("DESAPROBADO".equalsIgnoreCase(revision.getEstado())) {
                rechazadas++;
            }
        }

        int total = aprobadas + rechazadas;
        float porcentajeAprobadas = (total > 0) ? (aprobadas * 100f / total) : 0f;

        // Configuración de las entradas del PieChart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(aprobadas, "Completado"));
        entries.add(new PieEntry(rechazadas, "Falta"));

        // Definir colores personalizados para las secciones
        PieDataSet dataSet = new PieDataSet(entries, "Revisiones");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(90f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.revisadoproyect));  // Verde para "Completado"
        colors.add(Color.RED);    // Rojo para "Falta"
        dataSet.setColors(colors);  // Aplicar los colores

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        // Actualizar el texto del centro según el porcentaje de aprobadas
        if (porcentajeAprobadas == 100f) {
            pieChart.setCenterText("Proyecto Completado");
        } else {
            pieChart.setCenterText(String.format("En progreso: %.2f%%", porcentajeAprobadas));
        }

        // Detectar el modo oscuro o claro para actualizar los colores
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int centerTextColor;
        int holecolor;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            centerTextColor = Color.WHITE;
            holecolor = Color.BLACK;
        } else {
            centerTextColor = Color.BLACK;
            holecolor = Color.WHITE;
        }
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holecolor);
        pieChart.invalidate();
    }

}
