package com.example.proyecto2t_pmdm.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentRegisterBinding
import com.example.proyecto2t_pmdm.fragments.LoginFragment.OnFragmentChangeListener
import com.example.proyecto2t_pmdm.viewmodels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth

    private var listener: OnFragmentChangeListener? = null

    //Interface para pasar información del Fragment al Activity
   // interface OnFragmentChangeListener{fun onFragmentChangeRegister()}

    //ViewModel para pasar información entre Fragments
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.root
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = activity as OnFragmentChangeListener

        //desactivar botón de registrar
        binding.registrarseRa.isEnabled = false

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        //configurar observadores
        viewModel.registerEnabled.observe(viewLifecycleOwner, Observer{ success ->

            if (success) {
                binding.registrarseRa.isEnabled = true
                binding.registrarseRa.alpha = 1f
            }else{
                binding.registrarseRa.isEnabled = false
                    binding.registrarseRa.alpha = 0.5f
            }
        })

        binding.campoCorreoRa.doOnTextChanged { text, _, _, _->
            viewModel.setEmail(text?.toString()?:"")
            viewModel.onRegisterEnableChanged()
        }

        binding.campoClaveRa.doOnTextChanged {text, _, _, _ ->
            viewModel.setPassword(text?.toString()?:"")
            viewModel.onRegisterEnableChanged()
        }

        binding.campoUsuarioRa.doOnTextChanged {text, _, _, _ ->
            viewModel.setUsername(text?.toString()?:"")
            viewModel.onRegisterEnableChanged()
        }

        binding.btnDateRa.doOnTextChanged {text, _, _, _ ->
            viewModel.setDate(text?.toString()?:"")
            viewModel.onRegisterEnableChanged()
        }


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
                Snackbar.make(binding.root, R.string.snackbar_registrarse_ra, Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_registerFragment2_to_scaffoldFragment3)

                auth = FirebaseAuth.getInstance()
                    auth
                        .createUserWithEmailAndPassword(binding.campoCorreoRa.text.toString(), binding.campoClaveRa.text.toString())
                        .addOnSuccessListener {
                            //si consigue registrarse, pasamos a otro fragment
                            findNavController().navigate(R.id.action_registerFragment2_to_scaffoldFragment3)
                        }
                        .addOnFailureListener { exception ->
                            //en caso de error
                            Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                        }
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
                val selectedDate = "${cal.get(java.util.Calendar.DAY_OF_MONTH)}" +
                        "/${cal.get(java.util.Calendar.MONTH)+1}" +
                        "/${cal.get(java.util.Calendar.YEAR)}"

                binding.dateRa.text = selectedDate

                viewModel.setDate(selectedDate)
            }

            DatePickerDialog(requireContext(),
                dateSetListener, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show()

        }
    }

    companion object {
        //Patrón Singleton
        //private var instance: RegisterFragment? = null
//aaaa
//        fun getInstance(): RegisterFragment
//        {
//
//            if (instance == null)
//            {
//                instance = RegisterFragment()
//            }
//
//            return instance!!
//        }

    }
}