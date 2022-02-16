package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //reference to the application that this fragment is attached to
        val application = requireNotNull(this.activity).application

        //using this method we bind directly to the view using the binding reference, and view binding
        val binding = FragmentVoterInfoBinding.inflate(inflater)

        //TO DO: Add ViewModel values and create ViewModel
        val dataSource = ElectionDatabase.getInstance(application).electionDao  //this gives access to teh DAO

        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val division = bundle.argDivision
            Log.i("The ElectionID", "$electionId")

        //the voterInfo screen shows information on a single instance, therefore the viewmodel when created need to have access to
        //the data on that instance.hence the use of the factory
        val viewModelFactory = VoterInfoViewModelFactory(dataSource, electionId, division, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        //handle url loading
        viewModel.voterLocations.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                it.let {
                    loadUrl(it)     //call the url method
                    // viewModel.votingLocationsClick()   //clean up the navigation
                    viewModel.votingLocationsNavigated()      //voterLocationsNavigated()
                    Log.i("THE VOTING URL", it)
                }
            } else {
                Toast.makeText(context, "Sorry no URL", Toast.LENGTH_SHORT).show()
            }
        })

        //ballot URL

        viewModel.ballotInformation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                it.let {
                    // Log.i("THE BALLOT URL", it)
                    loadUrl(it)
                    viewModel.ballotInformationClick()
                    viewModel.ballotUrlNavigated()
                }
            } else {
                Toast.makeText(context, "Sorry no URL", Toast.LENGTH_SHORT).show()
            }
        })

        //we need to get the election id from the electionFragment for use here
        //to use safeArgs, we need to add the arguments to the navGraph...
        // val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId   ----- refer to above

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        //check if this is done here or in the viewmodel

        //TO DO: Handle save button UI state

        viewModel.isElectionFollowed.observe(viewLifecycleOwner, Observer { isFollowed ->
            isFollowed?.let {
                //using when is cleaner
                if (viewModel.isElectionFollowed.value != false) {
                    binding.followbutton.text = getString(R.string.unfollow_button)
                    binding.followbutton.setTextColor(resources.getColor(R.color.colorAccent))
                    Toast.makeText(context, getString(R.string.electionfollowed), Toast.LENGTH_SHORT).show()


                } else {
                    binding.followbutton.text = getString(R.string.follow_button)
                    binding.followbutton.setTextColor(resources.getColor(R.color.design_default_color_primary_dark))
                    Toast.makeText(context, getString(R.string.electionunfollowed), Toast.LENGTH_SHORT).show()
                }
            }
        })

//        viewModel.isElectionFollowed.observe(viewLifecycleOwner, Observer { followElection ->
//            if (followElection.equals(true)) {
//                binding.followbutton.text = getString(R.string.unfollow_button)
//            } else {
//                binding.followbutton.text = getString(R.string.follow_button)
//            }
//        })


        //TO DO: cont'd Handle save button clicks
        return binding.root
    }

    //TO DO: Create method to load URL intents

    @SuppressLint("QueryPermissionsNeeded")
    private fun loadUrl(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }
}