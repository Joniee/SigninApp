package com.example.signinapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.signinapp.utils.DailyReport

class HistoryAdapter(private val reports: List<DailyReport>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val container: LinearLayout = view.findViewById(R.id.llSessionsContainer)
        // Eliminamos tvTotalDay si no quieres calcular totales por ahora
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_day, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val report = reports[position]
        val context = holder.itemView.context

        // 1. Poner Fecha
        holder.tvDate.text = report.date

        // 2. Limpiar lista anterior
        holder.container.removeAllViews()

        // 3. Crear una fila bonita por cada fichaje
        report.events.forEach { log ->

            // AQUI ESTA EL TRUCO: "Inflamos" tu diseño bonito
            val rowView = LayoutInflater.from(context).inflate(R.layout.item_history_row, holder.container, false)

            // Enlazamos los textos de ESA fila concreta
            val tvType = rowView.findViewById<TextView>(R.id.tvType)
            val tvTime = rowView.findViewById<TextView>(R.id.tvTime)

            // Ponemos los datos
            tvType.text = log.type
            tvTime.text = log.time // Ej: "08:30:00"

            // Colores (Verde entrada, Rojo salida)
            if (log.type.equals("Entrada", ignoreCase = true)) {
                val green = Color.parseColor("#388E3C")
                tvType.setTextColor(green)
                tvTime.setTextColor(green)
            } else if (log.type.equals("Salida", ignoreCase = true)) {
                val red = Color.parseColor("#D32F2F")
                tvType.setTextColor(red)
                tvTime.setTextColor(red)
            }

            // Añadimos la fila terminada a la tarjeta
            holder.container.addView(rowView)
        }
    }

    override fun getItemCount() = reports.size
}

