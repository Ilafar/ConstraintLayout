package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Tədris Nümunəsi: Giriş Fəaliyyəti (LoginActivity)
 * =================================================
 * Bu fəaliyyət tələbələrə 'findViewById' vasitəsilə elementlərlə işləməyi öyrədir:
 * 1. 'findViewById' ilə XML elementlərini koda bağlamaq.
 * 2. Form sahələrini yoxlamaq (Validasiya).
 * 3. Intent vasitəsilə digər fəaliyyətlərə (RegisterActivity və CalcActivity) keçid etmək.
 */
class LoginActivity : AppCompatActivity() {

    // Görünüş elementləri
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView
    private lateinit var btnOpenCalc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Müasir Edge-to-Edge ekran dəstəyi
        enableEdgeToEdge()

        // 1. Əvvəlcə XML layout-u ekrana yükləyirik
        setContentView(R.layout.activity_login)

        // Status bar və naviqasiya zolaqları üçün padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Elementləri ID-lərinə əsasən tapırıq
        initViews()

        // 3. Klik hadisələrini təyin edirik
        setupClickListeners()
    }

    /**
     * XML görünüşlərini ID vasitəsilə dəyişənlərə bağlayır
     */
    private fun initViews() {
        etEmail = findViewById(R.id.edittext_email)
        etPassword = findViewById(R.id.edittext_pwd)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoToRegister = findViewById(R.id.tvGoToRegister)
        btnOpenCalc = findViewById(R.id.btnOpenCalc)
    }

    /**
     * Klik dinləyicilərinin (Listeners) quraşdırılması
     */
    private fun setupClickListeners() {
        // 1. Giriş düyməsinə basıldıqda
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = getString(R.string.hint_email)
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = getString(R.string.hint_password)
                return@setOnClickListener
            }

            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        }

        // 2. Qeydiyyat ekranına keçid
        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 3. Kalkulyator ekranına keçid (ConstraintLayout zəncir nümunəsini yoxlamaq üçün)
        btnOpenCalc.setOnClickListener {
            val intent = Intent(this, CalcActivity::class.java)
            startActivity(intent)
        }
    }
}
