package com.edinson.agroemnew.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ProyectoNot;

import java.util.List;
public class NotiProyectoAdapter   extends RecyclerView.Adapter<NotiProyectoAdapter.ViewHolder> {

    private List<ProyectoNot> notificaciones;

    public NotiProyectoAdapter(List<ProyectoNot> notificaciones) {
        this.notificaciones = notificaciones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifproyecto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProyectoNot notificacion = notificaciones.get(position);

        if (holder.titleTextView != null) {
            holder.titleTextView.setText(notificacion.getTitle()); // Corregido
        } else {
            // Aquí puedes agregar un log si es necesario
        }

        if (holder.bodyTextView != null) {
            holder.bodyTextView.setText(notificacion.getBody());
        } else {
            // Aquí puedes agregar un log si es necesario
        }
        if (holder.urlTextView != null){
            holder.urlTextView.setText(notificacion.getUrl());
        }
        if (holder.estadoTextView != null){
            holder.estadoTextView.setText(notificacion.getEstado());
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView bodyTextView;
        TextView urlTextView;
        TextView estadoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
        }
    }
}