package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TO DO: Add binding values
        val binding: FragmentLaunchBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_launch, container, false)


        binding.lifecycleOwner = this

        //we can use FragmentLaunchBinding as we have the <layout> tag in the xml file.

        //this links the elections button to the navigation
        binding.upcomingButton.setOnClickListener { navToElections() }

        //this links the representative button onclick to the navRepresentatives
        binding.representativeButton.setOnClickListener { navToRepresentatives() }

        return binding.root
    }

    //without the directions, we can navigate to a location id. i.e. using the view we do the following, using the action id.
    // view.findNavController().navigate(R.id.action_launchFragment_to_representativeFragment)
    //to get LaunchFragmentDirections, we need to add the arrows in the navgraph for the approriate fragment directions
    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())

    }

    private fun navToRepresentatives() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
