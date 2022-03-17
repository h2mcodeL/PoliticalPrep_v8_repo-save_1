package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    private val TAG = "Representatives" //used for tesing items

    //TO DO: Declare ViewModel
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        //add constant for Location
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    // Fragment for OnCreate -
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View/*?*/ {

        //TO DO: Establish bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        //setDisplayHomeAsUpEnabled(true)
        //initialise viewmodel
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        binding.viewModel = viewModel   //bind the xml view to the viewmodel for data binding
        binding.lifecycleOwner = this

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext()) //this option is for fragments, there is an equivalent for activity

        //set up the adapter here, however the item should be in Activity below
        //TO DO: Define and assign Representative adapter

     /**   binding.buttonSearch.setOnClickListener {
           // viewModel.getRepresentatives()
           // getLocationArea()
            hideKeyboard()  //the items have been entered, so collapse keyboard
            if (binding.addressLine1.text.trim().isNullOrEmpty() ||
                    binding.city.text.trim().isNullOrEmpty() ||       //isNullOrEmpty() ||
                    binding.zip.text.trim().isEmpty() ||        //isNullOrEmpty() ||
                    binding.state.selectedItem.toString().trim().isEmpty())        //.isNullOrEmpty())
            {
                Toast.makeText(requireContext(), "Fill all required gaps", Toast.LENGTH_SHORT)
                        .show()
            } else {
                viewModel.getAddressFromGeoLocation(
                        Address(
                                line1 = binding.addressLine1.text.toString(),
                                line2 = binding.addressLine2.text.toString(),
                                city = binding.city.text.toString(),
                                state = binding.state.selectedItem.toString(),
                                zip = binding.zip.text.toString()))
                viewModel.getRepresentatives()
            }
        }

        //TO DO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
          // ---  if(checkLocationPermissions()) {        //check location permnissions enabled
            getLocation()
            //    getLocationArea()
      //----  } else {
          //----  checkLocationPermissions() }
     }
     **/

        //create adapter for recyclerview, then bind to the recycler in the xml layout
        val repsAdapter = RepresentativeListAdapter()
        binding.representativeList.adapter = repsAdapter

        //get the click in the reps adapters
      // val repsClick = RepresentativeListAdapter.RepresentativeClickListener()


        //TO DO: Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            it?.let {
                repsAdapter.submitList(it)
            }
        })

//generate array for the cities
        ArrayAdapter.createFromResource(requireContext(), R.array.states,
                android.R.layout.simple_spinner_item).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.state.adapter = arrayAdapter
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                //If request is cancelled, the result arrays are empty
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //Permission is granted. Continue workflow, we can get our location
                    getLocation() //CHECK
                } else {
                    Snackbar.make(requireView(),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_LONG)
                            .setAction("Settings") {
                                startActivity(Intent().apply {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                })
                            }.show()
                }
                return
            }
        }
    }


    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return false
        }
    }

    //this will check if the user has granted permission for location
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (isPermissionGranted()) {
            fusedLocationProviderClient.lastLocation.
            addOnSuccessListener { location : Location? ->
                location?.let {
                val address =  getGeoLocation((location))
                viewModel.getAddressFromGeoLocation(address)
                    Toast.makeText(context, "Permission given", Toast.LENGTH_SHORT).show()
            } } }
        else {
            isPermissionGranted()
            Toast.makeText(requireContext(), "Are permissions granted.", Toast.LENGTH_SHORT)
                    .show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this


        binding.buttonSearch.setOnClickListener {
            // viewModel.getRepresentatives()
            // getLocationArea()
            hideKeyboard()  //the items have been entered, so collapse keyboard
            //here we are checking the text box and not actual data in the viewmodel
            if (binding.addressLine1.text.trim().isNullOrEmpty() ||
                    binding.city.text.trim().isNullOrEmpty() ||       //isNullOrEmpty() ||
                    binding.zip.text.trim().isEmpty() ||        //isNullOrEmpty() ||
                    binding.state.selectedItem.toString().trim().isEmpty())        //.isNullOrEmpty())
            {
                Toast.makeText(requireContext(), "Fill all required gaps", Toast.LENGTH_SHORT)
                        .show()
            } else {
                viewModel.getAddressFromGeoLocation(
                        Address(    //now populate the address with the entered data
                                line1 = binding.addressLine1.text.toString(),
                                line2 = binding.addressLine2.text.toString(),
                                city = binding.city.text.toString(),
                                state = binding.state.selectedItem.toString(),
                                zip = binding.zip.text.toString()))
                viewModel.getRepresentatives()
            }
        }

        //TO DO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            // ---  if(checkLocationPermissions()) {        //check location permnissions enabled
            getLocation()
            //    getLocationArea()
            //----  } else {
            //----  checkLocationPermissions() }
        }
    }




//    @SuppressLint("MissingPermission")
//    private fun getLocationArea() {
//        if (checkLocationPermissions()) {
//            LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
//                if (it == null) {
//                    Toast.makeText(requireContext(), "Error with getting location.", Toast.LENGTH_SHORT)
//                            .show()
//                } else {
//                    viewModel.getAddressFromGeoLocation(getGeoLocation(it))
//                }
//            }
//        } else {
//            requestPermissions(
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_LOCATION_PERMISSION)
//        }
//    }


    /**    private fun enableLocation() { try the one above
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            Log.i("Something", "Check")
            viewModel.getAddressFromGeoLocation()

        }
    }
*/

//    private fun searchLocation() {
//
//        if (isPermissionGranted())
//            LocationServices.getFusedLocationProviderClient(requireContext())
//                    /*.lastLocation.*/
//        if (ActivityCompat.checkSelfPermission(requireContext(),
//                        Manifest.permission.ACCESS_FINE_LOCATION) !=
//                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationProviderClient.getCurrentLocation(1,p1)
//                .addOnSuccessListener { location: Location? ->
//                        viewModel.getAddressFromList(viewModel.address)
//                    }
//    }



    //TO DO: Get location from LocationServices
    private fun getGeoLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude,1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    //this is for the address input
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}