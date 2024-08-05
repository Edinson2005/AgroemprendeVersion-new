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
            private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutos en milisegundos

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
                long lastUpdateTime = sharedPreferences.getLong("LastUpdateTime", 0);
                boolean shouldFetchFromApi = forceUpdate || System.currentTimeMillis() - lastUpdateTime > CACHE_DURATION;
                if (shouldFetchFromApi) {
                    fetchUserDetails();
                } else {
                    loadDataFromSharedPreferences();
                }
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
                                updateSharedPreferences(sub);
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

            private void loadDataFromSharedPreferences() {
                String userName = sharedPreferences.getString("UserName", "");
                String userEmail = sharedPreferences.getString("UserEmail", "");
                String userPhone = sharedPreferences.getString("UserPhone", "");
                String userNumIdentificacion = sharedPreferences.getString("UserNumIdentificacion", "");
                String userBirthDate = sharedPreferences.getString("UserBirthDate", "");

                UserDetails.Sub sub = new UserDetails.Sub();
                // Configura el objeto 'sub' con los datos en caché
                sub.setNombre(userName);
                sub.setEmail(userEmail);
                sub.setTelefono(userPhone);
                sub.setNumIdentificacion(userNumIdentificacion);
                sub.setFechaNacimiento(userBirthDate);

                _userData.setValue(sub);
            }

            private void updateSharedPreferences(UserDetails.Sub sub) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserName", sub.getNombre());
                editor.putString("UserEmail", sub.getEmail());
                editor.putString("UserPhone", sub.getTelefono());
                editor.putString("UserNumIdentificacion", sub.getNumIdentificacion());
                editor.putString("UserBirthDate", sub.getFechaNacimiento());
                editor.putLong("LastUpdateTime", System.currentTimeMillis());
                editor.apply();
            }
        }