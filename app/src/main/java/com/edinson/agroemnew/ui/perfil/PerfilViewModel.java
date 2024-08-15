        package com.edinson.agroemnew.ui.perfil;

        import android.app.Application;
        import android.content.SharedPreferences;
        import android.util.Log;

        import androidx.annotation.NonNull;
        import androidx.lifecycle.AndroidViewModel;
        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;

        import com.edinson.agroemnew.modelApi.ApiLogin;
        import com.edinson.agroemnew.modelApi.ApiService;
        import com.edinson.agroemnew.modelApi.UserDetails;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        public class PerfilViewModel extends AndroidViewModel {
            private final MutableLiveData<UserDetails.Sub> _userData = new MutableLiveData<>();
            private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
            private final SharedPreferences sharedPreferences;


            public PerfilViewModel(@NonNull Application application) {
                super(application);
                sharedPreferences = application.getSharedPreferences("MyApp", Application.MODE_PRIVATE);
            }

            public LiveData<UserDetails.Sub> getUserData() {
                return _userData;
            }

            public LiveData<String> getErrorMessage() {
                return _errorMessage;
            }

            public void updateUserInterface(boolean forceUpdate) {
                fetchUserDetails();



            }

            private void fetchUserDetails() {
                String token = sharedPreferences.getString("UserToken", null);

                if (token == null) {
                    _errorMessage.setValue("Token no encontrado");
                    return;
                }

                ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
                Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);

                call.enqueue(new Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserDetails.Sub sub = response.body().getSub();
                            if (sub != null) {
                                _userData.setValue(sub);
                                Log.d("PerfilViewModel", "Datos actualizados desde API: " + sub.toString());
                            } else {
                                _errorMessage.setValue("Datos de usuario no disponibles");
                            }
                        } else {
                            _errorMessage.setValue("Error al obtener detalles de usuario: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        _errorMessage.setValue("Falló la conexión: " + t.getMessage());
                    }
                });
            }

        }