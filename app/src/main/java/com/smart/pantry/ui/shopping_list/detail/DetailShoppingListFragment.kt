package com.smart.pantry.ui.shopping_list.detail

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.smart.pantry.R
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentDetailShoppingListBinding
import com.smart.pantry.ui.shopping_list.list.DATA_EXTRA
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import org.koin.android.ext.android.inject


class DetailShoppingListFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailShoppingListBinding

    override val _viewModel: DetailShoppingListViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_detail_shopping_list,
                container,
                false
            )

        val data = arguments?.getSerializable(DATA_EXTRA) as ShoppingListDataItem
        _viewModel.start(data)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setHasOptionsMenu(true)

        binding.editShoppingListFab.setOnClickListener {
            val bundle = Bundle().apply {this.putSerializable(DATA_EXTRA,data) }
            findNavController().navigate(R.id.navigation_edit_shopping_list,bundle)
        }

        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                _viewModel.deleteShoppingList()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

}