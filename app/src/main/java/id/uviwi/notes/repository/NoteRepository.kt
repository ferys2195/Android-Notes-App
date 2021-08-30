package id.uviwi.notes.repository

import id.uviwi.notes.model.Note
import id.uviwi.notes.room.NoteDao

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun getNote() = noteDao.getNote()
    suspend fun insertNote(note : Note) = noteDao.insertNote(note)
    suspend fun deleteNote(uuid : Int) = noteDao.deleteNote(uuid)
}