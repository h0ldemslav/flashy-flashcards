package cz.mendelu.pef.flashyflashcards.database.wordcollections

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.flashyflashcards.model.WordCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordCollectionsDao {

    @Query("SELECT * FROM word_collections")
    fun getAllWordCollections(): Flow<List<WordCollectionEntity>>

    @Insert
    suspend fun createNewWordCollection(collectionEntity: WordCollectionEntity)

    @Update
    suspend fun updateWordCollection(collectionEntity: WordCollectionEntity)

    @Delete
    suspend fun deleteWordCollection(collectionEntity: WordCollectionEntity)
}