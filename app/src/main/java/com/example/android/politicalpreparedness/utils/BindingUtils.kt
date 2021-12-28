package com.example.android.politicalpreparedness.utils

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse


//this is the overall binding Adapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

//used for the Voter Info query
@BindingAdapter("generalText")
fun TextView.setTitle(item: VoterInfoResponse?) {
    item?.let {
        text = item.election.name
    }
}


//create an extention function of TextView
@BindingAdapter("electionTitle")
fun TextView.setElectionTitle(item: Election?) {
    item?.let {
        text = item.name
    }


    @BindingAdapter("electionDate")
    fun TextView.setElectionDate(item: Election?) {
        item?.let {
            //  text = item.electionDay.toString()     //this needs to be formatted
            //    text = convertToDateString(item.electionDay)//, context.resources)
            text = item.electionDay.toString()
        }
    }


    fun Fragment.setTitle(title: String) {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = title
        }
    }


}