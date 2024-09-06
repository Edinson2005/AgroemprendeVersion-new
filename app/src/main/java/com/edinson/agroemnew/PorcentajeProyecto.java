package com.edinson.agroemnew;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.ProyectoDetails;
import com.edinson.agroemnew.modelApi.Revision;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
        pieChart.setCenterText("Revisiones del Proyecto");
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.getDescription().setEnabled(false);
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
    private void processRevisiones(List<Revision> revisions, PieChart pieChart){
        int aprobadas = 0;
        int rechazadas =0;

        for(Revision revision : revisions){
            //asume que el estado de las revisiones es aprobado o rechazado
            if("APROBADO".equalsIgnoreCase(revision.getEstado())){
                aprobadas++;
            } else if ("DESAPROBADO".equalsIgnoreCase(revision.getEstado())) {
                rechazadas++;
            }
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(aprobadas, "APROBADO"));
        entries.add(new PieEntry(rechazadas, "DESAPROBADO"));

        PieDataSet dataSet = new PieDataSet(entries, "Revisiones");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}
