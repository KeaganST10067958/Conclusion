package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keagan.conclusion.domain.model.Note
import com.keagan.conclusion.domain.repo.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class NotesViewModel(
    private val repo: NoteRepository
) : ViewModel() {

    val notes: StateFlow<List<Note>> =
        repo.observeAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(title: String, content: String?) = viewModelScope.launch {
        repo.upsert(
            Note(
                id = "",
                userId = "local", // later: Google uid or your backend user id
                title = title,
                content = content,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
    }

    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }
}
