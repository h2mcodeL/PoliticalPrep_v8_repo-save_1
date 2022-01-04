package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse(
        val election: Election,
        val pollingLocations: String? = null, //TO DO: Future Use
        /*val contests: String? = null, //TO DO: Future Use - comment out as this throws an error */
        val state: List<State>? = null,
        val electionElectionOfficials: List<ElectionOfficial>? = null
)