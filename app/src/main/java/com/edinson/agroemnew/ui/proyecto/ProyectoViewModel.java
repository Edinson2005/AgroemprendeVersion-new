package com.edinson.agroemnew.ui.proyecto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edinson.agroemnew.modelApi.proyecto.Project;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProyectoViewModel extends ViewModel {

    private final MutableLiveData<List<Project>> pruebaLiveData;
    private final MutableLiveData<String> errorMessage;

    public ProyectoViewModel() {
        pruebaLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Project>> getPrueba() {
        return pruebaLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setPruebas(List<Project> projects) {
        // Ordena la lista de proyectos por fecha de forma descendente (más reciente primero)
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {
                // Supongamos que getFecha() devuelve la fecha en formato "yyyy-MM-dd"
                // Aquí debes implementar el código necesario para comparar las fechas adecuadamente
                return p2.getFecha().compareTo(p1.getFecha()); // Orden descendente
            }
        });
        pruebaLiveData.setValue(projects);
    }

    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }
}