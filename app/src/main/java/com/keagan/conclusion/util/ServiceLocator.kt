package com.keagan.conclusion.util

import android.content.Context
import com.keagan.conclusion.R
import com.keagan.conclusion.auth.AuthManager
import com.keagan.conclusion.data.local.AppDatabase
import com.keagan.conclusion.data.repo.NoteRepositoryImpl
import com.keagan.conclusion.data.repo.TaskRepositoryImpl
import com.keagan.conclusion.domain.repo.NoteRepository
import com.keagan.conclusion.domain.repo.TaskRepository
import com.keagan.conclusion.data.remote.PlannerApi
import com.keagan.conclusion.data.remote.RetrofitProvider

object ServiceLocator {
    lateinit var appContext: Context
        private set

    // DB & repositories
    lateinit var db: AppDatabase
        private set
    lateinit var noteRepo: NoteRepository
        private set
    lateinit var taskRepo: TaskRepository
        private set

    // Auth & streak
    lateinit var authManager: AuthManager
        private set
    lateinit var streakManager: StreakManager
        private set

    // Optional REST API
    lateinit var api: PlannerApi
        private set

    fun init(context: Context) {
        appContext = context.applicationContext

        // Room
        db = AppDatabase.get(appContext)
        noteRepo = NoteRepositoryImpl(db.noteDao())
        taskRepo = TaskRepositoryImpl(db.taskDao())

        // Google Sign-In
        val webClientId = appContext.getString(R.string.server_client_id)
        authManager = AuthManager(webClientId)

        // Streak/quotes
        streakManager = StreakManager(appContext)

        // Retrofit API scaffold (replace base URL when your API is ready)
        api = RetrofitProvider.api(
            baseUrl = "https://YOUR-API.example/" // <- must end with a '/'
        )
    }
}
