package com.smart.pantry.ui.shopping_list.save

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.smart.pantry.R
import com.smart.pantry.base.BaseFragment
import com.smart.pantry.databinding.FragmentSaveShoppingListBinding
import com.smart.pantry.ui.shopping_list.list.DATA_EXTRA
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
        binding.lifecycleOwner = viewLifecycleOwner
        setObserver()
        setEvents()
        return binding.root
    }

    private fun setEvents() {
        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.createButton.setOnClickListener {
            saveShoppingList()
        }
    }

    private fun setObserver() {
        _viewModel.navigate.observe(viewLifecycleOwner,{  navigate ->
            if(navigate){
                  val bundle = Bundle().apply {this.putSerializable(DATA_EXTRA,_viewModel.getShoppingList()) }
                  Navigation.findNavController(binding.root)
                      .navigate(R.id.navigation_add_products,bundle)
              }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_shopping_list -> {
                saveShoppingList()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun saveShoppingList() {
        _viewModel.saveShoppingList(
            binding.addShoppingListTittleText.text.toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

}