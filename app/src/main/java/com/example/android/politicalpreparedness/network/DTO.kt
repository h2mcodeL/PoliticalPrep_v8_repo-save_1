package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.database.DatabaseElection
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.squareup.moshi.JsonClass
import java.util.*

/** Election represents an item that can played*/

@JsonClass(generateAdapter = true)
data class NetworkElectionContainer(val election: List<NetworkElection>)


@JsonClass(generateAdapter = true)
data class NetworkElection(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division)

/**
        Converted Network results to database objects
 */

fun NetworkElectionContainer.asDomainModel(): List<Election> {
    return election.map {
        Election (
        id = it.id,
        name = it.name,
        electionDay = it.electionDay,
        division = it.division)
    }
}

 fun List<NetworkElection>.asDatabaseModel(): Array<DatabaseElection> {
         return map {
                 DatabaseElection(
                         id = it.id,
                         name = it.name,
                         electionDay = it.electionDay,
                         division = it.division)
         }.toTypedArray()
 }
