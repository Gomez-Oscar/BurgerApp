package com.example.practica3

import android.app.Application

class GlobalClass : Application() {

    private var conteoCar: Int = 0

    fun getConteo(): Int {
        return conteoCar
    }

    fun setConteo(conteoCar: Int) {
        this.conteoCar = conteoCar
    }
}