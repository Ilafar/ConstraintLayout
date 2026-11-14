package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Tədris Nümunəsi: Qeydiyyat Fəaliyyəti (RegisterActivity)
 * =======================================================
 * Bu fəaliyyət tələbələrə 'findViewById' ilə elementlərin tapılmasını və
 * düyməyə basıldıqda sadə bildiriş göstərilməsini nümayiş etdirir.
 */
class RegisterActivity : AppCompatActivity() {

    // Görünüş elementləri
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Müasir Edge-to-Edge ekran dəstəyi
        enableEdgeToEdge()

        // 1. Layout-u yükləyirik
        setContentView(R.layout.activity_register)

        // Padding tənzimlənməsi
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Elementləri tapırıq
        initViews()

        // 3. Hadisələri qoşuruq
        setupListeners()
    }

    /**
     * XML elementlərini 'findViewById' ilə başladır
     */
    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etRepeatPassword = findViewById(R.id.etRepeatPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
    }

    /**
     * Klik hadisələrinin quraşdırılması
     */
    private fun setupListeners() {
        // Qeydiyyat düyməsi basıldıqda
        btnRegister.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.register_success),
                Toast.LENGTH_LONG
            ).show()

            // Giriş ekranına qayıdırıq
            finish()
        }

        // "Artıq hesabınız var? Daxil olun" düyməsi: cari ekranı bağlayaraq LoginActivity-ə qayıdır
        btnLogin.setOnClickListener {
            finish()
        }
    }
}
