package com.smart.pantry

import androidx.multidex.MultiDexApplication
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.local.LocalDB
import com.smart.pantry.data.local.ShoppingListLocalRepository
import com.smart.pantry.ui.shopping_list.detail.DetailShoppingListViewModel
import com.smart.pantry.ui.shopping_list.edit.EditShoppingListViewModel
import com.smart.pantry.ui.shopping_list.list.ShoppingListViewModel
import com.smart.pantry.ui.shopping_list.save.SaveShoppingListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PantryApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            single {
                ShoppingListViewModel(
                    get(),
                    get() as ShoppingListDataSource
                )
            }
            single {
                EditShoppingListViewModel(
                    get(),
                    get() as ShoppingListDataSource
                )
            }
            single {
                SaveShoppingListViewModel(
                    get(),
                    get() as ShoppingListDataSource
                )
            }
            single {
                DetailShoppingListViewModel(
                    get(),
                    get() as ShoppingListDataSource
                )
            }

            single { LocalDB.createShoppingListDao(this@PantryApp) }
            single { ShoppingListLocalRepository(get()) as ShoppingListDataSource }

        }

        startKoin {
            androidContext(this@PantryApp)
            modules(listOf(myModule))
        }
    }
}