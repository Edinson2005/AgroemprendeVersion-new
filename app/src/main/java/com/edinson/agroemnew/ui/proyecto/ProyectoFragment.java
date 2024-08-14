package com.edinson.agroemnew.ui.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.InformacionProyecto;
import com.edinson.agroemnew.databinding.FragmentProyectoBinding;
import com.edinson.agroemnew.modelApi.ApiLogin;
import com.edinson.agroemnew.modelApi.ApiService;
import com.edinson.agroemnew.modelApi.Proyecto;
import com.edinson.agroemnew.modelApi.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyectoFragment extends Fragment {

    private ProyectoViewModel proyectoViewModel;
    private FragmentProyectoBinding binding;
    private RecyclerView recyclerView;
    private ProyectoAdapter adapter;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProyectoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewProyectos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyApp", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            loadProyectosFromProfile("Bearer " + token);
        } else {
            Toast.makeText(getContext(), "Token no encontrado", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void loadProyectosFromProfile(String token) {
        apiService.getUserDetails(token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDetails userDetails = response.body();
                    if (userDetails.getSub() != null && userDetails.getSub().getProyectos() != null) {
                        List<Proyecto> proyectos = userDetails.getSub().getProyectos();
                        adapter = new ProyectoAdapter(proyectos, projectId -> {

                            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyApp", getContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("SelectID", projectId);
                            editor.apply();

                            Intent intent = new Intent(getContext(), InformacionProyecto.class);
                            startActivity(intent);

                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "No se encontraron proyectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar el perfil: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
