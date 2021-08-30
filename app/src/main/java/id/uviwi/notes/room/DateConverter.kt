package id.uviwi.notes.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class DateConverter {
  /*  @TypeConverter
    fun toDate(value: Long): Date {
        //If your result always returns 1970, then comment this line
        //val date =  Date(value)

        //If your result always returns 1970, then uncomment this line

        return Date(value * 1000L)
    }

    @TypeConverter
    fun fromDate(date:Date):Long{
        return date.time
    }
*/
    @TypeConverter
    fun dateToJson(date : Date) : String = Gson().toJson(date)
    @TypeConverter
    fun jsonToDate(json : String) : Date = Gson().fromJson(json, Date::class.java)
}