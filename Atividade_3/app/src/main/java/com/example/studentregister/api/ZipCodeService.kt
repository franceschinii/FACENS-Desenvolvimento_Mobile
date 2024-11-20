package com.example.studentregister.api

import com.example.studentregister.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ZipCodeService {
    @GET("{zip}/json/")
    fun fetchAddress(@Path("zip") zip: String): Call<Address>
}