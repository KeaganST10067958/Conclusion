package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keagan.conclusion.domain.model.Entry
import com.keagan.conclusion.domain.model.EntryType
import com.keagan.conclusion.domain.repo.EntryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class NotesViewModel(private val repo: EntryRepository) : ViewModel() {

    val notes = repo.observe(EntryType.NOTE)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(title: String, content: String?) {
        viewModelScope.launch {
            repo.upsert(
                Entry(
                    id = "",
                    userId = "local",
                    type = EntryType.NOTE,
                    title = title,
                    content = content,
                    date = null,
                    status = null,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch { repo.delete(id) }
    }
}
