package com.example.android.politicalpreparedness.representative

//import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _repImage = MutableLiveData<Representative>()
    val repImage: LiveData<Representative>
    get() = _repImage

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    //use this for testing the network connection
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response


    //TO DO: Establish live data for representatives and address
    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<Int>()

    //TO DO: Create function to fetch representatives from API from a provided address

    fun getRepresentatives() {///*address: String*/){
        viewModelScope.launch {
            _address.value?.let {
                try {
                 //   val response = getRepresentatives(it.toFormattedString())
                ////    val offices = response.offices
                //    val officials = response.officials

                    val address = _address.value!!.toFormattedString()
                    val (office, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                    _representatives.value = office.flatMap { office1 ->
                        office1.getRepresentatives(officials)
                    }
                } catch (e: Exception) {
                    _response.value = "Failure: ${e.message}}"
                }
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :
     */


    //TO DO: Create function get address from geo location
    fun getAddressFromGeoLocation(address: Address) {
        _address.value = address
    }

    init {
        _address.value = Address("", "", "", "", "") //state "California"
    }


    fun onClear() {
        /*
        addressLine1.value = null
        addressLine2.value = null
        city.value = null
        state.value = null
        zip.value = null*/
    }
}
