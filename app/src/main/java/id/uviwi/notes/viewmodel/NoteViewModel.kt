package id.uviwi.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.uviwi.notes.model.Note
import id.uviwi.notes.repository.NoteRepository
import id.uviwi.notes.utils.Resource
import kotlinx.coroutines.Dispatchers

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    fun getNote() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(noteRepository.getNote()))
        }catch (e : Exception){
            emit(Resource.error(null,"an error $e"))
        }
    }

    fun insertNote(note : Note) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(noteRepository.insertNote(note)))
        }catch (e : Exception){
            emit(Resource.error(null,"an error $e"))
        }
    }

    fun deleteNote(ids : ArrayList<Note>) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(noteRepository.deleteNote(ids)))
        }catch (e : Exception){
            emit(Resource.error(null,"an error $e"))
        }
    }
}