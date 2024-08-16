package com.edinson.agroemnew.modelApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;

import java.util.List;
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {

    private List<Notificacion> notificaciones;
    private Context context;

    public NotificacionAdapter(List<Notificacion> notificaciones, Context context) {
        this.notificaciones = notificaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        if (notificaciones != null && position < notificaciones.size()) {
            Notificacion notificacion = notificaciones.get(position);
            holder.titulo.setText(notificacion.getTitle());
            holder.cuerpo.setText(notificacion.getBody());
            holder.url.setText(notificacion.getUrl());
            holder.estado.setText(notificacion.getEstado());

            // Cargar estado 'vista' desde SharedPreferences
            boolean vista = cargarEstadoVista(notificacion.getId());

            // Cambiar el color del fondo según el estado 'vista'
            if (vista) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.black)); // Color cuando se ha visto
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVista)); // Color cuando no se ha visto
            }

            // Evento click para marcar la notificación como vista
            holder.itemView.setOnClickListener(v -> {
                notificacion.setVista(true);
                guardarEstadoVista(notificacion.getId(), true);
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones != null ? notificaciones.size() : 0;
    }

    private void guardarEstadoVista(String idNotificacion, boolean vista) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vista_" + idNotificacion, vista);
        editor.apply();
    }

    private boolean cargarEstadoVista(String idNotificacion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("vista_" + idNotificacion, false); // false por defecto si no existe
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView cuerpo;
        TextView url;
        TextView estado;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titleNotificacion);
            cuerpo = itemView.findViewById(R.id.bodyNotificacion);
            url = itemView.findViewById(R.id.urlNotificacion);
            estado = itemView.findViewById(R.id.estadoconvocatoria);
        }
    }
}