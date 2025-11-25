package com.plango.app.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.databinding.ItemTravelSummaryBinding
import com.plango.app.data.travel.TravelSummaryResponse
import java.text.SimpleDateFormat
import java.util.*

class TravelSummaryAdapter(
    private val onClick: (TravelSummaryResponse) -> Unit,
    private val onDelete: (TravelSummaryResponse) -> Unit
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

            binding.tvDDay.visibility = View.GONE
            binding.root.setCardBackgroundColor(Color.WHITE)

            binding.btnDelete.setOnClickListener {
                onDelete(item)
            }

            // 기본은 삭제 버튼 보임
            binding.btnDelete.visibility = View.VISIBLE

            when (type) {
                "finished" -> {
                    // 지난 여행은 삭제 버튼 숨김
                    binding.btnDelete.visibility = View.GONE

                    // END도 숨기기 → 요청사항 반영
                    binding.tvDDay.visibility = View.GONE
                }

                "ongoing" -> {
                    binding.tvDDay.visibility = View.VISIBLE
                    binding.tvDDay.text = "D-Day"
                    binding.tvDDay.setBackgroundResource(R.drawable.d_day_green_bg)
                }

                "upcoming" -> {
                    binding.tvDDay.visibility = View.VISIBLE
                    binding.tvDDay.text = calculateDDay(item.startDate)
                    binding.tvDDay.setBackgroundResource(R.drawable.d_day_bg)
                }
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
