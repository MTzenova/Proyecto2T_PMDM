package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.proyecto2t_pmdm.R
import androidx.appcompat.widget.SearchView
import com.example.proyecto2t_pmdm.clases.Item
import com.example.proyecto2t_pmdm.clases.ItemAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentScaffoldBinding
import com.google.firebase.auth.FirebaseAuth


class ScaffoldFragment : Fragment() {
    private lateinit var binding: FragmentScaffoldBinding
    private  var amigos: MutableList<Item> = mutableListOf()
    private lateinit var adaptader: ItemAdapter
    private lateinit var auth: FirebaseAuth

    private fun obtenerNombre(){
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user != null){
            val nombre = user.displayName
            if(nombre!=null){
                val navigationView = binding.navigationView
                val navHeaderView = navigationView.getHeaderView(0)
                val nombreUsuario:TextView = navHeaderView.findViewById(R.id.nombreUsuarioCabecera)
                nombreUsuario.text = nombre
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View{
        // Inflate the layout
        binding = FragmentScaffoldBinding.inflate(inflater, container, false)
        adaptader = ItemAdapter(amigos) // Inicializa adaptador aquí

        obtenerNombre()

        return binding.root
    }

    //funcion para buscar
    private fun buscar(query:String){
        Toast.makeText(activity,"Buscando $query", Toast.LENGTH_SHORT).show()
    }

    private fun performSearch(query:String){
        val filteredList = amigos.filter{item-> item.nombre.contains(query,ignoreCase = true)}
        adaptader.updateList(filteredList)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaptader = ItemAdapter(amigos)//añadi esto ahora para intentar resolver error


        /* TOOLBAR */
        /* Establece la Toolbar como nueva ActionBar */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            /* Infla la vista del menú */
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar,menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            buscar(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //este es para filtrar en tiempo real mientras escribe
                        newText?.let { performSearch(it) }
                        return true
                    }
                })
            }
            /* Gestiona evento onClick */
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        // Manejar la selección del item1
                        val searchView = menuItem.actionView as SearchView
                        searchView.isIconified = false // Expande la barra de búsqueda al hacer clic
                        true
                    }
                    R.id.action_settings -> {
                        // Manejar la selección del item2
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /* DRAWERLAYOUT */
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_scaffold) as NavHostFragment
        val navController = navHostFragment.navController
        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
                item -> when(item.itemId)
            {
                R.id.nav_home -> {
                    navController.navigate(R.id.listaFragment)
                    //binding.drawerLayout.closeDrawer(GravityCompat.START) //para cerrar cuando clicas
                    true
                }

                R.id.nav_dashboard -> {
                    navController.navigate(R.id.contactoFragment)
                    true
                }

                R.id.nav_notifications -> {
                    navController.navigate(R.id.favoritosFragment2)
                    true
                }

                else -> false
            }
        }

        /* BOTTOM NAVIGATION MENU */

        binding.bottomNavigation.setOnItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.bnm_home -> {
                    // Handle Home navigation
                    navController.navigate(R.id.listaFragment)
                    true
                }
                R.id.bnm_dashboard -> {
                    // Handle Dashboard navigation
                    navController.navigate(R.id.contactoFragment)
                    true
                }
                R.id.bnm_notifications -> {
                    // Handle Notifications navigation
                    navController.navigate(R.id.tabFragment)
                    true
                }
                //podemos añadir un boton para salir de la sesión
                else -> false
            }
        }

    }
}