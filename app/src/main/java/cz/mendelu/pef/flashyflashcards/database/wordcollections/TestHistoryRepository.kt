package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity
import kotlinx.coroutines.flow.Flow

interface TestHistoryRepository {

    fun getAllTestHistoryByCollectionId(id: Long?): Flow<List<TestHistoryEntity>>
    suspend fun createNewTestHistory(testHistoryEntity: TestHistoryEntity)
    suspend fun deleteTestHistory(testHistoryEntity: TestHistoryEntity)
}