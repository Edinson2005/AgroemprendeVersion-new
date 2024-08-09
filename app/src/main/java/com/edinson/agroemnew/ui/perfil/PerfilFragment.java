package com.edinson.agroemnew.ui.perfil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.edinson.agroemnew.MainActivity;
import com.edinson.agroemnew.UsuarioEditar;
import com.edinson.agroemnew.databinding.FragmentPerfilBinding;
import com.edinson.agroemnew.modelApi.UserDetails;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private static final int EDITAR_USUARIO_REQUEST = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }

        setupObservers();
        setupListeners();
        perfilViewModel.updateUserInterface(false);
    }

    private void setupObservers() {
        perfilViewModel.getUserData().observe(getViewLifecycleOwner(), this::updateUI);
        perfilViewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showError);
    }

    private void updateUI(UserDetails.Sub userData) {
        Log.d("PerfilFragment", "Actualizando UI con datos: " + userData.toString());
        binding.tvNombre.setText(userData.getNombre() + " " + userData.getApellido());
        binding.tvEmail.setText(userData.getEmail());
        binding.tvTelefono.setText(userData.getTelefono());
        // Actualiza otros campos si es necesario
    }

    private void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupListeners() {
        binding.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UsuarioEditar.class);
            startActivityForResult(intent, EDITAR_USUARIO_REQUEST);
        });
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITAR_USUARIO_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getBooleanExtra("DATOS_ACTUALIZADOS", false)) {
                // Forzar una actualización inmediata desde la API
                perfilViewModel.updateUserInterface(true);
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        perfilViewModel.updateUserInterface(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}