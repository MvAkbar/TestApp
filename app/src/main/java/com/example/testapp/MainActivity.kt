package com.example.testapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.adapters.ProductAdapter
import com.example.testapp.api.ApiClient
import com.example.testapp.api.ApiService
import com.example.testapp.api.models.ResponseLogin
import com.example.testapp.models.ProductModel
import com.example.testapp.ui.ProductActivity
import com.example.testapp.utils.TokenManager
import kotlinx.android.synthetic.main.activity_logen.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var service: ApiService
    private lateinit var callProducts: Call<List<ProductModel>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service = ApiClient.createServiceWithAuth(ApiService::class.java, applicationContext)
        getProducts()
    }

    fun getProducts() {
        callProducts = service.getProductsList()
        Log.d("Url", callProducts.request().url.toString())

        callProducts.enqueue(object : Callback<List<ProductModel>> {
            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                if (callProducts.isCanceled) {
                    return
                }
                Log.d("error", t.message.toString())
//                progress.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<List<ProductModel>>,
                response: retrofit2.Response<List<ProductModel>>
            ) {
//                progress.visibility = View.GONE
                if (response.isSuccessful) {
                    setDataToRecycler(response.body()!!)
                } else if (response.message().toString().toUpperCase().equals("BAD REQUEST")) {
                    Toast.makeText(
                        applicationContext,
                        "Token should not be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.message().toString().toUpperCase().equals("UNAUTHORIZED")) {
                    Toast.makeText(applicationContext, "Customer not found", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })
    }

    fun setDataToRecycler(list: List<ProductModel>) {
        productsRecycler.layoutManager = LinearLayoutManager(this)
        productsRecycler.adapter = ProductAdapter(list, {
            val intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("productId", it)
            startActivity(intent)
        }
        )

    }
}