package com.plango.app.ui.generate

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.databinding.ItemCompaniontypeBinding

class CompanionAdapter(
    private val context: Context,
    private val items: List<CompanionItem>,
    private val onSelect: (CompanionItem) -> Unit
) : RecyclerView.Adapter<CompanionAdapter.CompanionViewHolder>() {

    inner class CompanionViewHolder(private val binding: ItemCompaniontypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompanionItem) {
            val btn = binding.btnCompanion
            btn.text = when (item.companionType) {
                CompanionItem.CompanionType.SOLO -> "혼자"
                CompanionItem.CompanionType.COUPLE -> "연인"
                CompanionItem.CompanionType.FAMILY -> "가족"
                CompanionItem.CompanionType.FRIEND -> "친구"
            }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanionViewHolder {
        val binding = ItemCompaniontypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompanionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompanionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
