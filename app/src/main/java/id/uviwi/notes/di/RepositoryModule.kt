package id.uviwi.notes.di

import id.uviwi.notes.repository.NoteRepository
import org.koin.dsl.module

val repoModule = module {
    factory {
        NoteRepository(get())
    }
}