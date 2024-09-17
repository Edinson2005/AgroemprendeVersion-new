package com.edinson.agroemnew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.edinson.agroemnew.R;
import com.edinson.agroemnew.modelApi.notificaciones.ProyectoNot;
import java.util.List;

public class NotiProyectoAdapter extends RecyclerView.Adapter<NotiProyectoAdapter.ViewHolder> {

    private List<ProyectoNot> notificaciones;
    private OnItemClickListener onItemClickListener;
    private String selectedProjectId;

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

        if (notificacion.getProyecto().getId().equals(selectedProjectId)) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.leida));
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.noLeida));
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(notificacion.getProyecto().getId());
                selectedProjectId = notificacion.getProyecto().getId();
                notifyDataSetChanged();
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
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            cardView = (CardView) itemView;
        }
    }
}
