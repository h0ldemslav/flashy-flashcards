package cz.mendelu.pef.flashyflashcards.database.businesses

import cz.mendelu.pef.flashyflashcards.model.entities.BusinessEntity
import kotlinx.coroutines.flow.Flow

interface BusinessesRepository {

    fun getBusinessByRemoteId(businessRemoteId: String): Flow<BusinessEntity?>
    fun getAllBusinesses(): Flow<List<BusinessEntity>>
    suspend fun addNewBusiness(businessEntity: BusinessEntity)
    suspend fun deleteBusiness(businessEntity: BusinessEntity)
}