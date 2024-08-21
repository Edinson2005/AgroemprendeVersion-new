package com.edinson.agroemnew.modelApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import java.util.List;
import retrofit2.http.Path;

public interface ApiService {

    ////Usuario
    @FormUrlEncoded
    @POST("auth/login")
    Call<Token> getlogin(
            @Field("email") String email,
            @Field("contrasena") String contrasena);

    @POST("auth/usuario")
    Call<Void> registerUser (@Body RegisterRequest registerRequest);
    @GET("auth/profile")
    Call<UserDetails> getUserDetails(@Header("Authorization") String token);

    @HTTP(method = "PATCH", path = "auth/usuario/{userId}", hasBody = true)
    Call<Void> updateUserProfile(
            @Path("userId") String userId, // Usa "userId" para coincidir con el parámetro en la URL
            @Header("Authorization") String token,
            @Body UserUpdate userUpdateRequest
    );
    //RECUPERAR CONTRASEÑA
    @POST ("auth/forgot-password")
    Call<Forgot> forgotPassword(@Body Forgot forgot);


    @GET("proyectos/{id}")
    Call<ProyectoDetails> getProyectoDetails(@Header("Authorization") String token, @Path("id") String id);

    @GET("/proyectos/mis-proyectos")
    Call<List<Proyecto>> getProyecto(@Header("Authorization") String token);





    // Métodos para Notificaciones
    @GET("notificaciones/convocatoria")
    Call<List<Notificacion>> getNotificaciones(@Header("Authorization") String token);

    @GET("notificaciones/{id}")
    Call<Notificacion> getNotificacionById(
            @Path("id") String id,
            @Header("Authorization") String token
    );

    @POST("notificaciones")
    Call<Notificacion> createNotificacion(
            @Body Notificacion notificacion,
            @Header("Authorization") String token
    );



    @GET("/notificaciones/mis-proyectos")
    Call<List<ProyectoNot>> getProyectos(@Header("Authorization") String token);

    @GET("/notificaciones/mis-proyectos/{id}")
    Call<ProyectoNot> getProyectoById(@Header("Authorization") String token, @Path("id") String id);
}
