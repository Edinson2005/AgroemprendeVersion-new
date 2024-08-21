package com.edinson.agroemnew.ui.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.Proyecto;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {
    private List<Proyecto> proyectos;
    private Context context;

    public ProyectoAdapter(Context context, List<Proyecto> proyectos) {
        this.context = context;
        this.proyectos = proyectos;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_proyecto, parent, false);
        return new ProyectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
        Proyecto proyecto = proyectos.get(position);
        holder.tituloTextView.setText(proyecto.getTitulo());
        holder.fechaTextView.setText(proyecto.getFecha());
        holder.estadoTextView.setText(proyecto.getEstado());
        holder.descripcionTextView.setText(proyecto.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return proyectos.size();
    }

    public static class ProyectoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        TextView estadoTextView;
        TextView descripcionTextView;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
        }
    }
}