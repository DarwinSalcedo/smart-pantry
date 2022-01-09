package com.smart.pantry.ui.smart_nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smart.pantry.databinding.FragmentShoppingListBinding
import com.smart.pantry.databinding.FragmentSmartNutritionBinding

class SmartNutritionFragment : Fragment() {

    private var _binding: FragmentSmartNutritionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentSmartNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}