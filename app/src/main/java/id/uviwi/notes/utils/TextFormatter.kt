package id.uviwi.notes.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TextFormatter {
    @SuppressLint("SimpleDateFormat")
    fun fromDate(date : Date) : String{
        val pattern = "MMM dd, yyyy"
        return SimpleDateFormat(pattern).format(date).toString()
    }
    @SuppressLint("SimpleDateFormat")
    fun fromDateDetail(date : Date) : String{
        val pattern = "MMMM dd, yyyy HH:mm"
        return SimpleDateFormat(pattern).format(date).toString()
    }

    fun textInfo(date: Date, charCount: Int): String {
        return "${fromDateDetail(date)} | $charCount Characters"
    }
}