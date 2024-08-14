package com.edinson.agroemnew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.Revision;

import java.util.List;

public class RevisionAdapter extends RecyclerView.Adapter<RevisionAdapter.RevisionViewHolder> {
    private List<Revision> revisiones;

    public RevisionAdapter(List<Revision> revisiones) {
        this.revisiones = revisiones;
    }

    @NonNull
    @Override
    public RevisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revision, parent, false);
        return new RevisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevisionViewHolder holder, int position) {
        Revision revision = revisiones.get(position);
        holder.estadoTextView.setText(revision.getEstado());
        holder.tituloTextView.setText(revision.getTitulo());
        holder.sugerenciaTextView.setText(revision.getSugerencia());
        holder.descripcionTextView.setText(revision.getDescripcion());
        holder.fechaRevisionTextView.setText(revision.getFechaRevision());
    }

    @Override
    public int getItemCount() {
        return revisiones.size();
    }

    public static class RevisionViewHolder extends RecyclerView.ViewHolder {
        TextView estadoTextView;
        TextView tituloTextView;
        TextView sugerenciaTextView;
        TextView descripcionTextView;
        TextView fechaRevisionTextView;

        public RevisionViewHolder(@NonNull View itemView) {
            super(itemView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            sugerenciaTextView = itemView.findViewById(R.id.sugerenciaTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            fechaRevisionTextView = itemView.findViewById(R.id.fechaRevisionTextView);
        }
    }
}