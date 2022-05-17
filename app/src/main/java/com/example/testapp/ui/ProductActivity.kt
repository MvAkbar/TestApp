package com.example.testapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.adapters.ProductAdapter
import com.example.testapp.api.ApiClient
import com.example.testapp.api.ApiService
import com.example.testapp.models.ProductModel
import com.example.testapp.utils.TokenManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product.*
import retrofit2.Call
import retrofit2.Callback
import java.net.URL

class ProductActivity : AppCompatActivity() {
    private lateinit var service: ApiService
    private lateinit var callProduct: Call<ProductModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        service = ApiClient.createServiceWithAuth(ApiService::class.java, applicationContext)
        getProduct()
    }

    fun getProduct() {
        val productId: Int = intent.getIntExtra("productId", 0)
        callProduct = service.getProduct(productId)
        Log.d("Url", callProduct.request().url.toString())

        callProduct.enqueue(object : Callback<ProductModel> {
            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                Log.d("error", t.message.toString())
                if (callProduct.isCanceled) {
                    return
                }
            }

            override fun onResponse(
                call: Call<ProductModel>,
                response: retrofit2.Response<ProductModel>
            ) {
                Log.d("Succse Product", response.message().toString())
                if (response.isSuccessful) {
                    setData(response.body()!!)
                } else if (response.message().toString().toUpperCase().equals("NOT FOUND")) {
                    Toast.makeText(
                        applicationContext,
                        "Product not found",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.message().toString().toUpperCase().equals("BAD_REQUEST")) {
                    Toast.makeText(
                        applicationContext,
                        "Token should not be empty",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                } else if (response.message().toString().toUpperCase().equals("UNAUTHORIZED")) {
                    Toast.makeText(applicationContext, "Customer not found", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })
    }

    fun setData(product: ProductModel) {
        Picasso.get().load(product.photoUrl)
            .into(productImage)
        productName.text = product.name;
        discription.text = product.description;
        productPrice.text = product.price.toString();


    }
}