package id.uviwi.notes.di

import id.uviwi.notes.viewmodel.NoteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        NoteViewModel(get())
    }
}