package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentLoginBinding
import com.example.proyecto2t_pmdm.viewmodels.LoginViewModel
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
    private lateinit var binding: FragmentLoginBinding //acceder a elementos
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    private var listener: OnFragmentChangeListener? = null //esto creo que tampoco lo uso ahora

    //Interface para pasar información del Fragment al Activity
    interface OnFragmentChangeListener { fun onFragmentChangeLogin() } //no lo usamos

    //ViewModel para pasar información entre Fragments
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLoginBinding.inflate(layoutInflater) //Infla la vista
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)//Infla la vista
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = activity as OnFragmentChangeListener

        //desactivar botón de login
        binding.button.isEnabled = false

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        //configurar observadores
        viewModel.loginEnable.observe(viewLifecycleOwner, Observer { success ->
            if(success){
                binding.button.isEnabled = true //activo el botón
                binding.button.alpha = 1f //el botón no se ve transparente
            }else{
                binding.button.isEnabled = false //desactivo el botón
                binding.button.alpha = 0.5f //bajo transparencia al botón
            }
        })

        //para actualizar los valores en viewmodel (correo)
        binding.campoCorreo.doOnTextChanged{text, _, _, _ ->
            viewModel.onEmailChanged(text?.toString()?:"")
            viewModel.onLoginEnableChanged()
        }
        //para actualizar los valores en viewmodel (contraseña)
        binding.campoContrasenya.doOnTextChanged{text, _, _, _ ->
            viewModel.onPasswordChanged(text?.toString()?:"")
            viewModel.onLoginEnableChanged()
        }

        //boton de iniciar sesion
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

            //si los campos son correctos
            if(bien){
                Snackbar.make(binding.root, R.string.snackbar_iniciar, Snackbar.LENGTH_LONG).show()

                //para autenticarnos con correo electrónico
                auth = FirebaseAuth.getInstance()
                auth
                    .signInWithEmailAndPassword(binding.campoCorreo.text.toString(), binding.campoContrasenya.text.toString())
                    .addOnSuccessListener {
                        //si el login es exitoso, pasamos a otro fragment
                        findNavController().navigate(R.id.action_loginFragment2_to_scaffoldFragment3)
                    }
                    .addOnFailureListener { exception ->
                        //si hay error, mostramos el mensaje
                        Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show() //formato de contraseña inválido???
                    }
            }else{
                val snackError = Snackbar.make(binding.root, R.string.login_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_cerrar)
                {

                }
                snackError.show()
            }


        }

        //no recordar contraseña
        binding.noRecordar.setOnClickListener ()
        {
            //SE DEBE CAMBIAR POR ALERT DIALOG
            val snackNoRecordar = Snackbar.make(binding.root, R.string.snackbar_recordar, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_cerrar)
            {

            }
            snackNoRecordar.show()
        }

        //boton no tener cuenta.
        binding.noCuenta.setOnClickListener{
            Snackbar.make(binding.root, R.string.snackbar_no_cuenta, Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2) //lleva a register
        }

        //botón de iniciar sesión con google
        binding.button2.setOnClickListener()
        {
            signInWithGoogle() //con este método se inicia sesión con google
        }

        //botón de iniciar sesión con facebook
        binding.button3.setOnClickListener()
        {
            Snackbar.make(binding.root, R.string.snackbar_iniciar_facebook, Snackbar.LENGTH_LONG).show() //muestra mensaje
        }



    }

//    private fun cargarFragment(fragment: Fragment) {
//        parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()
//    }

    //método para iniciar sesión con google
    private fun signInWithGoogle(){
        val auth = FirebaseAuth.getInstance() //si no inicio esto aquí, no me inicia sesión

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) //con esto en false ya me deja elegir cuenta
            .setServerClientId(getString(R.string.web_client_id)) //la id
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

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                if(authResult != null)
                {
                    //si va bien, nos lelva al scaffold, a la lista
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment2_to_scaffoldFragment3)
                    }

                }
                else
                {
                    //si no, mensaje de error
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
//       //patron singleton
//        private var instance: LoginFragment? = null
//        fun getInstance(): LoginFragment {
//            if (instance == null) {
//                instance = LoginFragment()
//            }
//            return instance!!
//        }

    }
}