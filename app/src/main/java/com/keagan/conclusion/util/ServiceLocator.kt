package com.keagan.conclusion.util

import android.content.Context
import com.keagan.conclusion.R
import com.keagan.conclusion.auth.AuthManager
import com.keagan.conclusion.data.local.AppDatabase
import com.keagan.conclusion.data.repo.EntryRepositoryImpl
import com.keagan.conclusion.domain.repo.EntryRepository

object ServiceLocator {
    lateinit var appContext: Context
        private set

    lateinit var db: AppDatabase
        private set

    lateinit var entryRepo: EntryRepository
        private set

    lateinit var authManager: AuthManager
        private set

    fun init(context: Context) {
        appContext = context.applicationContext

        // Room
        db = AppDatabase.get(appContext)
        entryRepo = EntryRepositoryImpl(db.entryDao())

        // Google SSO – just pass the WEB client ID string
        val webClientId = appContext.getString(R.string.server_client_id)
        authManager = AuthManager(webClientId)
    }
}
