package com.example.testapp.api


import com.example.testapp.api.models.ResponseLogin
import com.example.testapp.models.ProductModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {
    /* Login */
    @POST("/api/v1/authenticate")
    @Headers("Content-Type: application/json")
    fun login(@Body map: HashMap<String, Any>?): Call<ResponseLogin>

    /*Products*/
    @GET("/api/v1/products")
    fun getProductsList(): Call<List<ProductModel>>

    /*Product*/
    @GET("/api/v1/products/{id}")
    fun getProduct(
        @Path("id") id: Int
    ): Call<ProductModel>

}