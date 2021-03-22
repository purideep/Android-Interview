package com.example.surfaces.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surfaces.R
import com.example.surfaces.models.TabModel

class TabsAdapter(context: Context, val list: List<TabModel>, val listener: TabSelectListener) :
    RecyclerView.Adapter<TabsAdapter.TabView>() {
    val layoutInflater = LayoutInflater.from(context)

    interface TabSelectListener {
        fun onTabSelected(tabModel: TabModel)
    }

    inner class TabView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener {
                listener.onTabSelected(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabView {
        return TabView(layoutInflater.inflate(R.layout.tab_item, parent, false))
    }

    override fun onBindViewHolder(holder: TabView, position: Int) {
        val model = list[position]
        holder.title.setText(model.name)
        holder.title.setTextColor(if (model.isSelected) Color.BLACK else Color.GRAY)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}