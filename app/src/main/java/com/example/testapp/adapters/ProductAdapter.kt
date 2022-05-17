package com.example.testapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.models.ProductModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*


class ProductAdapter(val list: List<ProductModel>, val callback: (id:Int) -> Unit)  :
    RecyclerView.Adapter<ProductAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_item, parent, false
        )
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(list[position])

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val productName = itemView.productName
        private val price = itemView.forPrice
        private val description = itemView.description
        private val btn = itemView.selectButton
        private val image = itemView.productItemImage


        init {
            itemView.selectButton.setOnClickListener {
                callback?.invoke(list[adapterPosition].id)
                Log.d("clck","see")
            }
        }

        fun bind(productModel: ProductModel) {
            productName.text = productModel.name
            price.text = productModel.description
            description.text = productModel.description
            Picasso.get().load( productModel.photoUrl)
                .into(image)
            btn.setOnClickListener {
                callback?.invoke(list[adapterPosition].id)
            }
        }
    }



}