package com.example.proyecto2t_pmdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyecto2t_pmdm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //Declara una variable de vinculación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) //infla al vista utilizando la clase de vinculación
        val view = binding.root

        //Establece la vista del activity
        setContentView(view)

        //Ahora puedes acceder a tus vistas directamente usando la variable de vinculación

    }
}