package id.uviwi.notes.room

import androidx.room.*
import id.uviwi.notes.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    suspend fun getNote() : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note)

    @Query("DELETE FROM Note WHERE id=:uuid")
    suspend fun deleteNote(uuid : Int)
}