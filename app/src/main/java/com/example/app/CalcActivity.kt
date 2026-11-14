package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat

/**
 * Tədris Nümunəsi: Kalkulyator Fəaliyyəti (CalcActivity)
 * =======================================================
 * Bu sinif tələbələrə 'findViewById' mexanizmini və səliqəli kod strukturunu öyrətmək üçün hazırlanıb:
 *
 * 1. 'findViewById' İstifadəsi:
 *    - Kotlin kodunda XML layout-dakı elementləri tapmaq üçün 'findViewById<Tip>(R.id.element_id)' istifadə olunur.
 *    - VACİB QAYDA: 'findViewById' mütləq 'setContentView(R.layout.activity_calc)' çağırıldıqdan SONRA işlədilməlidir!
 *      Əgər setContentView-dən əvvəl çağırılarsa, layout hələ yüklənmədiyi üçün tətbiq çökər (NullPointerException).
 *
 * 2. Strukturlaşdırılmış Kod (Structured Code):
 *    - initViews(): Bütün vizual elementlərin tapılması və dəyişənlərə mənimsədilməsi.
 *    - setupListeners(): Klik hadisələrinin vahid yerdə quraşdırılması.
 *    - handleOperation(): Təkrarçılığın qarşısını alan mərkəzləşdirilmiş hesablama funksiyası.
 *
 * 3. Təhlükəsiz Məlumat Çevrilməsi (Safe Parsing):
 *    - '.toDouble()' əvəzinə '.toDoubleOrNull()' istifadə edilərək boş xana və ya səhv simvol daxil edildikdə
 *      tətbiqin çökməsinin (crash) qarşısı alınır.
 */
class CalcActivity : AppCompatActivity() {

    // Görünüş elementlərinin təyini (lateinit var ilə sonradan initViews daxilində başladılacaq)
    private lateinit var etNumber1: EditText
    private lateinit var etNumber2: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnMultiple: Button
    private lateinit var btnDivide: Button
    private lateinit var btnClear: Button
    private lateinit var btnBackToLogin: Button

    // Riyazi əməliyyatların növlərini təyin edən Enum strukturu
    private enum class MathOperation(val symbol: String) {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("×"),
        DIVIDE("÷")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Müasir Android dizaynı üçün Edge-to-Edge ekran dəstəyi
        enableEdgeToEdge()

        // 1. Layout-u ekrana yükləyirik
        setContentView(R.layout.activity_calc)

        // Sistem status bar və naviqasiya paneli üçün padding tənzimlənməsi
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Elementləri ID-lərinə görə tapırıq
        initViews()

        // 3. Düymələrin klik hadisələrini qoşuruq
        setupListeners()
    }

    /**
     * XML faylındakı görünüşləri (Views) ID-ləri ilə Kotlin dəyişənlərinə bağlayır
     */
    private fun initViews() {
        etNumber1 = findViewById(R.id.number1)
        etNumber2 = findViewById(R.id.number2)
        tvResult = findViewById(R.id.result)

        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnMultiple = findViewById(R.id.btnMultiple)
        btnDivide = findViewById(R.id.btnDivide)

        btnClear = findViewById(R.id.btnClear)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)
    }

    /**
     * Düymələrin klik hadisələrini (Click Listeners) quraşdırır.
     */
    private fun setupListeners() {
        // Əməliyyat düymələri
        btnPlus.setOnClickListener { handleOperation(MathOperation.ADD) }
        btnMinus.setOnClickListener { handleOperation(MathOperation.SUBTRACT) }
        btnMultiple.setOnClickListener { handleOperation(MathOperation.MULTIPLY) }
        btnDivide.setOnClickListener { handleOperation(MathOperation.DIVIDE) }

        // Təmizləmə düyməsi
        btnClear.setOnClickListener { clearInputs() }

        // Geri qayıtma düyməsi (Giriş ekranına dönür)
        btnBackToLogin.setOnClickListener { finish() }
    }

    /**
     * Vahid Hesablama Məntiqi (Structured & Centralized logic):
     * 1. Girişləri yoxlayır (Validasiya)
     * 2. Əməliyyatı icra edir
     * 3. Nəticəni ekranda göstərir
     */
    private fun handleOperation(operation: MathOperation) {
        // 1. Giriş sahələrindən ədədləri təhlükəsiz şəkildə oxuyuruq
        val inputs = validateAndExtractInputs() ?: return

        val (num1, num2) = inputs

        // 2. Sıfıra bölmə xətasının yoxlanması
        if (operation == MathOperation.DIVIDE && num2 == 0.0) {
            val errorMsg = getString(R.string.error_divide_by_zero)
            tvResult.text = errorMsg
            etNumber2.error = errorMsg
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Əməliyyatın hesablanması
        val calculation = when (operation) {
            MathOperation.ADD -> num1 + num2
            MathOperation.SUBTRACT -> num1 - num2
            MathOperation.MULTIPLY -> num1 * num2
            MathOperation.DIVIDE -> num1 / num2
        }

        // 4. Nəticənin formatlanması və ekranda əks etdirilməsi
        val formattedResult = formatNumber(calculation)
        tvResult.text = formattedResult

        // Əməliyyat barədə qısa Toast məlumatı
        val infoText = "$num1 ${operation.symbol} $num2 = $formattedResult"
        Toast.makeText(this, infoText, Toast.LENGTH_SHORT).show()
    }

    /**
     * Təhlükəsiz Giriş Yoxlanışı (Input Validation):
     * İstifadəçinin boşluq buraxmadığını və düzgün rəqəm daxil etdiyini təsdiqləyir.
     *
     * @return Uğurlu olduqda Pair(Double, Double), xəta olduqda null qaytarır
     */
    private fun validateAndExtractInputs(): Pair<Double, Double>? {
        val str1 = etNumber1.text.toString().trim()
        val str2 = etNumber2.text.toString().trim()

        var isValid = true

        if (str1.isEmpty()) {
            etNumber1.error = getString(R.string.error_empty_fields)
            isValid = false
        }

        if (str2.isEmpty()) {
            etNumber2.error = getString(R.string.error_empty_fields)
            isValid = false
        }

        if (!isValid) return null

        // toDoubleOrNull() crash-in qarşısını alan təhlükəsiz çevrilmə metodudur
        val num1 = str1.toDoubleOrNull()
        val num2 = str2.toDoubleOrNull()

        if (num1 == null) {
            etNumber1.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (num2 == null) {
            etNumber2.error = getString(R.string.error_invalid_number)
            isValid = false
        }

        if (!isValid) return null

        return Pair(num1!!, num2!!)
    }

    /**
     * Nəticəni gözəl formatda göstərmək üçün köməkçi metod:
     * - 5.0 əvəzinə "5"
     * - 3.1400000000000001 əvəzinə "3.14" kimi əks etdirir.
     */
    private fun formatNumber(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            val df = DecimalFormat("#.########")
            df.format(value)
        }
    }

    /**
     * Giriş sahələrini və nəticəni təmizləyərək ilkin vəziyyətə gətirir.
     */
    private fun clearInputs() {
        etNumber1.text?.clear()
        etNumber2.text?.clear()
        etNumber1.error = null
        etNumber2.error = null
        tvResult.text = getString(R.string.result_default)
        etNumber1.requestFocus()
    }
}
