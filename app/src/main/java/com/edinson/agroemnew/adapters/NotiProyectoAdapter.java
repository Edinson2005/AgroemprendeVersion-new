package com.edinson.agroemnew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.ProyectoNot;
import com.edinson.agroemnew.ui.proyecto.ProyectoAdapter;

import java.util.List;

public class NotiProyectoAdapter extends RecyclerView.Adapter<NotiProyectoAdapter.ViewHolder> {

    private List<ProyectoNot> notificaciones;
    private OnItemClickListener onItemClickListener;

    public NotiProyectoAdapter(List<ProyectoNot> notificaciones, OnItemClickListener onItemClickListener) {
        this.notificaciones = notificaciones;
        this.onItemClickListener = onItemClickListener;
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

        holder.titleTextView.setText(notificacion.getTitle());
        holder.bodyTextView.setText(notificacion.getBody());
        holder.estadoTextView.setText(notificacion.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(notificacion.getProyecto().getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String projectId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView bodyTextView;
        TextView estadoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
        }
    }
}