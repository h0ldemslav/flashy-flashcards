package cz.mendelu.pef.flashyflashcards.database.wordcollections

import cz.mendelu.pef.flashyflashcards.model.entities.TestHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TestHistoryRepositoryImpl @Inject constructor(
    private val testHistoryDao: TestHistoryDao
) : TestHistoryRepository {
    override fun getAllTestHistoryByCollectionId(id: Long?): Flow<List<TestHistoryEntity>> {
        return testHistoryDao.getAllTestHistoryByCollectionId(id)
    }

    override suspend fun createNewTestHistory(testHistoryEntity: TestHistoryEntity) {
        testHistoryDao.createNewTestHistory(testHistoryEntity)
    }

    override suspend fun deleteTestHistory(testHistoryEntity: TestHistoryEntity) {
        testHistoryDao.deleteTestHistory(testHistoryEntity)
    }
}