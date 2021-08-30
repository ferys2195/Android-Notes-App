package id.uviwi.notes

import android.app.Application
import id.uviwi.notes.di.databaseModule
import id.uviwi.notes.di.repoModule
import id.uviwi.notes.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(databaseModule, repoModule, viewModelModule))
        }
    }
}