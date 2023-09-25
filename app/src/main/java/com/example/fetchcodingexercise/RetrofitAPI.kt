package com.example.fetchcodingexercise

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("hiring.json")
    fun getEntries(): Call<ArrayList<Entry>>
}
