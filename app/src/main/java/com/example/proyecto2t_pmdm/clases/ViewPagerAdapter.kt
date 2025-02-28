package com.example.proyecto2t_pmdm.clases

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyecto2t_pmdm.fragments.ContactoFragment
import com.example.proyecto2t_pmdm.fragments.FavoritosFragment
import com.example.proyecto2t_pmdm.fragments.ListaFragment

class ViewPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> ListaFragment()
            1 -> FavoritosFragment()
            else -> ListaFragment()
        }
    }


}