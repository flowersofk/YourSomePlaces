package com.flowersofk.yoursomeplaces.api

import android.util.Log
import com.flowersofk.yoursomeplaces.utils.Constants
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.network.SearchPlaceResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchPlaceService {

    @GET("/App/json/{page}.json")
    fun getSearchPlace(
    @Path("page") page: Int
    ) : Call<SearchPlaceResponse>

    companion object {

        fun create(): SearchPlaceService {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(SearchPlaceService::class.java)

        }

    }

}

/**
 * 장소 API 호출 정의
 */
fun getSearchPlace(
    service: SearchPlaceService,
    page: Int,
    onSuccess: (count: Int, response: List<PlaceProduct>) -> Unit,
    onFailure: (error: String) -> Unit) {

    service.getSearchPlace(page).enqueue(
        object : Callback<SearchPlaceResponse> {
            override fun onFailure(call: Call<SearchPlaceResponse>, t: Throwable) {

                onFailure(t.message ?: "unknown error")

            }

            override fun onResponse(call: Call<SearchPlaceResponse>, @NotNull response: Response<SearchPlaceResponse>) {

                if(response.isSuccessful && response.body()?.code == 200) {

                    val body = response.body()
                    if(body != null) {
                        onSuccess(body.data.totalCount, body.data.product)
                    } else {
                        onFailure("Data Empty")
                    }

                } else {

                    onFailure(response.errorBody()?.string() ?: "Unknown error")

                }

            }

        })

}