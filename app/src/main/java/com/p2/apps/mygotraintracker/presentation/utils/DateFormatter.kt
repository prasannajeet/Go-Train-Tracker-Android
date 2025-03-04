package com.p2.apps.mygotraintracker.presentation.utils

import kotlinx.datetime.LocalDateTime
import java.util.Locale

object DateFormatter {
    
    /**
     * Formats a LocalDateTime to a user-friendly string
     * Example: "15 March 2023"
     */
    fun formatToReadableDate(dateTime: LocalDateTime): String {
        return "${dateTime.dayOfMonth} ${dateTime.month.name.lowercase().replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
        }} ${dateTime.year}"
    }
    
    /**
     * Formats a LocalDateTime to a time string
     * Example: "14:30"
     */
    fun formatToTime(dateTime: LocalDateTime): String {
        return "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
    }
    
    /**
     * Formats a date string in YYYYMMDD format to a readable date
     * Example: "20230315" -> "15 March 2023"
     */
    fun formatDateString(dateString: String): String {
        if (dateString.length != 8) return dateString
        
        val year = dateString.substring(0, 4).toInt()
        val month = dateString.substring(4, 6).toInt()
        val day = dateString.substring(6, 8).toInt()
        
        val monthName = when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
        
        return "$day $monthName $year"
    }
} 