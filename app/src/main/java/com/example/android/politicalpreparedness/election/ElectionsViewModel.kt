package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch


class ElectionsViewModel(
        val database: ElectionDatabase, application: Application) : AndroidViewModel(application) {

    //create the repository
    private val electionsRepository = ElectionsRepository(database)

    private val savedElectionRepository = ElectionsRepository(database)

    val upcomingElections: LiveData<List<Election>>
        get() = electionsRepository.allElections

    //get the list of elections that are followed in the db
    val electionFollowed: LiveData<List<Election>>
        get() = savedElectionRepository.allFollowedElections

    //TO DO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    //TO DO: Create functions to navigate to saved or upcoming election voter info

    //navigate to the selected voterinfo from the election mutablelivedata externalized as livedata, links to display below
    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo


    //this is for the saved elections
    private val _savedElection = MutableLiveData<Election>()
    val savedElection: LiveData<Election>
        get() = _savedElection

    init {
        viewModelScope.launch {
            electionsRepository.refreshElectionsList()
        }
    }

    //new code added for Adapter
    fun onElectionClicked(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun onElectionNavigated(election: Election) {
        _navigateToVoterInfo.value = null
    }

    //this allows navigation to the voter info screen from _naviga.... above
    fun displayElectionDetails(election: Election) {
        _navigateToVoterInfo.value = election
    }

    //clean up the navigation
    fun displayElectionDetailsComplete() {
        _navigateToVoterInfo.value = null
    }

    fun displaySavedElection(savedElection: Election) {
        _savedElection.value = savedElection
    }

    fun displaySavedElectionComplete() {
        _savedElection.value = null
    }

    //recent edit check
    private fun checkForSavedElection() {
        viewModelScope.launch {
            //_savedElection.value = followThisElection()
            electionFollowed
        }
    }


}