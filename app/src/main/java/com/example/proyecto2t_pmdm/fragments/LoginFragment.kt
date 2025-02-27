package com.example.proyecto2t_pmdm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentLoginBinding
import com.example.proyecto2t_pmdm.viewmodels.SharedViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

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
                findNavController().navigate(R.id.action_loginFragment2_to_scaffoldFragment3)
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
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }

        binding.button2.setOnClickListener()
        {
            signInWithGoogle()
        }

        binding.button3.setOnClickListener()
        {
            val snackLoginFacebook = Snackbar.make(binding.root, R.string.snackbar_iniciar_facebook, Snackbar.LENGTH_LONG).show()
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

    fun signInWithGoogle(){
        val auth = FirebaseAuth.getInstance() //si no inicio esto aquí, no me inicia sesiñón

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) //con esto en false ya me deja elegir cuenta
            .setServerClientId(getString(R.string.web_client_id))
            //.setNonce(hashedNonce)
            .setAutoSelectEnabled(false) //con esto en false no selecciona automáticamente una cuenta de google
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            credentialManager = CredentialManager.create(context = requireContext())
            try {

                val result = credentialManager.getCredential(context = requireContext(), request = request)
                val credential = result.credential

                // Use googleIdTokenCredential and extract the ID to validate and
                // authenticate on your server.
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                // You can use the members of googleIdTokenCredential directly for UX
                // purposes, but don't use them to store or control access to user
                // data. For that you first need to validate the token:
                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                if(authResult != null)
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment2_to_scaffoldFragment3)
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Error en el login", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            catch (e: GetCredentialException)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(requireContext(), e.localizedMessage , Toast.LENGTH_SHORT).show()
                }
            }
        }
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