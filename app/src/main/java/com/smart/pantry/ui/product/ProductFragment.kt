package com.smart.pantry.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentProductListBinding
import com.smart.pantry.ui.shopping_list.list.DATA_EXTRA
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import com.smart.pantry.utils.setup
import org.koin.android.ext.android.inject

class ProductFragment : BaseFragment() {

    override val _viewModel: ProductViewModel by inject()

     val viewModel: ItemViewModel by inject()

    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)

        val data = arguments?.getSerializable(DATA_EXTRA) as ShoppingListDataItem

        viewModel.setShoppingList(data)

        binding.viewModel = _viewModel

        binding.itemView = viewModel

        setObservers()
        return binding.root
    }

    private fun setObservers() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val suggestionAdapter = ProductItemRecyclerViewAdapter { data ->
            viewModel.saveItem(data){viewModel.loadItems()}
        }
        binding.suggestionList.setup(suggestionAdapter)

        val addedAdapter = ProductItemRecyclerViewAdapter { data ->

        }
        binding.addedList.setup(addedAdapter)
    }

    override fun onResume() {
        super.onResume()
        _viewModel.loadProducts()
        viewModel.loadItems()
    }
}