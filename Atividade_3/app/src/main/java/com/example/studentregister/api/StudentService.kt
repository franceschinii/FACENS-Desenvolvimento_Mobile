package com.example.studentregister.api

import com.example.studentregister.model.Student
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentService {
    @GET("student")
    fun listStudents(): Call<List<Student>>

    @POST("student")
    fun saveStudent(@Body student: Student): Call<Student>
}