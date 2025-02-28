package com.example.proyecto2t_pmdm.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.proyecto2t_pmdm.R
import com.example.proyecto2t_pmdm.clases.ViewPagerAdapter
import com.example.proyecto2t_pmdm.databinding.FragmentScaffoldBinding
import com.google.android.material.search.SearchView
import com.google.android.material.tabs.TabLayoutMediator

class ScaffoldFragment : Fragment() {
    private lateinit var binding: FragmentScaffoldBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View?{
        // Inflate the layout
        binding = FragmentScaffoldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* TOOLBAR */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        //para las tabs
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        {
                tab, position ->    tab.text = when (position)
            {
                0 -> getString(R.string.lista_amigos)
                1 -> getString(R.string.favoritos)
                else -> ""
            }
        }.attach()


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar,menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as SearchView
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        // Manejar la selección del item1
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
                    navController.navigate(R.id.listaFragment) //cambiar este por la lista
                    true
                }
                R.id.bnm_dashboard -> {
                    // Handle Dashboard navigation
                    navController.navigate(R.id.contactoFragment)
                    true
                }
                R.id.bnm_notifications -> {
                    // Handle Notifications navigation
                    navController.navigate(R.id.favoritosFragment2)
                    true
                }
                //podemos añadir un boton para salir de la sesión
                else -> false
            }
        }

    }
}