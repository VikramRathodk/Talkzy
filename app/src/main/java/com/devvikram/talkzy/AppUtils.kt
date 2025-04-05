package com.devvikram.talkzy

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AppUtils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getDatePartition(timeStump: Long): String {
            // dd-mm-yyyy
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val date = Date(timeStump)
            return sdf.format(date)
        }

        fun getTimeFromTimeStamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        }


        fun getTime(timestamp: Long): String {
            Log.d(TAG, "getTime: ${getTimeFromTimeStamp(timestamp = timestamp)}")
            val now = System.currentTimeMillis()
            val date = Date(timestamp)

            val calendarNow = Calendar.getInstance()
            val calendarTimestamp = Calendar.getInstance().apply { time = date }

            return when {
                // Same day
                calendarNow.get(Calendar.YEAR) == calendarTimestamp.get(Calendar.YEAR) &&
                        calendarNow.get(Calendar.DAY_OF_YEAR) == calendarTimestamp.get(Calendar.DAY_OF_YEAR) -> {
                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()) // 24-hour format
                    sdf.format(date)
                }

                // Yesterday
                calendarNow.get(Calendar.YEAR) == calendarTimestamp.get(Calendar.YEAR) &&
                        calendarNow.get(Calendar.DAY_OF_YEAR) - calendarTimestamp.get(Calendar.DAY_OF_YEAR) == 1 -> {
                    "Yesterday"
                }

                // Older dates
                else -> {
                    val sdf = SimpleDateFormat("MMM d", Locale.getDefault()) // Example: Apr 4
                    sdf.format(date)
                }
            }
        }

    }
}