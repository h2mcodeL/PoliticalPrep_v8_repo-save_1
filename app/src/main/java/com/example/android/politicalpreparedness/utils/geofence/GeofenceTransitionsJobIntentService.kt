package com.example.android.politicalpreparedness.utils.geofence

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : /*GeofenceTransitions*/JobIntentService() {

//import com.udacity.project4.utils.sendNotification
//import kotlinx.coroutines.*


    private var coroutineJob: Job = Job()
    /*override*/ val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        // private const val errorMessage = "Error"
        const val ACTION_GEOFENCE_EVENT = "ACTION_GEOFENCE_EVENT"

        //        TO DO: call this to start the JobIntentService to handle the geofencing transition events
        //this is called from the GeoBroadcast
        fun enqueueWork(context: Context, intent: Intent) {
            //  fun enqueueWork(context: Context, intent: Intent, JOB_ID: Int, intent1: Class<GeofenceTransitionsJobIntentService>) {
            enqueueWork(
                    context,
                    GeofenceTransitionsJobIntentService::class.java, this.JOB_ID, intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        TODO("Not yet implemented")
    }

    /*
    //new code
    override fun onHandleWork(intent: Intent) {
        // handle the geofencing transition events and
        // send a notification to the user when he enters the geofence area
        // call @sendNotification
        Log.i("onHandleWork", "Job received")
        //Timber.i("Job received")
        val event = GeofencingEvent.fromIntent(intent)

        if (event.hasError()) {
            Log.d("GeofenceTransitionJob", "Event error: ${event.errorCode}")
            return
        }

        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendNotification(event.triggeringGeofences)
           // Timber.i("$event.geofenceTransition")
        }
    }

    // get the request id of the current geofence
    private fun sendNotification(triggeringGeofences: List<Geofence>) {
        Log.d("sendNotification", "sendNotification: Called ")
        val requestId = triggeringGeofences[0].requestId
        //Get the local repository instance
        val remindersLocalRepository: ReminderDataSource by inject()
//        Interaction to the repository has to be through a coroutine scope
        CoroutineScope(coroutineContext).launch(SupervisorJob()) {
            //get the reminder with the request id
            val result = remindersLocalRepository.getReminder(requestId)
            if (result is Result.Success<ReminderDTO>) {
                val reminderDTO = result.data
                Log.d("sendNotification", "sendNotification: Title is ${reminderDTO.title}")
                //send a notification to the user with the reminder details
                sendNotification(
                        this@GeofenceTransitionsJobIntentService,
                        ReminderDataItem(
                        reminderDTO.title,
                        reminderDTO.description,
                        reminderDTO.location,
                        reminderDTO.latitude,
                        reminderDTO.longitude,
                        reminderDTO.id
                )
                )
            }
        }
    }

     */
}