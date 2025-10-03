package com.keagan.conclusion.util

import android.content.Context
import com.keagan.conclusion.R
import com.keagan.conclusion.auth.AuthManager
import com.keagan.conclusion.data.local.AppDatabase
import com.keagan.conclusion.data.repo.NoteRepositoryImpl
import com.keagan.conclusion.domain.repo.NoteRepository

object ServiceLocator {
    lateinit var appContext: Context
        private set

    // Room
    lateinit var db: AppDatabase
        private set

    // Notes repo
    lateinit var noteRepo: NoteRepository
        private set

    // Google auth
    lateinit var authManager: AuthManager
        private set

    fun init(context: Context) {
        appContext = context.applicationContext

        db = AppDatabase.get(appContext)
        noteRepo = NoteRepositoryImpl(db.noteDao())

        val webClientId = appContext.getString(R.string.server_client_id)
        authManager = AuthManager(webClientId)
    }
}
