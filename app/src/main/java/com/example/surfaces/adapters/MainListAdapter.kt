package com.example.surfaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surfaces.R
import com.example.surfaces.models.SurfaceModel
import com.example.surfaces.models.VenueModel
import java.lang.IllegalStateException

/**
 * This class takes care of the main list. It behaves like a sectioned list adapter.
 */
class MainListAdapter(
    val context: Context,
    val list: List<Any>,
    val listener: SurfaceSelectListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    val SECTION_HEADER = 1
    val SECTION_ITEM = 2


    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val model = list[position];
        return if (model is VenueModel) SECTION_HEADER else SECTION_ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RecyclerView.ViewHolder
        when (viewType) {
            SECTION_HEADER -> {
                view = SectionHeader(layoutInflater.inflate(R.layout.section_header, parent, false))
            }
            SECTION_ITEM -> {
                view = SectionItem(layoutInflater.inflate(R.layout.section_item, parent, false))
            }
            else -> {
                throw IllegalStateException("Handle this new case ${viewType}")
            }
        }
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (model is VenueModel && holder is SectionHeader) {
            holder.title.setText(model.name)
        } else if (model is SurfaceModel && holder is SectionItem) {
            holder.image.setImageResource(model.getImage())
            holder.subHeading.setText(model.ip)
            holder.title.setText(model.name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SectionHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title);
    }

    inner class SectionItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title);
        val image: ImageView = itemView.findViewById(R.id.image_view);
        val subHeading: TextView = itemView.findViewById(R.id.sub_heading);

        init {
            itemView.setOnClickListener {
                (list[adapterPosition] as? SurfaceModel)?.let {
                    val subList = list.subList(0, adapterPosition).findLast { it is VenueModel }

                    listener.onSurfaceSelected(it, (subList as? VenueModel)?.name)
                }
            }
        }
    }

    interface SurfaceSelectListener {
        fun onSurfaceSelected(item: SurfaceModel?, venueName: String?)
    }
}