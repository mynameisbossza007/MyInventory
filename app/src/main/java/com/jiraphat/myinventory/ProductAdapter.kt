package com.jiraphat.myinventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var productList: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "Price: ${product.price}"
        holder.tvQuantity.text = "Quantity: ${product.quantity}"

        holder.itemView.setOnClickListener { onItemClick(product) }
        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
    }

    override fun getItemCount(): Int = productList.size

    fun updateData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
