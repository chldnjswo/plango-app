package com.plango.app.ui.generate

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.databinding.ItemDestinationAbroadBinding

class AbroadAdapter (
    private val context: Context,
    private val items: List<AbroadItem>,
    private val onSelect: (AbroadItem) -> Unit
) : RecyclerView.Adapter<AbroadAdapter.AbroadViewHolder>() {

    inner class AbroadViewHolder(private val binding: ItemDestinationAbroadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AbroadItem) {
            val btn = binding.btnAbroad
            btn.text = item.destination

            val colorSelectedBg = ContextCompat.getColor(context, R.color.button_primary)
            val colorUnselectedBg = ContextCompat.getColor(context, R.color.card_background)
            val colorStroke = ContextCompat.getColor(context, R.color.card_border)
            val colorSelectedText = ContextCompat.getColor(context, android.R.color.white)
            val colorUnselectedText = ContextCompat.getColor(context, R.color.text_primary)

            if (item.isSelected) {
                btn.backgroundTintList = ColorStateList.valueOf(colorSelectedBg)
                btn.strokeWidth = 0
                btn.setTextColor(colorSelectedText)
                btn.animate().scaleX(1.08f).scaleY(1.08f).setDuration(120).start()
            } else {
                btn.backgroundTintList = ColorStateList.valueOf(colorUnselectedBg)
                btn.strokeColor = ColorStateList.valueOf(colorStroke)
                btn.strokeWidth = 2
                btn.setTextColor(colorUnselectedText)
                btn.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            }

            btn.setOnClickListener { onSelect(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbroadViewHolder {
        val binding = ItemDestinationAbroadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AbroadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AbroadViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}