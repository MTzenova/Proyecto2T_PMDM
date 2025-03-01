package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentFavoritosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritosFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var db:FirebaseFirestore
    private var listaAmigos = mutableListOf<Item>()
    private var listaFavoritos = mutableListOf<Int>()
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentFavoritosBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentFavoritosBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //progressBar = binding.progressBar

//        GlobalScope.launch (Dispatchers.Main){
//            showProgressBar()
//        }

//        //Datos
//        val contactosList = listOf(
//            Item(1,R.drawable.msn_disponible,"Edu","Voy a domesticar a tu gato","(Disponible)",false),
//            Item(2,R.drawable.msn_ocupado,"David","Ocupado en este momento, no voy a responder","(Ausente)",false),
//            Item(3,R.drawable.msn_disponible,"Fuyur","Mónica, eres la mejor","(Disponible)",false),
//            Item(4,R.drawable.msn_desconectado,"Inés","Me gustan los gatos","(Desconectado)",false),
//            Item(5,R.drawable.msn_disponible_2,"Dolet","¿Alguien quiere hablar?","(Disponible)",false),
//            Item(6,R.drawable.msn_ausente,"Chaima","El chocolate con queso no está mal","(Ausente)",false),
//            Item(7,R.drawable.msn_desconectado,"Marco","Aquí está vuestro delegado, os quiero","(Desconectado)",false),
//            Item(8,R.drawable.msn_ausente,"Vero","La vida son dos días","(Ausente)",false),
//            Item(9,R.drawable.msn_desconectado,"Gabriel","No me habléis","(Desconectado)",false),
//            Item(10,R.drawable.msn_disponible,"Basty","Miauu","(Disponible)",false)
//        )
//
//        //var searchView: SearchView = view.findViewById(R.id.searchView)
//        //Referencia al RecyclerView en el layout
//        val reciclerView: RecyclerView = view.findViewById(R.id.rv)
//
//        //Configuración del RecyclerView
//        reciclerView.layoutManager = LinearLayoutManager(requireContext())
//        reciclerView.adapter = ItemAdapter(requireContext(), contactosList)
   }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

//    private fun loadFav()
//    {
//        val user = auth.currentUser?.email?:return
//
//        db.collection("usuarios").document(user).get().addOnSuccessListener {
//            document ->
//            if(document.exists()){
//                listaFavoritos = (document.get("fav" as? List<Long>)?.map() { it.toInt() }?.toMutableList() ?: mutableListOf()
//            }
//        }
//    }

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