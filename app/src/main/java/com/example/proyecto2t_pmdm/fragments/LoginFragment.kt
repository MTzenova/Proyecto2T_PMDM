package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.databinding.FragmentLoginBinding
import com.example.proyecto2t_pmdm.viewmodels.SharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
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

        binding.noCuenta.setOnClickListener{
            listener?.onFragmentChangeLogin()
        }

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