package com.example.android.politicalpreparedness.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Concrete implementation of a data source as a database.
 * @param database the database should link to the DAO
 */
class ElectionsRepository(private val database: ElectionDatabase) {

    /*** Get the elections list from the local db, using the DAO. Its possibly a long running task so use dispatchers.IO*/

    //get all saved elections
    val allElections: LiveData<List<Election>> = database.electionDao.getAllElections()

    //all followed elections
    val allFollowedElections: LiveData<List<Election>> = database.electionDao.getFollowedElections()


    //save the selected election
    suspend fun saveElection(id: Int) {
        withContext(Dispatchers.IO) {
            val followed = FollowedElection(id)
            database.electionDao.insertFollowedElection(followed)
        }
    }

  //  suspend fun unfollowElection(election: Election) {
    suspend fun unfollowElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.unfollowElection(id)       //(election.id)
        }
    }


    suspend fun refreshElectionsList() {
        withContext(Dispatchers.IO) {
            try {
                val elecResponses = CivicsApi.retrofitService.getElectionResults()
                val results = elecResponses.elections
                database.electionDao.insertAll(*results.toTypedArray()) //dao needs to be vararg elec: Election - not database
                //  database.electionDao.insertAllElections(*result.toTypedArray()) //need to check on this
                Log.d(TAG, results.toString())
            } catch (e: Exception) {
                e.printStackTrace()
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


/*
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(election: Election) {
       // val followElection =
        //databaseDao.insert(election)
        database.electionDao.insert(election)
    }

 */


}