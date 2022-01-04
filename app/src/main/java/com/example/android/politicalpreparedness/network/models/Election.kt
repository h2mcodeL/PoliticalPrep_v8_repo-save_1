package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")
@Parcelize
data class Election(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
) : Parcelable



//Table for followed elections
@Entity(tableName = "follow_election_table")
data class FollowedElection(
        @PrimaryKey var id: Int)

