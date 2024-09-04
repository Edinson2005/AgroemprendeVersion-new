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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.Project;
import com.edinson.agroemnew.modelApi.Proyecto;
import com.edinson.agroemnew.modelApi.UserDetails;
import com.edinson.agroemnew.proyecto.Notificaciones;
import com.edinson.agroemnew.proyecto.NtfProyectos;
import com.edinson.agroemnew.databinding.FragmentHomeBinding;
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
        homeViewModel.getProjectCount().observe(getViewLifecycleOwner(), count -> {
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
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holeColor);
    }

    private void updatePieChart(PieChart pieChart, int projectCount) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(projectCount, "Proyectos"));

        PieDataSet dataSet = new PieDataSet(entries, "Número de Proyectos");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Usa colores predeterminados
        dataSet.setValueTextSize(16f);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresca el gráfico para mostrar los cambios


        //Detectar el modo oscuro y claro
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int centerTextColor;
        int holeColor;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            centerTextColor = Color.WHITE;
            holeColor = Color.BLACK;
        }else {
            centerTextColor = Color.BLACK;
            holeColor = Color.WHITE;
        }

        pieChart.setCenterText("Proyectos: " + projectCount);
        pieChart.setCenterTextColor(centerTextColor);
        pieChart.setHoleColor(holeColor);
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
                    int projectCount = (proyectos != null) ? proyectos.size() : 0;
                    homeViewModel.setProjectCount(projectCount);
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
