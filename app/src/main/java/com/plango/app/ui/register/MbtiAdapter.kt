package com.plango.app.ui.register

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.databinding.ItemMbtiBinding

class MbtiAdapter(
    private val context: Context,
    private val items: List<MbtiItem>,
    private val onSelect: (MbtiItem) -> Unit
) : RecyclerView.Adapter<MbtiAdapter.MbtiViewHolder>() {

    inner class MbtiViewHolder(private val binding: ItemMbtiBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: MbtiItem) {
            val btn = binding.btnMbti
            btn.text = item.type

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MbtiViewHolder {
        val binding = ItemMbtiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MbtiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MbtiViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
