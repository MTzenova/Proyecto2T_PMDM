package com.example.proyecto2t_pmdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.proyecto2t_pmdm.databinding.ActivityMainBinding
import com.example.proyecto2t_pmdm.fragments.LoginFragment
import com.example.proyecto2t_pmdm.fragments.RegisterFragment

class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentChangeListener {

    private lateinit var binding: ActivityMainBinding //Declara una variable de vinculación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) //infla al vista utilizando la clase de vinculación
        val view = binding.root
        setContentView(view)
        //cargamos el fragment del login primero
        cargarFragment(LoginFragment.getInstance())
    }

    private fun cargarFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun onFragmentChangeLogin(){
        cargarFragment(RegisterFragment())
    }

}