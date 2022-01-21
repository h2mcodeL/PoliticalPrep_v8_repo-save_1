package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a database.
 * @param database the database should link to the DAO
 */
class ElectionsRepository(private val database: ElectionDatabase) {

    /*** Get the elections list from the local db, using the DAO. Its possibly a long running task so use dispatchers.IO*/

    val upcomingElections = MutableLiveData<List<Election>>()
    //get all saved elections
    val allElections: LiveData<List<Election>> = database.electionDao.getAllElections()

    //all followed elections
    val allFollowedElections: LiveData<List<Election>> = database.electionDao.getFollowedElections()

    suspend fun refreshElectionsList() {
        withContext(Dispatchers.IO) {
            try {
                val elecResponses = CivicsApi.retrofitService.getElectionResults()
                val results = elecResponses.elections
                database.electionDao.insertAll(*results.toTypedArray())

            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("Result cannot be found", "No Result!!")
            }
        }
    }

    suspend fun refreshFollowElection() {
        withContext(Dispatchers.IO) {
            try {
                database.electionDao.getFollowedElections()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}