package com.plango.app.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.databinding.ItemTravelSummaryBinding
import com.plango.app.data.travel.TravelSummaryResponse
import java.text.SimpleDateFormat
import java.util.*

class TravelSummaryAdapter(
    private val onClick: (TravelSummaryResponse) -> Unit
) : RecyclerView.Adapter<TravelSummaryAdapter.ViewHolder>() {

    private val items = mutableListOf<Pair<String, TravelSummaryResponse>>()

    fun setData(
        ongoing: List<TravelSummaryResponse>,
        upcoming: List<TravelSummaryResponse>,
        finished: List<TravelSummaryResponse>
    ) {
        items.clear()
        ongoing.forEach { items.add("ongoing" to it) }
        upcoming.forEach { items.add("upcoming" to it) }
        finished.forEach { items.add("finished" to it) }
        notifyDataSetChanged()
    }

    private fun calculateDDay(startDateString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val startDate = sdf.parse(startDateString) ?: return ""

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val startCal = Calendar.getInstance().apply {
                time = startDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val diff = (startCal.timeInMillis - today.timeInMillis) / (24 * 60 * 60 * 1000)

            when {
                diff > 0 -> "D-$diff"
                diff == 0L -> "D-Day"
                else -> "END"
            }
        } catch (e: Exception) {
            ""
        }
    }

    inner class ViewHolder(private val binding: ItemTravelSummaryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(type: String, item: TravelSummaryResponse) {

            binding.tvDestination.text = item.travelDest
            binding.tvDate.text = "${item.startDate} ~ ${item.endDate}"

            // 초기화
            binding.tvOngoingLabel.visibility = View.GONE
            binding.tvDDay.visibility = View.GONE
            binding.root.setCardBackgroundColor(Color.WHITE)

            when (type) {

                "ongoing" -> {
                    binding.tvOngoingLabel.visibility = View.VISIBLE
                    binding.root.setCardBackgroundColor(Color.parseColor("#E8F8F2"))
                }

                "upcoming" -> {
                    binding.tvDDay.visibility = View.VISIBLE
                    binding.tvDDay.text = calculateDDay(item.startDate)
                }

                "finished" -> {}
            }

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTravelSummaryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (type, item) = items[position]
        holder.bind(type, item)
    }
}
