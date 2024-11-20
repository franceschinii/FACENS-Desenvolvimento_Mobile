package com.example.studentregister.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentregister.R
import com.google.android.material.textfield.TextInputEditText
import com.example.studentregister.api.ApiClient
import com.example.studentregister.api.StudentService
import com.example.studentregister.api.ZipCodeService
import com.example.studentregister.model.Address
import com.example.studentregister.model.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentFormActivity : AppCompatActivity() {

    private lateinit var edtName: TextInputEditText
    private lateinit var edtRa: TextInputEditText
    private lateinit var edtZip: TextInputEditText
    private lateinit var edtStreet: TextInputEditText
    private lateinit var edtComplement: TextInputEditText
    private lateinit var edtNeighborhood: TextInputEditText
    private lateinit var edtCity: TextInputEditText
    private lateinit var edtState: TextInputEditText
    private lateinit var btnFetchZip: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_form)

        edtName = findViewById(R.id.edtName)
        edtRa = findViewById(R.id.edtRa)
        edtZip = findViewById(R.id.edtZip)
        edtStreet = findViewById(R.id.edtStreet)
        edtComplement = findViewById(R.id.edtComplement)
        edtNeighborhood = findViewById(R.id.edtNeighborhood)
        edtCity = findViewById(R.id.edtCity)
        edtState = findViewById(R.id.edtState)
        btnFetchZip = findViewById(R.id.btnFetchZip)
        btnSave = findViewById(R.id.btnSave)

        btnFetchZip.setOnClickListener {
            val zip = edtZip.text.toString()
            if (zip.isNotEmpty()) {
                fetchAddress(zip)
            } else {
                Toast.makeText(this, "Enter a valid ZIP code", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            saveStudent()
        }
    }

    private fun fetchAddress(zip: String) {
        val service = ApiClient.getInstance("https://viacep.com.br/ws/")
            .create(ZipCodeService::class.java)

        service.fetchAddress(zip).enqueue(object : Callback<Address> {
            override fun onResponse(call: Call<Address>, response: Response<Address>) {
                if (response.isSuccessful) {
                    val address = response.body()
                    address?.let {
                        edtStreet.setText(it.street)
                        edtComplement.setText(it.complement)
                        edtNeighborhood.setText(it.neighborhood)
                        edtCity.setText(it.city)
                        edtState.setText(it.state)
                    }
                } else {
                    Toast.makeText(this@StudentFormActivity, "ZIP not found", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {
                Toast.makeText(
                    this@StudentFormActivity,
                    "Failed to fetch ZIP: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveStudent() {
        val name = edtName.text.toString()
        val ra = edtRa.text.toString().toIntOrNull()
        val zip = edtZip.text.toString()
        val street = edtStreet.text.toString()
        val complement = edtComplement.text.toString()
        val neighborhood = edtNeighborhood.text.toString()
        val city = edtCity.text.toString()
        val state = edtState.text.toString()

        if (name.isBlank() || ra == null || zip.isBlank() || street.isBlank() || neighborhood.isBlank() || city.isBlank() || state.isBlank()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val student = Student(name, ra, zip, street, complement, neighborhood, city, state)
        val service = ApiClient.getInstance("https://673d4c710118dbfe8606cbfc.mockapi.io/")
            .create(StudentService::class.java)

        service.saveStudent(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@StudentFormActivity,
                        "Student saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@StudentFormActivity, StudentListActivity::class.java))
                } else {
                    Toast.makeText(
                        this@StudentFormActivity,
                        "Error saving student",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(
                    this@StudentFormActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}