package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection

//TO DO remove queries that are bloat.
@Dao
interface ElectionDao {

    //allows all elections to be saved to db, used with the elections repo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedElection(followedElection: FollowedElection)

    @Update //used to update data into the database
    suspend fun update(followedElection: FollowedElection)

    //Is Election Followed CHECK 1 ---
   // @Query("SELECT EXISTS (SELECT 1 FROM follow_election_table WHERE follow_id = :id)")
    @Query("SELECT EXISTS (SELECT 1 FROM follow_election_table WHERE follow_id = :id)")
    fun isElectionFollowed(id: Int): LiveData<Boolean>

//    @Update
//    suspend fun update(election: Election)

    //Get all elections from this query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElections(): LiveData<List<Election>>

    //get all saved elections to show the followed ones------- this one doesnt work
   // @Query("SELECT * FROM election_table WHERE id in (SELECT id follow_election_table) ORDER BY electionDay DESC")
   // fun getFollowedElections(): LiveData<List<Election>>

    //this one gets the elections only for the upcoming elecs. Dont use the one above
  //  @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM follow_election_table WHERE follow_id == id ) ORDER BY electionDay DESC")
   @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM follow_election_table WHERE id == follow_id) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>



    //Get election when id matches.
 //        @Query("SELECT * from election_table WHERE id = :key")
  //          suspend fun getSelectedElection(key: Int): Election?

    //WHAT IS THIS FOR??
    //TO DO: Add select single election query
    @Query("SELECT * FROM election_table ORDER BY id DESC LIMIT 1")
    fun getElection(): Election?

    // ---- method for passing the followed Election id -- TRY THIS ANOTHER WAY
    @Query("INSERT INTO follow_election_table (follow_id) VALUES (:electionID)")
    suspend fun followElection(electionID: Int)
    suspend fun followElection(election: Election) {
        followElection(election.id)
        followElection(election)
    }


    //TO DO: Add clear query
    @Query("DELETE FROM election_table")
    fun clearAllElections()

    @Query("DELETE FROM follow_election_table")
    suspend fun clearFollowed()

    //delete followed election
    //@Query("DELETE FROM follow_election_table WHERE follow_id =:electionId")
    @Query("DELETE FROM follow_election_table WHERE follow_id =:electionId")
    suspend fun unfollowElection(electionId: Int) {
        unfollowElection(electionId)
       // clearAllElections()
    }

//the case expression is similar to the IF-THEN-ELSE statement
    //@Query("SELECT CASE follow_id WHEN NULL THEN 0 ELSE 1 END FROM follow_election_table WHERE follow_id =:idElection")
    @Query("SELECT CASE follow_id WHEN NULL THEN 0 ELSE 1 END FROM follow_election_table WHERE follow_id =:idElection")
    fun isElectionsFollowed(idElection: Int): LiveData<Int>
}