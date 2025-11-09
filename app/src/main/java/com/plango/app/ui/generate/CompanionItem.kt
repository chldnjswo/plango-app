package com.plango.app.ui.generate

data class CompanionItem (
    val companionType: CompanionType,
    var isSelected: Boolean = false
)

{
    enum class CompanionType { SOLO, COUPLE, FAMILY, FRIEND }
}