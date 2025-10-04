package com.keagan.conclusion.util

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Minimal, in-memory service locator to satisfy screen dependencies.
 * - AuthManagerStub exposes a fake signed-in account.
 * - NoteRepositoryStub provides simple local notes with flows.
 */
object ServiceLocator {

    // region Auth
    data class Account(
        val id: String,
        val displayName: String,
        val email: String,
        val photoUrl: String? = null
    )

    class AuthManagerStub {
        private var cached: Account? = Account(
            id = "local",
            displayName = "Keagan Shaw",
            email = "keagancollege2002@gmail.com",
            photoUrl = null
        )

        fun lastAccount(context: Context): Account? = cached

        fun signIn(context: Context, onResult: (Boolean) -> Unit = {}) {
            // pretend it succeeded
            if (cached == null) {
                cached = Account(id = "local", displayName = "Keagan Shaw", email = "keagancollege2002@gmail.com")
            }
            onResult(true)
        }

        fun signOut(context: Context, onResult: (Boolean) -> Unit = {}) {
            cached = null
            onResult(true)
        }
    }
    // endregion

    // region Notes
    data class Note(
        val id: Long,
        val title: String,
        val content: String,
        val createdAt: Long = System.currentTimeMillis()
    )

    interface NoteRepository {
        fun observe(): StateFlow<List<Note>>
        suspend fun addNote(title: String, content: String)
        suspend fun deleteNote(id: Long)
    }

    class NoteRepositoryStub : NoteRepository {
        private val _notes = MutableStateFlow<List<Note>>(emptyList())
        private var counter = 0L

        override fun observe(): StateFlow<List<Note>> = _notes

        override suspend fun addNote(title: String, content: String) {
            val n = Note(id = ++counter, title = title.trim(), content = content.trim())
            _notes.update { it + n }
        }

        override suspend fun deleteNote(id: Long) {
            _notes.update { it.filterNot { n -> n.id == id } }
        }
    }
    // endregion

    // exposed singletons (what your screens use)
    val authManager = AuthManagerStub()
    val noteRepo: NoteRepository = NoteRepositoryStub()

    // noop init to mirror your old code signature; call from MainActivity if you want
    fun init(@Suppress("UNUSED_PARAMETER") context: Context) = Unit
}
