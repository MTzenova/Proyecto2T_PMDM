package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentFavoritosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritosFragment : Fragment() {
    private lateinit var progressBar: ProgressBar

    private var _binding: FragmentFavoritosBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentFavoritosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        GlobalScope.launch (Dispatchers.Main){
            showProgressBar()
        }

        //Datos
        val contactosList = listOf(
            Item(R.drawable.msn_disponible,"Emilio","A un macarrón no se la dan con queso","(Disponible)",false),
            Item(R.drawable.msn_ocupado,"David","Ocupado en este momento, no voy a responder","(Ausente)",false),
            Item(R.drawable.msn_disponible,"Fuyur","Mónica, eres la mejor","(Disponible)",false),
            Item(R.drawable.msn_desconectado,"Inés","Me gustan los gatos","(Desconectado)",false),
            Item(R.drawable.msn_disponible_2,"Dolet","¿Alguien quiere hablar?","(Disponible)",false),
            Item(R.drawable.msn_ausente,"Chaima","El chocolate con queso no está mal","(Ausente)",false),
            Item(R.drawable.msn_desconectado,"Marco","Aquí está vuestro delegado, os quiero","(Desconectado)",false),
            Item(R.drawable.msn_ausente,"Vero","La vida son dos días","(Ausente)",false),
            Item(R.drawable.msn_desconectado,"Gabriel","No me habléis","(Desconectado)",false),
            Item(R.drawable.msn_disponible,"Basty","Miauu","(Disponible)",false)
        )

        //Referencia al RecyclerView en el layout
        val reciclerView: RecyclerView = view.findViewById(R.id.rv)

        //Configuración del RecyclerView
        reciclerView.layoutManager = LinearLayoutManager(requireContext())
        reciclerView.adapter = ItemAdapter(requireContext(), contactosList)
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    companion object
    {
        //Patrón Singleton
        private var instance: FavoritosFragment? = null

        fun getInstance(): FavoritosFragment
        {

            if (instance == null)
            {
                instance = FavoritosFragment()
            }

            return instance!!
        }
    }

    private suspend fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE

        delay(3000)

        progressBar.visibility = android.view.View.GONE


    }
}