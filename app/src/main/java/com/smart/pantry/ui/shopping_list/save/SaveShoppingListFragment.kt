package com.smart.pantry.ui.shopping_list.save

import android.os.Bundle
import android.view.MenuInflater
import android.view.Menu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smart.pantry.R
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentSaveShoppingListBinding
import org.koin.android.ext.android.inject


class SaveShoppingListFragment : BaseFragment() {

    override val _viewModel: SaveShoppingListViewModel by inject()

    private lateinit var binding: FragmentSaveShoppingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_save_shopping_list,
                container,
                false
            )
        setHasOptionsMenu(true)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_shopping_list -> {
                _viewModel.saveShoppingList(
                    binding.addShoppingListTittleText.text.toString(),
                    binding.addShoppingListDescriptionText.text.toString(),
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