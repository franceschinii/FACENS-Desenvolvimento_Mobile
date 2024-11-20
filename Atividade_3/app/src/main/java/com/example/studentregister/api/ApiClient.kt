package com.example.studentregister.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val instances = mutableMapOf<String, Retrofit>()

    fun getInstance(baseUrl: String): Retrofit {
        return instances.getOrPut(baseUrl) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
