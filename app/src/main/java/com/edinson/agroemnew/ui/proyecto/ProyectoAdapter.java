package com.edinson.agroemnew.ui.proyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.Proyecto;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ViewHolder> {
    private List<Proyecto> proyectos;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String projectId);
    }

    public ProyectoAdapter(List<Proyecto> proyectos, OnItemClickListener onItemClickListener) {
        this.proyectos = proyectos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proyecto proyecto = proyectos.get(position);
        holder.tituloTextView.setText(proyecto.getTitulo());
        holder.fechaTextView.setText(proyecto.getFecha());
        holder.estadoTextView.setText(proyecto.getEstado());
        holder.descripcionTextView.setText(proyecto.getDescripcion());

        // Configura el clic
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(proyecto.get_id()));
    }

    @Override
    public int getItemCount() {
        return proyectos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView, fechaTextView, estadoTextView, descripcionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
        }
    }
}
