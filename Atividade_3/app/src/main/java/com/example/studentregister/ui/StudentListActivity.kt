package com.example.studentregister.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregister.api.ApiClient
import com.example.studentregister.api.StudentService
import com.example.studentregister.R
import com.example.studentregister.model.Student
import com.example.studentregister.adapter.StudentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        // Habilitar botão de voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Botão de Voltar no layout
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Finaliza a atividade atual e volta à anterior
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar adapter com lista vazia
        adapter = StudentAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchStudents()
    }

    private fun fetchStudents() {
        val service = ApiClient.getInstance("https://673d4c710118dbfe8606cbfc.mockapi.io/")
            .create(StudentService::class.java)

        service.listStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val students = response.body()
                    if (!students.isNullOrEmpty()) {
                        adapter = StudentAdapter(students)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@StudentListActivity, "No students found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@StudentListActivity, "Error fetching students", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@StudentListActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
