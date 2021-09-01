package id.uviwi.notes.utils

import id.uviwi.notes.databinding.ItemBinding
import id.uviwi.notes.model.Note

interface INote {
    fun onClick(note : Note, position : Int, view : ItemBinding)
    fun onLongClick(note : Note, position : Int, view : ItemBinding)
}