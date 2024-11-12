package com.aigirlfriend.virtuallove.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.model.LanguageModel
import com.aigirlfriend.virtuallove.utils.LocaleHelper

class LanguageAdapter(
    private val languages: List<LanguageModel>,
    private val listener: OnLanguageSelectedListener
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private var selectedPosition = languages.indexOfFirst { it.isSelected }

    interface OnLanguageSelectedListener {
        fun onLanguageSelected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        with(holder) {
            tvLanguageName.text = language.name
            imgFlag.setImageResource(language.flagResource)
            radioButton.isChecked = position == selectedPosition

            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                val selectedLanguage = languages[selectedPosition].code
                LocaleHelper.setLocale(itemView.context, selectedLanguage)
                listener.onLanguageSelected(selectedPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = languages.size

    fun getSelectedLanguage(): String? =
        if (selectedPosition != -1) languages[selectedPosition].code else null

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFlag: ImageView = itemView.findViewById(R.id.imgFlag)
        val tvLanguageName: TextView = itemView.findViewById(R.id.tvLanguageName)
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
    }
}
