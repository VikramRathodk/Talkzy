package com.devvikram.talkzy

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class AppUtils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getDatePartition(timeStump: Long): String {
            // dd-mm-yyyy
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val date = Date(timeStump)
            return sdf.format(date)
        }
    }
}