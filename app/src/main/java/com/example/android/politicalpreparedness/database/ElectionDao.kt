package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection

@Dao
interface ElectionDao {

    //allows all elections to be saved to db, used with the elections repo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg elections: Election)

    //TO DO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowedElection(followedElection: FollowedElection)

    @Query("SELECT EXISTS (SELECT 1 FROM follow_election_table WHERE follow_id = :id)")
    fun isElectionFollowed(id: Int): LiveData<Boolean> //<Int>

    @Update
    suspend fun update(election: Election)

    //TO DO: Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElections(): LiveData<List<Election>>

    //get all saved elections to show the followed ones-------
    @Query("SELECT * FROM election_table WHERE id in (SELECT id follow_election_table) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    //use SQL case examples - if followId is NULL then 0, else it is 1 and END. use follow table
    //try Foreign key for the follow button

    //Get election when id matches.
    @Query("SELECT * from election_table WHERE id = :key")
    suspend fun getSelectedElection(key: Int): Election?

    //TO DO: Add select single election query
    @Query("SELECT * FROM election_table ORDER BY id DESC LIMIT 1")
    fun getElection(): Election?

    //method for passing the followed Election id
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
    @Query("DELETE FROM follow_election_table WHERE follow_id =:electionId")
    suspend fun unfollowElection(electionId: Int) {
        unfollowElection(electionId)
        //clearFollowed()
        clearAllElections()
    }
// ----- this method simply checks if the election is followed
//the case expression is similar to the IF-THEN-ELSE statement
    @Query("SELECT CASE follow_id WHEN NULL THEN 0 ELSE 1 END FROM follow_election_table WHERE follow_id =:idElection")
    fun isElectionsFollowed(idElection: Int): LiveData<Int>

}