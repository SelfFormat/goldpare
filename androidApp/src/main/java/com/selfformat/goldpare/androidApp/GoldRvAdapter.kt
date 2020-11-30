package com.selfformat.goldpare.androidApp

import com.selfformat.goldpare.shared.model.GoldItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoldRvAdapter(var goldItems: List<GoldItem>) : RecyclerView.Adapter<GoldRvAdapter.GoldViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoldViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gold, parent, false)
            .run(::GoldViewHolder)
    }

    override fun getItemCount(): Int = goldItems.count()

    override fun onBindViewHolder(holder: GoldViewHolder, position: Int) {
        holder.bindData(goldItems[position])
    }

    inner class GoldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val priceTextView = itemView.findViewById<TextView>(R.id.price)
        private val titleTextView = itemView.findViewById<TextView>(R.id.title)
        private val websiteTextView = itemView.findViewById<TextView>(R.id.website)
        private val linkTextView = itemView.findViewById<TextView>(R.id.link)

        fun bindData(goldItem: GoldItem) {
            priceTextView.text = goldItem.price
            titleTextView.text = goldItem.title
            websiteTextView.text = goldItem.website
            linkTextView.text = goldItem.link
        }
    }
}
