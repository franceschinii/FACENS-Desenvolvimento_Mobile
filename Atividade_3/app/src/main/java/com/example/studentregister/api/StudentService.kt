package com.example.studentregister.api

import com.example.studentregister.model.Student
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentService {
    @GET("Student")
    fun listStudents(): Call<List<Student>>

    @POST("Student")
    fun saveStudent(@Body student: Student): Call<Student>
}