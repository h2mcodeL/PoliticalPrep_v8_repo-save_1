package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    private lateinit var electViewModel: ElectionsViewModel
    private lateinit var binding: FragmentElectionBinding

    private lateinit var electionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TO DO: Add binding values
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_election, container, false)

        //get an instance of the application for use in the viewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = ElectionDatabase.getInstance(application)
        val viewModelFactory = ElectionsViewModelFactory(dataSource, application)

        //TO DO: Add ViewModel values and create ViewModel  (get a reference to the viewmodel associated with this fragment using providers
        electViewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        //this binds the name election from the xml file directly with the viewmodel
        binding.electionViewModel = electViewModel

        //binding observes livedata updates in the viewmodel
        binding.lifecycleOwner = this

        //Election list
        electionsAdapter = ElectionListAdapter(ElectionListAdapter.ElectionClickListener {
            findNavController().navigate(ElectionsFragmentDirections.actionShowElection(it.id, it.division))
            Log.i("CHECK DATA", "${it.id}, ${it.name}")
        })
        binding.upcomingElectionsList.adapter = electionsAdapter

        electViewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                electionsAdapter.submitList(it)
            }
        })


        //SAVED ELECTIONS
        savedElectionsAdapter = ElectionListAdapter(ElectionListAdapter.ElectionClickListener {
            findNavController().navigate(ElectionsFragmentDirections.actionShowElection(it.id, it.division))
        })

        binding.savedElectionsList.adapter = savedElectionsAdapter

        electViewModel.electionFollowed.observe(viewLifecycleOwner, Observer {
            it?.let {
                savedElectionsAdapter.submitList(it)
            }
        })

//        //TO DO: Refresh adapters when fragment loads
//        binding.refreshLayout.setOnRefreshListener { electViewModel.refreshList() }

        return binding.root
    }

}