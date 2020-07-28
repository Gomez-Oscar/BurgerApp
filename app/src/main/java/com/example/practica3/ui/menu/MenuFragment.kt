package com.example.practica3.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practica3.R
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_burger.setOnClickListener{
            findNavController().navigate(R.id.action_MenuFragment_to_BurgerFragment)

        }

        iv_perros.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_PerrosFragment)
        }

        iv_complementos.setOnClickListener {
                findNavController().navigate(R.id.action_MenuFragment_to_ComplementosFragment)
        }

        iv_bebidas.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_BebidasFragment)
        }

    }

}