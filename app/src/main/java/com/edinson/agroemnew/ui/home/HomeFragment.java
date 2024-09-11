package com.edinson.agroemnew.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.proyecto.Proyecto;
import com.edinson.agroemnew.modelApi.usuario.UserDetails;
import com.edinson.agroemnew.proyecto.Notificaciones;
import com.edinson.agroemnew.proyecto.NtfProyectos;
import com.edinson.agroemnew.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }

        ImageButton imageButton = binding.btnNotificaciones;
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NtfProyectos.class);
            startActivity(intent);
        });

        Button btnOtraActividad = binding.Buttonconvocatoria;
        btnOtraActividad.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Notificaciones.class);
            startActivity(intent);
        });

        // Configuración del gráfico circular
        PieChart pieChart = binding.pieChart;
        setupPieChart(pieChart);

        // Observa el LiveData del número de proyectos
        homeViewModel.getProjectList().observe(getViewLifecycleOwner(), count -> {
            updatePieChart(pieChart, count);
        });

        // Observa el LiveData de errores
        homeViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        // Obtén el número de proyectos
        obtenerNumeroDeProyectos();

        return root;
    }

    private void setupPieChart(PieChart pieChart) {
        //Detecta el modo oscuro y claro
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int centerTextColor;
        int holeColor;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            //modo oscuro
            centerTextColor = Color.WHITE;
            holeColor = Color.BLACK;
        }else {
            //modo claro
            centerTextColor = Color.BLACK;
            holeColor = Color.WHITE;
        }
        pieChart.setCenterText("Cargando...");
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holeColor);
        pieChart.getDescription().setEnabled(false);
    }

    private void updatePieChart(PieChart pieChart, List<Proyecto> proyectos) {
        if (proyectos == null || proyectos.isEmpty()) {
            pieChart.setCenterText("Proyectos: 0");
            return;
        }

        // Contadores para cada estado
        int completado = 0;
        int resultErrores = 0;
        int revisado = 0;
        int enProgreso = 0;
        int enRevision = 0;

        // Contar proyectos según su estado
        for (Proyecto proyecto : proyectos) {
            switch (proyecto.getEstado().toLowerCase()) {
                case "completado":
                    completado++;
                    break;
                case "revisado con errores":
                    resultErrores++;
                    break;
                case "revisado":
                    revisado++;
                    break;
                case "en progreso":
                    enProgreso++;
                    break;
                case "en revision":
                    enRevision++;
                    break;
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if (completado > 0) {
            entries.add(new PieEntry(completado, "Completado"));
            colors.add(ContextCompat.getColor(requireContext(), R.color.revisadoproyect));
        }
        if (resultErrores > 0) {
            entries.add(new PieEntry(resultErrores, "Con Errores"));
            colors.add(ContextCompat.getColor(requireContext(), R.color.Error));
        }
        if (revisado > 0) {
            entries.add(new PieEntry(revisado, "Revisado"));
            colors.add(ContextCompat.getColor(requireContext(), R.color.revisado));
        }
        if (enProgreso > 0) {
            entries.add(new PieEntry(enProgreso, "En Progreso"));
            colors.add(ContextCompat.getColor(requireContext(), R.color.En_progreso));
        }
        if(enRevision > 0){
            entries.add(new PieEntry(enRevision, "En Revision"));
            colors.add(ContextCompat.getColor(requireContext(), R.color.Aprovado));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);

        pieChart.setData(data);
        pieChart.invalidate(); // Refresca el gráfico para mostrar los cambios

        // Detectar el modo oscuro y claro
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int centerTextColor;
        int holeColor;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            centerTextColor = Color.WHITE;
            holeColor = Color.BLACK;
        } else {
            centerTextColor = Color.BLACK;
            holeColor = Color.WHITE;
        }

        ///contar total de proyctos
        int totalProyectos = completado + enProgreso + revisado + resultErrores + enRevision;
        pieChart.setCenterText("Proyectos: " + totalProyectos);
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holeColor);

        // Refrescar la vista
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }


    private void obtenerNumeroDeProyectos() {
        // Recupera el token de SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", "");

        if (token.isEmpty()) {
            homeViewModel.setErrorMessage("Token de autenticación no encontrado");
            return;
        }
        // Agrega "Bearer " al token
        String authToken = "Bearer " + token;

        ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
        apiService.getUserDetails(authToken).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDetails userDetails = response.body();
                    List<Proyecto> proyectos = userDetails.getSub().getProyectos();
                    homeViewModel.setProjectList(proyectos);
                    updatePieChart(binding.pieChart, proyectos);
                } else {
                    homeViewModel.setErrorMessage("Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                homeViewModel.setErrorMessage("Error al conectar con la API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
