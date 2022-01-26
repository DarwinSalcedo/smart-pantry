package com.smart.pantry.ui.product

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.pantry.databinding.ItemProductBinding


class ProductItemRecyclerViewAdapter(
   private val callBack: (dataItem: ProductDataItem) -> Unit
) : RecyclerView.Adapter<ProductItemRecyclerViewAdapter.ViewHolder>() {


    private var values: MutableList<ProductDataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),callBack
        )

    }

    fun addData(items: List<ProductDataItem>) {
        values.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * Clears the _items data
     */
    fun clear()  =
        values.clear()

        fun getItem(position: Int) = values[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: ItemProductBinding,val callBack: (dataItem: ProductDataItem) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductDataItem) {
            Log.e("TAG", "bind: $item" )
            binding.item = item
            this.itemView.setOnClickListener {
                callBack.invoke(item)
            }
    }
    }

}