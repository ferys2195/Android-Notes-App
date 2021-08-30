package id.uviwi.notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val text : String,
    val date : Date
) : Parcelable
