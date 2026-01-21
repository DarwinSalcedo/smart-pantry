package com.smart.pantry.di

/**
 * All Koin modules for the application
 */
val appModules = listOf(
    platformDatabaseModule,
    databaseModule,
    repositoryModule,
    viewModelModule
)
