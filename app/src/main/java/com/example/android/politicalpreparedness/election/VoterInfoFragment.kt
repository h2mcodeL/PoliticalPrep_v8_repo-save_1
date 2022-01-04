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
import kotlinx.android.synthetic.main.fragment_voter_info.*

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //reference to the application that this fragment is attached to
        val application = requireNotNull(this.activity).application

        //using this method we bind directly to the view using the binding reference
        val binding = FragmentVoterInfoBinding.inflate(inflater)

         //binding.lifecycleOwner = this

        //TO DO: Add ViewModel values and create ViewModel
        val dataSource = ElectionDatabase.getInstance(application).electionDao  //this gives access to teh DAO

       // val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())      //were using navigation safeArgs
        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())   //.selectedElection
        val electionId = bundle.argElectionId
        val division = bundle.argDivision
            Log.i("The ElectionID", "$electionId")

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

        //TO DO: Handle loading of URLs

        //TO DO: Handle save button UI state     -- ###### refer to the loading button app

        viewModel.followedElection.observe(viewLifecycleOwner, Observer {
            when (it == true) {
                viewModel.followedElection.equals(true) -> followbutton.text = getString(R.string.unfollow_button)
                viewModel.followedElection.equals(false) -> followbutton.text = getString(R.string.follow_button)
            }
        })






//        viewModel.followedElection.observe(viewLifecycleOwner, Observer { isFollowed ->
//            isFollowed?.let {
//                //using when is cleaner
//                if (viewModel.followedElection.equals(true)) {
//                    binding.followbutton.text = getString(R.string.unfollow_button)
//                    binding.followbutton.setTextColor(resources.getColor(R.color.design_default_color_primary))   //  nice colour change
//                } else {
//                    binding.followbutton.text = getString(R.string.follow_button)
//                    binding.followbutton.setTextColor(resources.getColor(R.color.design_default_color_primary_dark))
//                }
//            }
//        })



//        viewModel.followedElection.observe(viewLifecycleOwner, Observer { //isFollowed ->
//            when {
//                //when button is already followed, unfollow
//                viewModel.followedElection.equals(true) -> followbutton.text = resources.getText(R.string.unfollow_button)
//                //when button is not followedf follow
//                viewModel.followedElection.equals(false) -> followbutton.text = resources.getText(R.string.follow_button)
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

