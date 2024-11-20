package com.example.studentregister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.studentregister.R
import com.example.studentregister.ui.StudentFormActivity
import com.example.studentregister.ui.StudentListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencie os botões pelo ID
        val btnGoToRegister: Button = findViewById(R.id.btnGoToRegister)
        val btnGoToList: Button = findViewById(R.id.btnGoToList)

        // Configura o clique para ir à tela de cadastro
        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, StudentFormActivity::class.java)
            startActivity(intent)
        }

        // Configura o clique para ir à tela de listagem
        btnGoToList.setOnClickListener {
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }
    }
}