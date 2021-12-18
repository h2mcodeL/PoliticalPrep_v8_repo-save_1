package com.example.android.politicalpreparedness.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 Functions created to format a string that can be used in a TextView
 */

private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

@SuppressLint("SimpleDateFormat")
fun convertToDateString(date: Date): String {
    return SimpleDateFormat("dd-MMM-yy' ")     //Time: HH:mm:ss' EDT yyyy")
            .format(date).toString()
}