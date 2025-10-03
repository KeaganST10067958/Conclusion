package com.keagan.conclusion.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entries WHERE type = :type ORDER BY updatedAt DESC")
    fun observeByType(type: String): Flow<List<EntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(e: EntryEntity)

    @Delete
    suspend fun delete(e: EntryEntity)

    @Query("SELECT * FROM entries WHERE id = :id")
    suspend fun find(id: String): EntryEntity?
}
