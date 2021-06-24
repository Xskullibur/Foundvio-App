package com.foundvio.setup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foundvio.databinding.TrackeeViewHolderBinding
import com.foundvio.model.Trackee

class TrackeeAdapter(
    private var trackees: List<Trackee>
) : RecyclerView.Adapter<TrackeeAdapter.TrackeeViewHolder>() {

    class TrackeeViewHolder(private val binding: TrackeeViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setTrackee(trackee: Trackee){
            this.binding.trackee = trackee
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackeeViewHolderBinding.inflate(layoutInflater)
        return TrackeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackeeViewHolder, position: Int) {
        val trackee = trackees[position]
        holder.setTrackee(trackee)
    }

    override fun getItemCount() = trackees.size

}