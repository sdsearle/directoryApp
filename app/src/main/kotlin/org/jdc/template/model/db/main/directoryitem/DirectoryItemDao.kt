package org.jdc.template.model.db.main.directoryitem

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectoryItemDao {
    @Query("SELECT id, lastName, firstName, image FROM individual ORDER BY lastName, firstName")
    fun findAllDirectItemsFlow(): Flow<List<DirectoryItem>>
}
