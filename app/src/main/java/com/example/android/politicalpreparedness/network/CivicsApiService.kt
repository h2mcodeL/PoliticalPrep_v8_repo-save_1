package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// TO DO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
//get electionsAdapter from jsonadapter.., Date format refer to square docs.
//this is the convertor
private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(ElectionAdapter())     //this is added to enable the electionDay to work correctly
        .add(KotlinJsonAdapterFactory())        //this needs to be moved after the custom adapters.
        .build()


//moshi converter
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())   //coroutine call adapter factory
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()


/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */


interface CivicsApiService {
    @GET("elections")
    suspend fun getElectionResults(): ElectionResponse   //do not supply a request body with this method

    //TO DO: Add voterinfo API Call -  refer to google civics parameters address, electionId
    @GET("voterinfo")   //"voterinfo" as required for http request
    suspend fun getVoterInfo(
            @Query("address") address: String,  //this is throwing an error. Check on civics api
            @Query("electionId") electionId: Int): VoterInfoResponse     //deferred added here

    //TO DO: Add representatives API Call
    @GET("representatives")
    suspend fun getRepresentatives(
            @Query("address") address: String): RepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}

