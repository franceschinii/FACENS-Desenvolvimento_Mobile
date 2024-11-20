package com.example.studentregister.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studentregister.R
import com.example.studentregister.api.ApiClient
import com.example.studentregister.api.StudentService
import com.example.studentregister.api.ZipCodeService
import com.example.studentregister.model.Address
import com.example.studentregister.model.Student
import com.google.android.material.textfield.TextInputEditText
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

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Finaliza a atividade atual e volta à anterior
        }

        // Habilitar botão de voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                        // Atualize apenas os campos que não estão vazios
                        if (!it.street.isNullOrBlank()) edtStreet.setText(it.street)
                        if (!it.complement.isNullOrBlank()) edtComplement.setText(it.complement)
                        if (!it.neighborhood.isNullOrBlank()) edtNeighborhood.setText(it.neighborhood)
                        if (!it.city.isNullOrBlank()) edtCity.setText(it.city)
                        if (!it.state.isNullOrBlank()) edtState.setText(it.state)
                    }
                } else {
                    Toast.makeText(
                        this@StudentFormActivity,
                        "CEP não encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {
                Toast.makeText(
                    this@StudentFormActivity,
                    "Erro ao buscar CEP: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveStudent() {
        val name = edtName.text.toString().trim()
        val ra = edtRa.text.toString().toIntOrNull()
        val zip = edtZip.text.toString().trim()
        val street = edtStreet.text.toString().trim()
        val complement = edtComplement.text.toString().trim()
        val neighborhood = edtNeighborhood.text.toString().trim()
        val city = edtCity.text.toString().trim()
        val state = edtState.text.toString().trim()

        // Verifique se todos os campos obrigatórios estão preenchidos
        if (name.isBlank() || ra == null || zip.isBlank() || street.isBlank() || neighborhood.isBlank() || city.isBlank() || state.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val student = Student(name, ra, zip, street, complement, neighborhood, city, state)

        // Adicione um log para verificar os dados que estão sendo enviados
        println("Dados do estudante: $student")

        val service = ApiClient.getInstance("https://673d4c710118dbfe8606cbfc.mockapi.io/")
            .create(StudentService::class.java)

        service.saveStudent(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@StudentFormActivity,
                        "Estudante salvo com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@StudentFormActivity, StudentListActivity::class.java))
                } else {
                    // Adicione um log para verificar a resposta do erro
                    println("Erro na resposta: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@StudentFormActivity,
                        "Erro ao salvar estudante",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(
                    this@StudentFormActivity,
                    "Erro de rede: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
