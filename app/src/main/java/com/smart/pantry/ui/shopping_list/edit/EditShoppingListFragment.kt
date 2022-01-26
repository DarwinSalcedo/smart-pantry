package com.smart.pantry.ui.shopping_list.edit

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.smart.pantry.R
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentEditShoppingListBinding
import com.smart.pantry.ui.shopping_list.list.DATA_EXTRA
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import org.koin.android.ext.android.inject


class EditShoppingListFragment : BaseFragment() {

    override val _viewModel: EditShoppingListViewModel by inject()

    private lateinit var binding: FragmentEditShoppingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_edit_shopping_list,
                container,
                false
            )
        setHasOptionsMenu(true)
        val data = arguments?.getSerializable(DATA_EXTRA) as ShoppingListDataItem
        _viewModel.start(data)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_shopping_list -> {
                _viewModel.editShoppingList(
                    binding.addShoppingListTittleText.text.toString()
                )
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

}