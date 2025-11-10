package com.plango.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.data.travel.Trip

class TripAdapter(private val tripList: List<Trip>) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    // ViewHolder: item_trip.xml 내부의 뷰를 연결
    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val tvTripTitle: TextView = itemView.findViewById(R.id.tvTripTitle)
        val tvTripDate: TextView = itemView.findViewById(R.id.tvTripDate)
    }

    // 아이템 레이아웃 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.tvTripTitle.text = trip.title
        holder.tvTripDate.text = trip.date
        holder.imgThumbnail.setImageResource(trip.imageResId)
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = tripList.size
}
