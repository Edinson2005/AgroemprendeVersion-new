        package com.edinson.agroemnew.adapters;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import com.edinson.agroemnew.R;
        import com.edinson.agroemnew.modelApi.Revision;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;


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

                //Formatear fecha
                String fechaRevision = revision.getFechaRevision();
                String fechaFormateada = formatFecha(fechaRevision);
                holder.fechaRevisionTextView.setText(fechaFormateada);

                //Cambiar el color del fondo segun su estado (Aprovado-Rechazado)
                String estado = revision.getEstado().toLowerCase();
                Log.d("RevisionAdapter", "Estado: " + estado);

                int color;
                if (estado.equalsIgnoreCase("aprobado")) {
                    color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Aprovado);
                } else if (estado.equalsIgnoreCase("desaprobado")) {
                    color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Rechazado);
                } else if (estado.equalsIgnoreCase(" revisado con errores")) {
                    color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Error);
                } else {
                    color = ContextCompat.getColor(holder.itemView.getContext(), R.color.white);
                }


                holder.itemView.setBackgroundColor(color);
                Log.d("RevisionAdapter", "Color aplicado: " + color);
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

            public String formatFecha(String fecha){
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat formatoSalida = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
                try{
                    Date fechaDate = formatoEntrada.parse(fecha);
                    if (fechaDate != null){
                        return formatoSalida.format(fechaDate);
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
                return fecha;
            }
        }