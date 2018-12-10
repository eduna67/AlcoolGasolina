package com.example.ena.alcoolougasolina

import android.content.Context

class MinhaPreferencia(context: Context) {

    val PREFERENCIA_NOME = "ExemploPreferencia"

    val PREFERENCIA_GASOLINA_MEIDA_KM = "GasolinaMediakm"
    val PREFERENCIA_GASOLINA_VALOR_LITRO = "GasolinaValorLitro"

    val PREFERENCIA_ALCOOL_MEIDA_KM = "AlcoolMediakm"
    val PREFERENCIA_ALCOOL_VALOR_LITRO = "AlcoolValorLitro"


    val preferencia = context.getSharedPreferences(PREFERENCIA_NOME,Context.MODE_PRIVATE)

    fun getGasolinaMediaKm(): Float{
        return preferencia.getFloat(PREFERENCIA_GASOLINA_MEIDA_KM, 0.00F)
    }
    fun setGasolinaMediaKM(ValorMedioKm: Float){
        val editor = preferencia.edit()
        editor.putFloat(PREFERENCIA_GASOLINA_MEIDA_KM,ValorMedioKm)
        editor.apply()
    }
    fun getAlcoolMediaKm(): Float{
        return preferencia.getFloat(PREFERENCIA_ALCOOL_MEIDA_KM, 0.00F)
    }
    fun setAlcoolMediaKM(ValorMedioKm: Float){
        val editor = preferencia.edit()
        editor.putFloat(PREFERENCIA_ALCOOL_MEIDA_KM,ValorMedioKm)
        editor.apply()
    }
    fun getAlcoolValorLitro(): Float{
        return preferencia.getFloat(PREFERENCIA_ALCOOL_VALOR_LITRO, 0.00F)
    }
    fun setAlcoolValorLitro(ValorMedioKm: Float){
        val editor = preferencia.edit()
        editor.putFloat(PREFERENCIA_ALCOOL_VALOR_LITRO,ValorMedioKm)
        editor.apply()
    }
    fun getGasoinaValorLitro(): Float{
        return preferencia.getFloat(PREFERENCIA_GASOLINA_VALOR_LITRO, 0.00F)
    }
    fun setGasolinaValorLitro(ValorMedioKm: Float){
        val editor = preferencia.edit()
        editor.putFloat(PREFERENCIA_GASOLINA_VALOR_LITRO,ValorMedioKm)
        editor.apply()
    }

}