//package com.example.android.politicalpreparedness.database
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.example.android.politicalpreparedness.network.models.Division
//import com.example.android.politicalpreparedness.network.models.Election
//import java.util.*
//
///* Create the election database object type @Entity */
//
//@Entity
//data class DatabaseElection constructor(
//        @PrimaryKey
//        val id: Int,
//        val name: String,
//        val electionDay: Date,
//        val division: Division)
//
////this extention function converts from a database object to a domain object
//
//fun List<DatabaseElection>.asDomainModel(): List<Election> {
//    return map {
//        Election(
//                id = it.id,
//                name = it.name,
//                electionDay = it.electionDay,
//                division = it.division)
//    }
//}
