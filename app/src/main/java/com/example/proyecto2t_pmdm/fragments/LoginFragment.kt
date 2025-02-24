package com.example.proyecto2t_pmdm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentLoginBinding
import com.example.proyecto2t_pmdm.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

//    private var param1: String? = null
//    private var param2: String? = null

    private var listener: OnFragmentChangeListener? = null

    //Interface para pasar información del Fragment al Activity
    interface OnFragmentChangeListener { fun onFragmentChangeLogin() }

    //ViewModel para pasar información entre Fragments
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLoginBinding.inflate(layoutInflater) //Infla la vista utilizando la clase de vinculación
        val view = binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = activity as OnFragmentChangeListener

        binding.button.setOnClickListener {
            val contrasenyaHelper = binding.campoContrasenya.text.toString()
            val correoElectronico = binding.campoCorreo.text.toString()
            var bien = true

            //contraseña
            if(contrasenyaHelper.isEmpty() || contrasenyaHelper.length < 8){
                bien = false
                binding.outlinedTextField1.error = getString(R.string.login_contrasenya_helper)
            }else {
                binding.outlinedTextField1.error = null
            }

            //correo electronico
            if(correoElectronico.isEmpty()){
                bien = false
                binding.outlinedTextField2.error = getString(R.string.login_correo_helper)
            }else {
                binding.outlinedTextField2.error = null
            }
            if(bien){
                val snackLogin = Snackbar.make(binding.root, R.string.snackbar_iniciar, Snackbar.LENGTH_LONG).show()
                //al pulsar el botón de iniciar sesion, nos carga el fragment de favoritos
                //cargarFragment(FavoritosFragment())
                findNavController().navigate(R.id.action_loginFragment2_to_favoritosFragment)
            }else{
                val snackError = Snackbar.make(binding.root, R.string.login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_cerrar)
                {

                }
                snackError.show()
            }
        }

        binding.noRecordar.setOnClickListener ()
        {
            //SE DEBE CAMBIAR POR ALERT DIALOG
            val snackNoRecordar = Snackbar.make(binding.root, R.string.snackbar_recordar, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_cerrar)
            {

            }
            snackNoRecordar.show()
        }

        //boton no tener cuenta -- lleva a register
        binding.noCuenta.setOnClickListener{
            val snackNoCuenta = Snackbar.make(binding.root, R.string.snackbar_no_cuenta, Snackbar.LENGTH_LONG).show()
            listener?.onFragmentChangeLogin()
        }

    }

    private fun cargarFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        //_binding = null
    }

    companion object {
       //patron singleton
        private var instance: LoginFragment? = null
        fun getInstance(): LoginFragment {
            if (instance == null) {
                instance = LoginFragment()
            }
            return instance!!
        }

    }
}