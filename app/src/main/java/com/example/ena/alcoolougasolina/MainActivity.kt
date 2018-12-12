package com.example.ena.alcoolougasolina

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MainActivity : AppCompatActivity() {

    private val prefix = "R$ "

    private val MAX_LENGTH = 4
    private val MAX_DECIMAL = 3

    //lateinit var preferencias : MinhaPreferencia
    var preferencias : MinhaPreferencia? = null

    //var preferencias = MinhaPreferencia(baseContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setContentView(R.layout.activity_main)
        var previousCleanString = ""

        //preferenciasTeste = MinhaPreferencia(this.baseContext)
        //mediaGasolina.setText(preferenciasTeste!!.getGasolinaMediaKm().toString().replace(".", ","))

        preferencias = MinhaPreferencia(baseContext)

        if (!preferencias!!.getGasolinaMediaKm().toString().trim().isEmpty())
            mediaGasolina.setText(preferencias!!.getGasolinaMediaKm().toString().replace(".", ","))

        if (!preferencias!!.getAlcoolMediaKm().toString().trim().isEmpty())
            mediaAlcool.setText(preferencias!!.getAlcoolMediaKm().toString().replace(".", ","))

        if (!preferencias!!.getGasoinaValorLitro().toString().trim().isEmpty())
            valorLitroGasolina.setText(getString(R.string.moeda, preferencias!!.getGasoinaValorLitro().toString().replace(".", ",")))

        if (!preferencias!!.getAlcoolValorLitro().toString().trim().isEmpty())
            valorLitroAlcool.setText(getString(R.string.moeda, preferencias!!.getAlcoolValorLitro().toString().replace(".", ",")))

        mediaGasolina.setSelection(mediaGasolina.text.length)
        mediaAlcool.setSelection(mediaAlcool.text.length)
        valorLitroGasolina.setSelection(valorLitroGasolina.text.length)
        valorLitroAlcool.setSelection(valorLitroAlcool.text.length)


        valorLitroAlcool.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val str = s.toString()
                if (str.length < prefix.length) {
                    valorLitroAlcool.setText(prefix)
                    valorLitroAlcool.setSelection(prefix.length)
                    return
                }
                if (str == prefix) {
                    return
                }
                // cleanString this the string which not contain prefix and ,
                val cleanString = str.replace(prefix, "").replace("[,]".toRegex(), "")
                // for prevent afterTextChanged recursive call

                if (cleanString == previousCleanString || cleanString.isEmpty()) {
                    return
                }
                previousCleanString = cleanString

                val formattedString: String
                if (cleanString.contains(".")) {
                    formattedString = formatDecimal(cleanString)
                } else {
                    formattedString = formatInteger(cleanString)
                }
                valorLitroAlcool.removeTextChangedListener(this) // Remove listener
                valorLitroAlcool.setText(formattedString)
                handleSelection()
                valorLitroAlcool.addTextChangedListener(this) // Add back the listener
            }
        })

        valorLitroAlcool.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calcular()
                methodManager.hideSoftInputFromWindow(valorLitroAlcool.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                true
            } else {
                false
            }

        }

        button_calular.setOnClickListener {
            methodManager.hideSoftInputFromWindow(valorLitroAlcool.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            methodManager.hideSoftInputFromWindow(valorLitroGasolina.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            methodManager.hideSoftInputFromWindow(mediaGasolina.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            methodManager.hideSoftInputFromWindow(mediaAlcool.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            calcular()
        }

        // currency.
    }

    private fun formatInteger(str: String): String {
        val parsed = BigDecimal(str)
        val formatter = DecimalFormat("$prefix#,###", DecimalFormatSymbols(Locale.US))
        return formatter.format(parsed)
    }

    private fun formatDecimal(str: String): String {
        if (str == ".") {
            return "$prefix."
        }
        val parsed = BigDecimal(str)
        // example pattern VND #,###.00
        val formatter = DecimalFormat(prefix + "#,###" + getDecimalPattern(str),
                DecimalFormatSymbols(Locale.US))
        formatter.roundingMode = RoundingMode.DOWN
        return formatter.format(parsed)
    }

    private fun getDecimalPattern(str: String): String {
        val decimalCount = str.length - str.indexOf(".") - 1
        val decimalPattern = StringBuilder()
        var i = 0
        while (i < decimalCount && i < MAX_DECIMAL) {
            decimalPattern.append("0")
            i++
        }
        return decimalPattern.toString()
    }

    private fun handleSelection() {
        if (valorLitroAlcool.getText().length <= MAX_LENGTH) {
            valorLitroAlcool.setSelection(valorLitroAlcool.getText().length)
        } else {
            valorLitroAlcool.setSelection(MAX_LENGTH)
        }
    }

    fun calcular() {
        if ((mediaAlcool.text.toString().replace(",", ".").toDouble() / mediaGasolina.text.toString().replace(",", ".").toDouble())
                * valorLitroGasolina.text.toString().replace(",", ".").toDouble() > valorLitroAlcool.text.toString().replace(",", ".").toDouble())
            txtSaida.setText("Use Alcool") // consumoMedioAlcool.text.toString())
        else
            txtSaida.setText("Use Gasolina") // consumoMedioAlcool.text.toString())

        preferencias!!.setGasolinaMediaKM(mediaGasolina.text.toString().replace(",", ".").toFloat())
        preferencias!!.setAlcoolMediaKM(mediaAlcool.text.toString().replace(",", ".").toFloat())

        preferencias!!.setGasolinaValorLitro(valorLitroGasolina.text.toString().replace(",", ".").toFloat())
        preferencias!!.setAlcoolValorLitro(valorLitroAlcool.text.toString().replace(",", ".").toFloat())

        Log.i("BOTAO", "Clicou no botão")


//
// gasolina 2,97
// alcool 2,15
// consumo alcool 6 / consumo gasolina 9 = 0,667 = fator
// 0,667*valor gasolina 2,97 = se for menor que o valor do alcool use gasolina, se for maior use alcool
    }
}
