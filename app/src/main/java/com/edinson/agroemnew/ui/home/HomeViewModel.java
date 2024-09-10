package com.edinson.agroemnew.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edinson.agroemnew.modelApi.proyecto.Proyecto;

import java.util.List;

public class HomeViewModel extends ViewModel {


    private final MutableLiveData<List<Proyecto>> projectListLiveData;
    private final MutableLiveData<String> errorMessage;

    public HomeViewModel() {
        projectListLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    // Observa la lista de proyectos
    public LiveData<List<Proyecto>> getProjectList() {
        return projectListLiveData;
    }

    // Observa el mensaje de error
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Establece la lista de proyectos
    public void setProjectList(List<Proyecto> projects) {
        projectListLiveData.setValue(projects);
    }

    // Establece el mensaje de error
    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }
}