package com.example.proyecto2t_pmdm.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentContactoBinding
import com.example.proyecto2t_pmdm.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [ContactoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactoFragment : Fragment() {
    private val CALL_PHONE_PERMISSION_REQUEST = 123
    private lateinit var binding: FragmentContactoBinding
    private var listener: LoginFragment.OnFragmentChangeListener? = null

    interface OnFragmentChangeListener {
        fun onFragmentChange(fragment: Fragment?)
    }

    private lateinit var viewModel: SharedViewModel

    override fun onDetach()
    {
        super.onDetach()
        //listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = activity as LoginFragment.OnFragmentChangeListener

        binding.imageTelefono.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall()
            }
            else
            {
                //El usuario ya ha rechazado previamente el permiso
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        android.Manifest.permission.CALL_PHONE))

                    Snackbar.make(binding.root, "El permiso ha sido rechazado previamente." +
                            "Debes activarlo desde ajustes", Snackbar.LENGTH_LONG).show()

                else
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),
                        CALL_PHONE_PERMISSION_REQUEST)
            }
        }

        binding.imageWtpp.setOnClickListener {
            val snackbarWtpp = Snackbar.make(binding.root, R.string.snack_wtpp_ca, Snackbar.LENGTH_INDEFINITE).setAction("Cerrar"){
                val url="https://api.whatsapp.com/send?phone=34${binding.wtppCa.text}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            snackbarWtpp.show()

        }

        binding.imageMail.setOnClickListener {
            val snackbarMail = Snackbar.make(binding.root, R.string.snack_mail_ca, Snackbar.LENGTH_INDEFINITE).setAction("Cerrar"){
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@microsoft.com")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.mailCa.text))
                }

                startActivity(intent)
            }
            snackbarMail.show()

        }

        binding.imageUbi.setOnClickListener{
            val snackbarUbi = Snackbar.make(binding.root, R.string.snack_maps_ca, Snackbar.LENGTH_INDEFINITE).setAction("Cerrar"){
                val mapIntent= Intent(Intent.ACTION_VIEW, Uri.parse("geo:47.645914,-122.131959"))

                startActivity(mapIntent)
            }

            snackbarUbi.show()

        }
    }

    private fun makePhoneCall()
    {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:999999999"))

        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivity(intent)
        else
            Snackbar.make(binding.root, "No se puede hacer la llamada porque " +
                    "no hay ninguna app disponible", Snackbar.LENGTH_LONG).show()
    }

    private fun cargarFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    companion object {
        //Patrón Singleton
        private var instance: ContactoFragment? = null

        fun getInstance(): ContactoFragment
        {

            if (instance == null)
            {
                instance = ContactoFragment()
            }

            return instance!!
        }
    }
}