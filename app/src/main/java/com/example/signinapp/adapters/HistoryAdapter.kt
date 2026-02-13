package com.example.signinapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.signinapp.models.AccessLog

class HistoryAdapter(private var logs: List<AccessLog> = mutableListOf()) :
    RecyclerView.Adapter<HistoryAdapter.LogViewHolder>() {

    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // AJUSTA ESTOS IDs a los que tengas en item_history_day.xml
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvType: TextView = view.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        // Usamos TU layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_day, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]

        // Asignación directa sin cálculos
        holder.tvDate.text = log.date
        holder.tvTime.text = log.time
        holder.tvType.text = log.type
    }

    override fun getItemCount(): Int = logs.size

    fun updateData(newLogs: List<AccessLog>) {
        this.logs = newLogs
        notifyDataSetChanged()
    }
}