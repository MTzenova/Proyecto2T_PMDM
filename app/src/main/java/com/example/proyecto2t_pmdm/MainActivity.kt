package com.example.proyecto2t_pmdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.proyecto2t_pmdm.clases.ViewPagerAdapter
import com.example.proyecto2t_pmdm.databinding.ActivityMainBinding
import com.example.proyecto2t_pmdm.fragments.LoginFragment
import com.example.proyecto2t_pmdm.fragments.RegisterFragment

class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentChangeListener {

    private lateinit var binding: ActivityMainBinding //Declara una variable de vinculación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) //infla al vista utilizando la clase de vinculación

        setContentView(binding.root)

        //cargamos el fragment del login primero
//        if(savedInstanceState == null) {
//            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
//            val navController = navHostFragment.navController
//
//        }
    }

    override fun onFragmentChangeLogin(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_loginFragment2_to_registerFragment2)
    }
    //Nuevo método para configurar el componente Navigation
    override fun onSupportNavigateUp(): Boolean
    {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}