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
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
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

  //  private lateinit var geofencingClient: GeofenceBroadcastReceiver



   // private val REQUEST_PERMISSION_REQUEST_CODE = 34

    //check if phone is running Q or later
  //  private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

   // ----- private lateinit var geofenceClient: GeofencingClient

//    private val geofencePendingIntent: PendingIntent by lazy {
//        val intent = Intent(requireActivity(), GeofenceBroadcastReceiver::class.java)
//        intent.action = ACTION_GEOFENCE_EVENT
//        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }


    // Fragment for OnCreate -
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View/*?*/ {

        //TO DO: Establish bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        //setDisplayHomeAsUpEnabled(true)
        //initialise viewmodel
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        //set up the adapter here, however the item should be in Activity below
        //TO DO: Define and assign Representative adapter

        binding.buttonSearch.setOnClickListener {
            viewModel.getRepresentatives()
            hideKeyboard()
        }
        //TO DO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            enableLocation()
        }

        //create adapter for recyclerview, then bind to the recycler in the xml layout
            val repsAdapter = RepresentativeListAdapter()
            binding.representativeList.adapter = repsAdapter

            //TO DO: Populate Representative adapter
            viewModel.representatives.observe(viewLifecycleOwner, Observer {
                it?.let {
                    repsAdapter.submitList(it)
                }
            })

//generate array for the cities
        ArrayAdapter.createFromResource(requireContext(), R.array.states,
                android.R.layout.simple_spinner_item).also {
                    arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.state.adapter = arrayAdapter
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //checkPermissionsAndStart()
        checkLocationPermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                //If request is cancelled, the result arrays are empty
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //Permission is granted. Continue workflow
                    //we can get our location
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
//            checkDeviceLocationSettings(false)
//        }
//    }



    //fun checkPermissionsAndStart() : Boolean{
        private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return false
        }
    }
      //  if (foregroundAndBackgroundLocationPermissionApproved()) {
       //     checkDeviceLocationSettings()
      //  } else {
      //      requestForegroundAndBackgroundLocationPermission()
      //  }
   // }


//    @TargetApi(29)
//    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
//        val foregroundLocationApproved = (
//                PackageManager.PERMISSION_GRANTED ==
//                        ActivityCompat.checkSelfPermission(this.requireContext(),
//                                android.Manifest.permission.ACCESS_FINE_LOCATION))
//
//        val backgroundPermissionApproved =
//                if (runningQOrLater) {
//                    PackageManager.PERMISSION_GRANTED ==
//                            ActivityCompat.checkSelfPermission(
//                                    this.requireContext(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                            )
//                } else  {
//                    true
//                }
//        return foregroundLocationApproved && backgroundPermissionApproved
//    }


//    private fun checkDeviceLocationSettings(resolve:Boolean = true) {
//        //we need to check if the devices location is on. Create a location request
//        val locationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_LOW_POWER
//        }
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//
//        //use location services to get the settings client
//        val settingsClient = LocationServices.getSettingsClient(this.requireContext())
//        val locationSettingsResponseTask =
//                settingsClient.checkLocationSettings(builder.build())
//
//        // we are most interested in finding out if the location settings are off. add a onFailure() and check if it is of type startResolution....
//        locationSettingsResponseTask.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException && resolve) {
//                try {
//                    exception.startResolutionForResult(requireActivity(),
//                            REQUEST_TURN_DEVICE_LOCATION_ON)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
//                }
//            } else {
//                Snackbar.make(
//                        this.requireView(),
//                        R.string.location_required_error,
//                        Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok) {
//                    checkDeviceLocationSettings()
//                }.show()
//            }
//        }
//        locationSettingsResponseTask.addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.i("Granted", "Location Granted")
//                //addGeofenceForClue()
//            }
//        }
//    }

//    @TargetApi(29)
//    private fun requestForegroundAndBackgroundLocationPermission() {
//
//        if (foregroundAndBackgroundLocationPermissionApproved())
//            return
//        var permissionsArray = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        val resultCode = when {
//            runningQOrLater -> {
//                permissionsArray += android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
//            }
//            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//        }
//        ActivityCompat.requestPermissions(
//                requireActivity(),
//                permissionsArray,
//                resultCode)
//    }


    //this will check if the user has granted permission for location
    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission (
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    //Enble location
//    private fun enableLocation() {
//        if (isPermissionGranted()) {
//            if (ActivityCompat.checkSelfPermission(requireContext(),
//                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(requireContext(),
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return
//            }
//            Log.i("Something", "Check")
//            viewModel.getAddressFromGeoLocation()
//
//        }
//    }

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

//    private fun getAddressFromList() {
//        binding.apply {
//            buttonSearch.setOnClickListener {
//                val address = "${addressLine1.text} ${addressLine2.text} ${city.text} ${state.prompt} ${zip.text}"
//                viewModel?.getRepresentatives(address)
//                representative
//
//            }
//             viewModel.getRepresentatives(viewModel.address.value.toString())
//        }
//
//    }

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        //TO DO: Get location from LocationServices
        //TO DO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient/*(requireActivity())*/    (requireContext())

                    .lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val geoLocation = geoCodeLocation(location)
                            viewModel.getAddressFromGeoLocation(geoLocation)

                            //viewModel.getRepresentatives(viewModel.address.value.toString())
                        }
                    }
           // geoCodeLocation()
        } else {
            //  ActivityCompat.requestPermissions(context as Activity, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }
  //  }


//    private fun getGeoLocation(location: Location): Address {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        return geocoder.getFromLocation(location.latitude, location.longitude,1)
//                .map { address ->
//                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
//                }
//                .first()
//    }


    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
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

        //TO DO: Add Constant for Location request - these are the foreground/background etc
     //   private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
       // private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
      //  private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
      //  private const val LOCATION_PERMISSION_INDEX = 0
      //  private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
      //  private const val REQUEST_CODE_LOCATION_SETTING = 1   //need to check this
        private const val REQUEST_LOCATION_PERMISSION = 1

