package com.smart.pantry.ui.smart_nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smart.pantry.databinding.FragmentSmartNutritionBinding

class SmartNutritionFragment : Fragment() {

   lateinit var binding: FragmentSmartNutritionBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentSmartNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

}