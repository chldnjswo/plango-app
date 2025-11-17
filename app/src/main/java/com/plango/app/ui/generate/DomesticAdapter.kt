package com.plango.app.ui.generate

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.databinding.ItemDestinationDomesticBinding

class DomesticAdapter (
    private val context: Context,
    private val items: List<DomesticItem>,
    private val onSelect: (DomesticItem) -> Unit
) : RecyclerView.Adapter<DomesticAdapter.DomesticViewHolder>() {

    inner class DomesticViewHolder(private val binding: ItemDestinationDomesticBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DomesticItem) {
            val btn = binding.btnDomestic
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomesticViewHolder {
        val binding = ItemDestinationDomesticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DomesticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DomesticViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}