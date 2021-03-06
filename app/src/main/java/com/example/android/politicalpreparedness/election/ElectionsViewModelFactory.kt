package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase

//TO DO: Create Factory to generate ElectionViewModel with provided election datasource

class ElectionsViewModelFactory(
        private val dataSource: ElectionDatabase,         //ElectionDao,
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(dataSource, application) as T
        }
        //TO DO("Not yet implemented")
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

