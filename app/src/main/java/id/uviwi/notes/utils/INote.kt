package id.uviwi.notes.utils

import id.uviwi.notes.model.Note

interface INote {
    fun onClick(note : Note)
    fun onLongClick(note : Note)
}