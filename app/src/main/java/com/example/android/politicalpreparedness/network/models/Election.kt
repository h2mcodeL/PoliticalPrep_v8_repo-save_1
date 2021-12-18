package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")   //each @Entity class represents and SQLite table
@Parcelize data class Election(
        @PrimaryKey(autoGenerate = true) var id: Int,        //each entity needs a primary key annotate with autogenerate, use var rather than val
        @ColumnInfo(name = "name")val name: String, //@ColumInfo specifies the name of the column in the table
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
):Parcelable

//each property stored in the database needs to have public visibility Kotlin default. i.e. val, rather than private val

//Table for followed elections
@Entity(tableName = "follow_election_table")
data class FollowedElection(
        @PrimaryKey var followId: Int)

