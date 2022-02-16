package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class VoterInfoViewModel(private val database: ElectionDao,
                         private val electionId: Int,
                         private val division: Division, application: Application) : ViewModel() {

    //TO DO: Add var and methods to populate voter info
    private val election: Election? = null

    private val elect = MutableLiveData<Election?>()

    private val db = getInstance(application)   //what is this for?

    //get access to repo
    private val electionsRepository = ElectionsRepository(db)       //this is not being used


    private val _followedElection = MutableLiveData<Int>()
    val followedElection: LiveData<Int>
        get() = database.isElectionsFollowed(electionId)

    //this is used for the select item
    //the id comes from the selected item
    private val _selectedVoterInfo = MutableLiveData<VoterInfoResponse>()
    val selectedVoterInfo: LiveData<VoterInfoResponse>
        get() = _selectedVoterInfo

    //Live data for the voterlocation url link
    private val _voterLocations = MutableLiveData<String?>()
    val voterLocations: LiveData<String?>
        get() = _voterLocations

    //Live data for the ballot url link
    private val _ballotInformation = MutableLiveData<String?>()
    val ballotInformation: LiveData<String?>
        get() = _ballotInformation

    //this is used for the unfollow election
    private val _removeElection = MutableLiveData<Election>()
    val removeElection: LiveData<Election>
        get() = _removeElection

    private val _navigateVoter = MutableLiveData<Election>()
    val navigateVoter: LiveData<Election>
        get() = _navigateVoter

    //reset the navigation variable
    fun doneNavigating() {
        _navigateVoter.value = null
    }

    private val _validSite = MutableLiveData<Boolean>(false)
    val validSite: LiveData<Boolean>
        get() = _validSite

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _status = MutableLiveData<Address>()
    val status: LiveData<Address>
        get() = _status


    init {
        getInfo()
        Log.i("Selected Info", "$selectedVoterInfo")
    }


//    private fun getInfo() {
//        viewModelScope.launch {
//           // try {
//                //var addr = _address.value!!.toFormattedString()
//               // var address = "country:${division.country}" this does not work, so dont use. it needs to be division.country, not "division.country
//            var address = division.country  //we need to create the variable
//
//                if (division.state.isNotBlank() && division.state.isNotEmpty()) {
//               address += "/state:${division.state}"
//                //"/state:${division.state}"
//         //   } else {
//          //     address +=  "/state:al"
//            }
//                _selectedVoterInfo.value = CivicsApi.retrofitService.getVoterInfo(address, electionId)
//
//                Log.i("SELECTED VOTER", "${selectedVoterInfo.value}")
//                Log.i("VOTER INFO", "$division.country")
//          //  }
//
//          //  catch (e: UnknownHostException) {
//          //      Log.i("Address Issues", division.country)
//          //  }
//            }
//        //}
//    }


    // val address = _address.value!!.toFormattedString()
    //get the voter info, launch a coroutine as this is a long running op
    private fun getInfo() {
        viewModelScope.launch {
            try {
                var address = division.country

                if (division.state.isNotBlank() && division.state.isNotEmpty()) {
                    address += "/state:${division.state}"

                } else {
                    address += "/state:al"
                }
                _selectedVoterInfo.value = CivicsApi.retrofitService.getVoterInfo(
                        address, electionId)
                Log.i("SELECTED VOTER", "${selectedVoterInfo.value}")
            } catch (e: UnknownHostException) {

                Log.i("VoterInfo", "")

            }
        }
    }

    //set up click function for voting locations
    fun votingLocationsClick() {
        // try {
        //if (_selectedVoterInfo.value != null) {
        _voterLocations.value = _selectedVoterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
        //  } else {
        //  } catch (e: NullPointerException) {
        Log.i("Location URL Click", "${_voterLocations.value}")
        // _selectedVoterInfo.value = null
        _validSite.value = false
        //    }
    }

    fun votingLocationsNavigated() {
        _voterLocations.value = null
    }

    fun ballotInformationClick() {
        if (_selectedVoterInfo.value != null) {
            _ballotInformation.value = _selectedVoterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
        } else {
            Log.i("The Ballot URL", "Cannot be found")
            _selectedVoterInfo.value = null
        }
    }

    fun ballotUrlNavigated() {
        _ballotInformation.value = null
    }

    private val _isElectionFollowed = MutableLiveData(false)
    val isElectionFollowed: LiveData<Boolean>
        get() = _isElectionFollowed

// }


    //TO DO: Populate voter info -- hide views without provided data.
    /**
    Hint: You will need to ensure proper data is provided from previous fragment.
     */

    fun followButton() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
             val elec = FollowedElection(electionId)
            if (isElectionFollowed.value != false) {
                deleteElection()
            } else {
                insert(elec)
            }}
        }
    }

    private fun insert(followelection: FollowedElection) {
                database.insertFollowedElection(followelection)
                _isElectionFollowed.value = true
    }

    private suspend fun deleteElection() {
        viewModelScope.launch {
          //  return@launch withContext(Dispatchers.IO) {
                //database.clearFollowed()
                database.unfollowElection(electionId)
                _isElectionFollowed.value = false
            }
      //  }
    }
}



//TO DO: Add var and methods to support loading URLs

//TO DO: Add var and methods to save and remove elections to local database

//TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

/*** Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database. */