package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
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

    private val db = getInstance(application) //??

    //get access to repo
    private val electionsRepository = ElectionsRepository(db)

//     private val _election = MutableLiveData<Election>()
//     val election : LiveData<Election>
//     get() = _election



    private val _followedElection = MutableLiveData<Boolean>()//true)
    val followedElection: LiveData<Boolean>
        get() = database.isElectionFollowed(electionId)
      //  get() = _followedElection

//    private val _followElection = MutableLiveData<Election?>()
//    val followElection: LiveData<Boolean>
//        get() = db.electionDao.isElectionFollowed(electionId)

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

    init {
        getInfo()
        Log.i("Selected Info", "$selectedVoterInfo")
    }

    //get the voter info
    private fun getInfo() {
        viewModelScope.launch {
            try {
                var address = "country:${division.country}"
                address += if (division.state.isNotBlank() && division.state.isNotEmpty()) {
                    "/state:${division.state}"
                } else {
                   address += "/state:ca"
                }
                _selectedVoterInfo.value = CivicsApi.retrofitService.getVoterInfo(
                        address, electionId)
                Log.i("SELECTED VOTER", "${selectedVoterInfo.value}")
            } catch (e: UnknownHostException) {
            }
        }
    }

    //set up click function for voting locations
    fun votingLocationsClick() {
        try {
            //if (_selectedVoterInfo.value != null) {
            _voterLocations.value = _selectedVoterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
            //  } else {
        } catch (e: NullPointerException) {
            Log.i("Location URL Click", "${_voterLocations.value}")
            // _selectedVoterInfo.value = null
            _validSite.value = false
        }
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




    //this simply says, is the election followed its a LiveData. then using the getter, get it from the database
   //---- private val _isElectionFollowed: LiveData<Int>
   //------ get() = database.isElectionFollowed(electionId)

   // private val isElectionFollowed = Transformations.map(_isElectionFollowed) { followElec ->
    private val isElectionFollowed = Transformations.map(_followedElection) {followElection ->
        followElection?.let {
            followElection.equals(1)
        }
    }

    //TO DO: Populate voter info -- hide views without provided data.
    /**
    Hint: You will need to ensure proper data is provided from previous fragment.
     */

    fun followButton() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if(isElectionFollowed.value == true) {
            //if (_followedElection.value != null) {
               // database.unfollowElection(electionId)
                   electionsRepository.unfollowElection(electionId)
                Log.i("FOLLOW ELECTION", "$electionId")
            } else {
              //  electionsRepository.saveElection(electionId)
                database.followElection(electionId)

            }
        }
    }

//COMMENT OUT THIS FUNCTION AS ITS ACOPY OF THE ABOVE..
//     fun followButton() {
//         viewModelScope.launch {
//             if (database.isElectionFollowed(electionId).equals(true)) {
//                //_followedElection.setValue(true)       //election is already followed
//                 _followedElection.postValue(true)
//                 electionsRepository.unfollowElection(electionId)       //therefore unfollow
//
//             } else {
//                 electionsRepository.saveElection(electionId)       //else follow the election
//                 _followedElection.postValue(false)
//             //}
//             //--_followedElection.value = electionsRepository.isSaved(election)   //need to create the function isSaved....
//
//         }
//     }



}}

//TO DO: Add var and methods to support loading URLs

//TODO: Add var and methods to save and remove elections to local database

//TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

/*** Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.     */

