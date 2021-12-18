package com.example.android.politicalpreparedness.utils.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

//TODO: implement the onReceive method to receive the geofencing events at the background
     //    if (intent.action == ACTION_GEOFENCE_EVENT) {
     //     if (intent.action == GeofenceTransitionsJobIntentService.ACTION_GEOFENCE_EVENT)
       GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
    }
}