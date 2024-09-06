package com.edinson.agroemnew;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

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

        // Añadir los datos al gráfico
        addDataToPieChart(pieChart);
    }

    private void setupPieChart(PieChart pieChart) {
        pieChart.setCenterText("Proyectos Completados");
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.getDescription().setEnabled(false);
    }

    private void addDataToPieChart(PieChart pieChart) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Datos estáticos para ilustrar
        entries.add(new PieEntry(40f, "Proyectos Completados"));
        entries.add(new PieEntry(30f, "Proyectos en Proceso"));
        entries.add(new PieEntry(20f, "Proyectos Pendientes"));
        entries.add(new PieEntry(10f, "Proyectos Cancelados"));

        PieDataSet dataSet = new PieDataSet(entries, "Proyectos");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate(); // Actualiza el gráfico
    }
}
