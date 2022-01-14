package com.smart.pantry.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.smart.pantry.R


/**
 * Base Fragment to observe on the common LiveData objects
 */
abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val _viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()
        _viewModel.showErrorMessage.observe(this, {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        _viewModel.showToast.observe(this, {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        _viewModel.showSnackBar.observe(this, {
            showSnackbar(it)
        })
        _viewModel.showSnackBarInt.observe(this, {
            showSnackbar(getString(it))
        })

        _viewModel.navigationCommand.observe(this, { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
            }
        })
    }

    private fun showSnackbar(value : String){
        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.nav_view)!!
        Snackbar.make(bottomNavView, value, Snackbar.LENGTH_SHORT).apply {
            anchorView = bottomNavView
        }.show()
    }
}