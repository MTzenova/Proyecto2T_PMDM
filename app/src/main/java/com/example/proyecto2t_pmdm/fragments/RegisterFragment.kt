package com.example.proyecto2t_pmdm.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Asignar el escuchador
        binding.registrarseRa.setOnClickListener {
            val clave = binding.campoClaveRa.text.toString()
            val correo = binding.campoCorreoRa.text.toString()
            val usuario = binding.campoUsuarioRa.text.toString()
            var bien = true
            if (clave.isEmpty() || clave.length < 8) {
                bien = false
                binding.claveRa.error = getString(R.string.login_contrasenya_helper)
            }else{
                binding.claveRa.error = null
            }
            if (correo.isEmpty()) {
                bien = false
                binding.correoElectronicoRa.error = getString(R.string.login_correo_helper)
            }else{
                binding.correoElectronicoRa.error = null
            }
            if (usuario.isEmpty()) {
                bien = false
                binding.nombreUsuarioRa.error = getString(R.string.usuario_error_ra)
            }else{
                binding.nombreUsuarioRa.error = null
            }
            if (bien){
                val snackRegister = Snackbar.make(binding.root, R.string.snackbar_registrarse_ra, Snackbar.LENGTH_LONG).show()
                //Intent
                //val intent: Intent = Intent(this, FavoritosActivity::class.java)
                //startActivity(intent)
                findNavController().navigate(R.id.action_registerFragment2_to_scaffoldFragment3)
            }else{
                val snackError = Snackbar.make(binding.root, R.string.login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_cerrar)
                {

                }
                snackError.show()
            }

        }

        binding.textoIniciarSesion.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
        }

        binding.btnDateRa.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener {
                    _, i, i2, i3 ->     cal.set(java.util.Calendar.YEAR, i)
                cal.set(java.util.Calendar.MONTH, i2)
                cal.set(java.util.Calendar.DAY_OF_MONTH, i3)
                binding.dateRa.text = "${cal.get(java.util.Calendar.DAY_OF_MONTH)}" +
                        "/${cal.get(java.util.Calendar.MONTH)+1}" +
                        "/${cal.get(java.util.Calendar.YEAR)}"

            }

            DatePickerDialog(requireContext(),
                dateSetListener, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show()

        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        //Patrón Singleton
        private var instance: RegisterFragment? = null

        fun getInstance(): RegisterFragment
        {

            if (instance == null)
            {
                instance = RegisterFragment()
            }

            return instance!!
        }

    }
}