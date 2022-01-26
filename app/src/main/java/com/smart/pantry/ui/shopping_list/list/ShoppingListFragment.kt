package com.smart.pantry.ui.shopping_list.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.smart.pantry.R
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentShoppingListBinding
import com.smart.pantry.utils.setup
import org.koin.android.ext.android.inject

const val DATA_EXTRA :String = "data_item"

class ShoppingListFragment : BaseFragment() {

    override val _viewModel: ShoppingListViewModel by inject()

    lateinit var binding: FragmentShoppingListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_shopping_list, container, false
            )

        binding.viewModel = _viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setupRecyclerView()

        binding.addShoppingListFab.setOnClickListener{
            Navigation.findNavController(binding.root)
                .navigate(R.id.navigation_save_shopping_list)
        }

        binding.refreshLayout.setOnRefreshListener { refreshData() }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun setupRecyclerView() {
        val adapter = ShoppingListAdapter { data ->
            val bundle = Bundle().apply {this.putSerializable(DATA_EXTRA,data) }

            Navigation.findNavController(binding.root)
                .navigate(R.id.navigation_detail_shopping_list,bundle)
        }
        binding.shoppingListReciclerView.setup(adapter)
    }

    private fun refreshData()  = _viewModel.loadShoppingList()

}