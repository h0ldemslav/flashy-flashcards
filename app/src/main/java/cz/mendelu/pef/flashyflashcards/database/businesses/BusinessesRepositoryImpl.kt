package cz.mendelu.pef.flashyflashcards.database.businesses

import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BusinessesRepositoryImpl @Inject constructor(
    private val businessesDao: BusinessesDao
) : BusinessesRepository {
    override fun getBusinessByRemoteId(businessRemoteId: String): Flow<BusinessEntity?> {
        return businessesDao.getBusinessByRemoteId(businessRemoteId)
    }

    override fun getAllBusinesses(): Flow<List<BusinessEntity>> {
        return businessesDao.getAllBusinesses()
    }

    override suspend fun addNewBusiness(businessEntity: BusinessEntity) {
        businessesDao.addNewBusiness(businessEntity)
    }

    override suspend fun deleteBusiness(businessEntity: BusinessEntity) {
        businessesDao.deleteBusiness(businessEntity)
    }
}